package com.github.vkpro.hw07;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vkpro.hw06.p2.CustomWebServer;
import com.github.vkpro.hw07.annotations.*;
import com.github.vkpro.hw07.controller.UserController;
import com.github.vkpro.hw07.service.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Extended API server with REST routing using Custom annotations.
 */
public class CustomApiWebServer extends CustomWebServer {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Route> routes = new HashMap<>();

    public CustomApiWebServer(int port, int threadPoolSize, boolean useVirtualThreads) {
        super(port, threadPoolSize, useVirtualThreads);
    }

    public static void main(String[] args) {
        var server = new CustomApiWebServer(8080, 50, true);
        UserService service = new UserService();
        UserController controller = new UserController(service);
        server.registerController(controller);

        try {
            server.start();
            server.printEndpoints();
            Thread.sleep(600_000); // keep alive
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }

    /**
     * Register a controller annotated with @CustomRestController
     */
    public void registerController(Object controller) {
        Class<?> clazz = controller.getClass();
        if (!clazz.isAnnotationPresent(CustomRestController.class)) return;

        String basePath = clazz.getAnnotation(CustomRequestMapping.class).value();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(CustomGetMapping.class))
                addRoute("GET", basePath + method.getAnnotation(CustomGetMapping.class).value(), controller, method);
            else if (method.isAnnotationPresent(CustomPostMapping.class))
                addRoute("POST", basePath + method.getAnnotation(CustomPostMapping.class).value(), controller, method);
            else if (method.isAnnotationPresent(CustomPutMapping.class))
                addRoute("PUT", basePath + method.getAnnotation(CustomPutMapping.class).value(), controller, method);
            else if (method.isAnnotationPresent(CustomDeleteMapping.class))
                addRoute("DELETE", basePath + method.getAnnotation(CustomDeleteMapping.class).value(), controller, method);
            else if (method.isAnnotationPresent(CustomPatchMapping.class))
                addRoute("PATCH", basePath + method.getAnnotation(CustomPatchMapping.class).value(), controller, method);
        }
    }

    private void addRoute(String httpMethod, String fullPath, Object controller, Method method) {
        boolean hasPathVar = fullPath.contains("{");
        routes.put(httpMethod + " " + fullPath, new Route(httpMethod, fullPath, hasPathVar, new Handler(controller, method)));
        System.out.println("Registered " + httpMethod + " " + fullPath);
    }

    @Override
    protected void handleEndpoint(OutputStream out, String method, String path, String body) throws IOException {
        // Check if the request matches a registered API route
        Route route = routes.get(method + " " + path);
        if (route != null && !route.hasPathVar()) {
            invokeHandler(route.handler(), out, body);
            return;
        }

        // Match with path variable (e.g. /api/v1/users/{id})
        for (Route r : routes.values()) {
            if (!r.method().equals(method) || !r.hasPathVar()) continue;
            String prefix = r.path().substring(0, r.path().indexOf("{"));
            if (path.startsWith(prefix)) {
                String idPart = path.substring(prefix.length());
                if (!idPart.contains("/")) {
                    try {
                        long id = Long.parseLong(idPart);
                        invokeHandler(r.handler(), out, body, id);
                        return;
                    } catch (NumberFormatException e) {
                        sendError(out, 400, "Invalid ID");
                        return;
                    }
                }
            }
        }

        // Fallback to parent (static and built-in endpoints)
        super.handleEndpoint(out, method, path, body);
    }

    private void invokeHandler(Handler handler, OutputStream out, String body, Object... extraArgs) throws IOException {
        try {
            Method m = handler.method();
            Object controller = handler.controller();

            // Build arguments array by mapping each parameter to its value
            Object[] args = Arrays.stream(m.getParameters())
                    .map(param -> {
                        try {
                            // Handle @CustomRequestBody: deserialize JSON body to object
                            if (param.isAnnotationPresent(CustomRequestBody.class)) {
                                if (body == null || body.isBlank()) {
                                    throw new RuntimeException("Request body is empty");
                                }
                                return objectMapper.readValue(body, param.getType());
                            }
                            // Handle @CustomPathVariable
                            if (param.isAnnotationPresent(CustomPathVariable.class) && extraArgs.length > 0)
                                return extraArgs[0];
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to parse body", e);
                        }
                        return null;
                    })
                    .toArray();

            Object result = m.invoke(controller, args);

            if (m.getReturnType().equals(void.class)) {
                out.write("HTTP/1.1 204 No Content\r\n\r\n".getBytes());
                return;
            }

            sendJson(out, objectMapper.writeValueAsString(result));
        } catch (Exception e) {
            e.printStackTrace();
            sendError(out, 500, e.getMessage());
        }
    }

    private void printEndpoints() {
        System.out.printf("""
                Visit:
                    http://localhost:%d
                API Endpoints:
                    curl http://localhost:%d/api/v1/users
                %n""", getPort(), getPort());
    }

    private record Handler(Object controller, Method method) {
    }

    private record Route(String method, String path, boolean hasPathVar, Handler handler) {
    }
}
