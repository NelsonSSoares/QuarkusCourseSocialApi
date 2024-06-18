package org.acme.entrypoints.rest;


import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.acme.domain.dto.FollowerRequest;
import org.acme.domain.entities.Follower;
import org.acme.domain.entities.User;
import org.acme.domain.repository.FollowersRepository;
import org.acme.domain.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(FollowersResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FollowerResourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    FollowersRepository followersRepository;

    Long userId;
    Long followerId;


    @BeforeEach
    @Transactional
    public void setup() {
        var user = new User();
        user.setName("Billy Joe");
        user.setAge(30);
        userRepository.persist(user);
        userId = user.getId();

        var follower = new User();
        user.setName("Joe Higashi");
        user.setAge(27);
        userRepository.persist(follower);
        followerId = user.getId();

        var followerEntity = new Follower();
        followerEntity.setUser(user);
        followerEntity.setFollower(follower);
        followersRepository.persist(followerEntity);
    }

    @Test
    @DisplayName("should return 409 when trying to follow yourself")
    @Order(1)
    public void sameUserAsFollowerTest(){

        var body = new FollowerRequest();

        given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .pathParam("userId", userId)
                .when()
                    .put()
                    .then()
                    .statusCode(Response.Status.CONFLICT.getStatusCode())
                .body(Matchers.is("You can't follow yourself"));
    }

    @Test
    @DisplayName("should return 404 when user not found")
    @Order(2)
    public void userNotFoundTest(){

        var body = new FollowerRequest();
        body.setFollowerId(userId);

        given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .pathParam("userId", userId)
                .when()
                    .put()
                    .then()
                    .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body(Matchers.is("You can't follow yourself"));
    }

    @Test
    @DisplayName("should follow a user successfully")
    @Order(3)
    public void followerNotFoundTest(){

        var body = new FollowerRequest();
        body.setFollowerId(userId);

        given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .pathParam("userId", userId)
                .when()
                    .put()
                    .then()
                    .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body(Matchers.is("You can't follow yourself"));
    }
    @Test
    @DisplayName("should return 404 on list user followers and user id not found")
    @Order(4)
    public void userNotFoundWhenTryingtoFollowTest(){
        var invalidUserId = 999L;

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", invalidUserId)
                .when()
                .get()
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }

    @Test
    @DisplayName("shold list a user's followers")
    @Order(5)
    public void listingFollowersTest(){

        var response = given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .when()
                .get()
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

        var followersCount = response.extract().jsonPath().get("followersCount");
        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode(200));
        assertEquals(1, followersCount);
    }

    @Test
    @DisplayName("should return 404 on unfollow user and user id not found")
    @Order(6)
    public void userNotFoundWhenUnfollowingAUserTest(){
        var invalidUserId = 999L;

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", invalidUserId)
                .queryParam("followerId", followerId)
                .when()
                .delete()
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }

    @Test
    @DisplayName("should unfollow a user successfully")
    @Order(6)
    public void unfollowUserTest(){


        given()
                .pathParam("userId", userId)
                .queryParam("followerId", followerId)
                .when()
                .delete()
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

    }
}
