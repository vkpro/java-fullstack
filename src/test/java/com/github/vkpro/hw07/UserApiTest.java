package com.github.vkpro.hw07;

import com.github.vkpro.hw07.controller.UserController;
import com.github.vkpro.hw07.service.UserService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTimeout;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserApiTest {
    private static CustomApiWebServer server;
    private static int createdUserId;

    @BeforeAll
    static void startServer() throws Exception {
        UserService service = new UserService();
        UserController controller = new UserController(service);

        server = new CustomApiWebServer(8080, 10, true);
        server.registerController(controller);
        server.start();

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        Thread.sleep(300); // give server a moment to start
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    private static io.restassured.config.HttpClientConfig httpClientConfig() {
        return io.restassured.config.HttpClientConfig.httpClientConfig()
                .setParam("http.connection.timeout", 5000)
                .setParam("http.socket.timeout", 5000);
    }

    @Test
    @Order(1)
    void createUser() {
        assertTimeout(Duration.ofSeconds(5), () -> {
            String newUser = """
                {
                    "name": "Alice",
                    "email": "alice@example.com"
                }
            """;

            createdUserId = given()
                    .config(RestAssured.config().httpClient(httpClientConfig()))
                    .header("Content-Type", "application/json")
                    .body(newUser)
                    .when()
                    .post("/api/v1/users")
                    .then()
                    .statusCode(anyOf(is(200), is(201)))
                    .body("name", equalTo("Alice"))
                    .body("email", equalTo("alice@example.com"))
                    .extract()
                    .path("id");
        });
    }

    @Test
    @Order(2)
    void getAllUsers() {
        assertTimeout(Duration.ofSeconds(5), () -> {
            given()
                    .config(RestAssured.config().httpClient(httpClientConfig()))
                    .when()
                    .get("/api/v1/users")
                    .then()
                    .statusCode(200)
                    .body("id", hasItem(createdUserId))
                    .body("name", hasItem("Alice"));
        });
    }

    @Test
    @Order(3)
    void getSingleUser() {
        assertTimeout(Duration.ofSeconds(5), () -> {
            given()
                    .config(RestAssured.config().httpClient(httpClientConfig()))
                    .when()
                    .get("/api/v1/users/" + createdUserId)
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(createdUserId))
                    .body("name", equalTo("Alice"))
                    .body("email", equalTo("alice@example.com"));
        });
    }

    @Test
    @Order(4)
    void updateUser() {
        assertTimeout(Duration.ofSeconds(5), () -> {
            String updatedUser = """
                {
                    "name": "Alice Updated",
                    "email": "alice.updated@example.com"
                }
            """;

            given()
                    .config(RestAssured.config().httpClient(httpClientConfig()))
                    .header("Content-Type", "application/json")
                    .body(updatedUser)
                    .when()
                    .put("/api/v1/users/" + createdUserId)
                    .then()
                    .statusCode(anyOf(is(200), is(204)));

            // verify update
            given()
                    .config(RestAssured.config().httpClient(httpClientConfig()))
                    .when()
                    .get("/api/v1/users/" + createdUserId)
                    .then()
                    .statusCode(200)
                    .body("name", equalTo("Alice Updated"))
                    .body("email", equalTo("alice.updated@example.com"));
        });
    }

    @Test
    @Order(5)
    void patchUser() {
        assertTimeout(Duration.ofSeconds(5), () -> {
            // PATCH: Update only the name (email should remain unchanged)
            String patchData = """
                {
                    "name": "Alice Patched"
                }
            """;

            given()
                    .config(RestAssured.config().httpClient(httpClientConfig()))
                    .header("Content-Type", "application/json")
                    .body(patchData)
                    .when()
                    .patch("/api/v1/users/" + createdUserId)
                    .then()
                    .statusCode(anyOf(is(200), is(204)));

            // Verify: name updated, email unchanged from previous PUT test
            given()
                    .config(RestAssured.config().httpClient(httpClientConfig()))
                    .when()
                    .get("/api/v1/users/" + createdUserId)
                    .then()
                    .statusCode(200)
                    .body("name", equalTo("Alice Patched"))
                    .body("email", equalTo("alice.updated@example.com"));
        });
    }

    @Test
    @Order(6)
    void deleteUser() {
        assertTimeout(Duration.ofSeconds(5), () -> {
            given()
                    .config(RestAssured.config().httpClient(httpClientConfig()))
                    .when()
                    .delete("/api/v1/users/" + createdUserId)
                    .then()
                    .statusCode(anyOf(is(200), is(204)));

            // verify deletion
            given()
                    .config(RestAssured.config().httpClient(httpClientConfig()))
                    .when()
                    .get("/api/v1/users/" + createdUserId)
                    .then()
                    .statusCode(200)
                    .body(equalTo("null"));
        });
    }
}