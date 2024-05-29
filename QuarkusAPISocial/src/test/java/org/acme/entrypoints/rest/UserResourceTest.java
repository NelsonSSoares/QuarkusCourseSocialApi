package org.acme.entrypoints.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.domain.dto.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserResourceTest {

    @Test
    @DisplayName("should create a user successfully")
    public void createUserTest() {
        var user = new UserRequest("Billy Joe",30);

        var response = given()
                            .contentType(ContentType.JSON)
                            .body(user)
                        .when()
                            .post("/users")
                        .then()
                            .extract().response();

        assertEquals(201, response.getStatusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }
}