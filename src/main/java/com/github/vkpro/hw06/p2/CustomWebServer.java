package com.github.vkpro.hw06.p2;

import com.github.vkpro.hw06.p1.CustomExecutorService;
import lombok.Getter;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple multi-threaded web server using CustomExecutorService
 */
public class CustomWebServer {
    public static final String STATIC_DIR = "src/main/resources/static";
    @Getter
    private final int port;
    private final CustomExecutorService executor;
    private ServerSocket serverSocket;
    private volatile boolean running = false;

    // Server statistics
    private final AtomicInteger requestCount = new AtomicInteger(0);
    private final long startTime = System.currentTimeMillis();

    public CustomWebServer(int port, int threadPoolSize, boolean useVirtualThreads) {
        this.port = port;
        this.executor = useVirtualThreads ? CustomExecutorService.withVirtualThreadPerTask()
                : CustomExecutorService.withPlatformThreadPool(threadPoolSize);
    }

    public static void main(String[] args) {
        var virtualServer = new CustomWebServer(8080, 50, true);
        var platformServer = new CustomWebServer(8081, 50, false);

        try {
            virtualServer.start();
            platformServer.start();

            printEndpoints();

            // Keep servers running
            Thread.sleep(600000); // Run for 1 minute
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            virtualServer.stop();
            platformServer.stop();
        }
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;

        System.out.println("Server started on port " + port);

        Thread.ofPlatform().start(() -> {
            while (running) {
                try {
                    Socket client = serverSocket.accept();
                    executor.execute(() -> handleClient(client));
                } catch (IOException e) {
                    if (running) System.err.println("Accept error: " + e.getMessage());
                }
            }
        });
    }

    public void stop() {
        running = false;

        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            System.err.println("Close error: " + e.getMessage());
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Server stopped");
    }

    private void handleClient(Socket client) {
        try (client;
             var in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             var out = client.getOutputStream()) {

            String requestLine = in.readLine();
            String[] parts = requestLine.split(" ");
            requestCount.incrementAndGet();
            System.out.println("Request: " + requestLine);

            int contentLength = 0;
            String line;
            while (!(line = in.readLine()).isEmpty()) {
                if (line.startsWith("Content-Length: ")) {
                    contentLength = Integer.parseInt(line.substring(16));
                }
            }

            String body = "";
            if (contentLength > 0) {
                char[] buffer = new char[contentLength];
                in.read(buffer, 0, contentLength);
                body = new String(buffer);
            }

            String method = parts[0];
            String path = parts[1];
            handleEndpoint(out, method, path, body);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void handleEndpoint(OutputStream out, String method, String path, String body) throws IOException {
        switch (method + " " + path) {
            case "GET /" -> serveFile(out, "index.html");
            case "GET /api/time" -> sendJson(out, "{\"time\": \"" + java.time.Instant.now() + "\"}");
            case "GET /api/stats" -> {
                long uptime = System.currentTimeMillis() - startTime;
                sendJson(out, String.format("{\"requests\": %d, \"uptime_seconds\": %.1f}",
                        requestCount.get(), uptime / 1000.0));
            }
            case "POST /api/echo" -> sendJson(out, "{\"echo\": \"" + body + "\"}");
            default -> {
                if ("GET".equals(method) && path.startsWith("/static/")) {
                    serveFile(out, path.substring(8)); // Remove "/static/"
                } else {
                    sendError(out, 404, "Not Found");
                }
            }
        }
    }

    protected void sendJson(OutputStream out, String json) throws IOException {
        String response = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n" +
                "Content-Length: " + json.length() + "\r\n\r\n" + json;
        out.write(response.getBytes());
        out.flush();
    }

    private void serveFile(OutputStream out, String fileName) throws IOException {
        Path staticDir = Paths.get(STATIC_DIR).toAbsolutePath().normalize();  // Absolute and normalized root
        Path requestedPath = Paths.get(STATIC_DIR, fileName).toAbsolutePath().normalize();     // Normalize the requested path (removes ../)

        // Check: requestedPath must start with staticDir (does not escape boundaries)
        if (!requestedPath.startsWith(staticDir)) {
            sendError(out, 403, "Forbidden");  // Deny access
            return;
        }

        // Additionally: Check that the file exists and is a regular file (not a directory)
        if (!Files.exists(requestedPath) || !Files.isRegularFile(requestedPath)) {
            sendError(out, 404, "Not Found");
            return;
        }

        byte[] content = Files.readAllBytes(requestedPath);

        out.write("HTTP/1.1 200 OK\r\nContent-Length: ".getBytes());
        out.write(String.valueOf(content.length).getBytes());
        out.write("\r\n\r\n".getBytes());
        out.write(content);
        out.flush();
    }

    protected void sendError(OutputStream out, int status, String message) throws IOException {
        String body = "<html><body><h1>" + status + " " + message + "</h1></body></html>";
        String response = "HTTP/1.1 " + status + " " + message + "\r\n" + "Content-Type: text/html\r\n" + "Content-Length: " + body.length() + "\r\n\r\n" + body;
        out.write(response.getBytes());
        out.flush();
    }

    private static void printEndpoints() {
        System.out.println("""
                Visit:
                    http://localhost:8080 (Virtual Threads)
                    http://localhost:8081 (Platform Threads)
                
                API Endpoints:
                    curl http://localhost:8080/api/time              # Current time
                    curl http://localhost:8080/api/stats             # Server stats
                    curl -X POST -d '{"msg":"hi"}' http://localhost:8080/api/echo  # Echo
                """);
    }
}