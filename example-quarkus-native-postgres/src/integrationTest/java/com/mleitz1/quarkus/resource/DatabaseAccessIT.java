package com.mleitz1.quarkus.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

@QuarkusIntegrationTest
public class DatabaseAccessIT  {
    @Test
    public void testGetAllUsers() {
        Response response = given()
            .when().get("/users");

        response.then()
            .statusCode(200);
    }

    @Test
    public void testCreateUser() {
        String requestBody = """
                {
                    "name": "Test User",
                    "email": "test@example.com"
                }
                """;

        Response response = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when().post("/users");

        response.then()
            .statusCode(201)
            .header("Location", containsString("/users/"));
    }

    @Test
    public void testGetUserById() {
        // First create a user
        String requestBody = """
                {
                    "name": "Get User Test",
                    "email": "get-test@example.com"
                }
                """;

        System.out.println("[DEBUG_LOG] testGetUserById create request body: " + requestBody);

        Response createResponse = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when().post("/users");

        System.out.println("[DEBUG_LOG] testGetUserById create response status: " + createResponse.getStatusCode());
        System.out.println("[DEBUG_LOG] testGetUserById create response body: " + createResponse.getBody().asString());

        String location = createResponse.then()
            .statusCode(201)
            .extract().header("Location");

        // Extract the ID from the location
        String id = location.substring(location.lastIndexOf("/") + 1);
        System.out.println("[DEBUG_LOG] testGetUserById extracted id: " + id);

        // Then get the user by ID
        Response getResponse = given()
            .when().get("/users/" + id);

        System.out.println("[DEBUG_LOG] testGetUserById get response status: " + getResponse.getStatusCode());
        System.out.println("[DEBUG_LOG] testGetUserById get response body: " + getResponse.getBody().asString());

        getResponse.then()
            .statusCode(200)
            .body("name", is("Get User Test"))
            .body("email", is("get-test@example.com"))
            .body("id", is(Integer.parseInt(id)));
    }
}
