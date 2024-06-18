package org.acme.entrypoints.rest;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.dto.UserRequest;
import org.acme.exceptions.ResponseError;
import org.hamcrest.Matchers;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.junit.jupiter.api.*;

import java.net.URL;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) //define a ordem de execução dos testes
class UserResourceTest {

    @TestHTTPResource("/users") // define rota base para os testes em uma variável
    URL apiURL;


    @Test
    @DisplayName("should create a user successfully")
    @Order(2) //define a ordem de execução do teste
    public void createUserTest() {
        var user = new UserRequest("Billy Joe",30);

        var response = given()
                            .contentType(ContentType.JSON)
                            .body(user)
                        .when()
                            .post(apiURL)
                        .then()
                            .extract().response();

        assertEquals(201, response.getStatusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("should return error when json is not valid")
    @Order(3)
    public void createUserValidationErrorTest(){


        var user = new UserRequest(null, null);

        var response = given()
                            .contentType(ContentType.JSON)
                            .body(user)
                        .when()
                            .post(apiURL)
                        .then()
                            .extract().response();

        assertEquals(Response.Status.BAD_REQUEST, response.statusCode());
        assertEquals("Validation Error",response.jsonPath().getString("message"));

        List<Map<String,String >> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0).get("message"));
        assertEquals("Age is required", errors.get(0).get("message"));
    }

    @Test
    @DisplayName("should list all users")
    @Order(4)
    public void listAllUsersTest(){
        var response = given()
                            .contentType(ContentType.JSON)
                        .when()
                            .get(apiURL)
                        .then()
                            .statusCode(200)
                            .body("size()", Matchers.is(1));


    }
}