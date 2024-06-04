package org.acme.entrypoints.rest;


import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import org.acme.domain.dto.CreatePostRequest;
import org.acme.domain.entities.Follower;
import org.acme.domain.entities.Post;
import org.acme.domain.entities.User;
import org.acme.domain.repository.FollowersRepository;
import org.acme.domain.repository.PostRepository;
import org.acme.domain.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class) // define qual a api que será testada
public class PostResourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    FollowersRepository followersRepository;

    @Inject
    PostRepository postRepository;

    Long userId;
    Long userNotFollowedId;
    Long UserFollowerId;


    @BeforeEach
    @Transactional
    public void setup() {
        //configurações iniciais
        var user = new User();
        user.setName("Billy Joe");
        user.setAge(30);
        userRepository.persist(user);
        userId = user.getId();

        var userNotFollowed = new User();
        userNotFollowed.setName("Joe Billy");
        userNotFollowed.setAge(30);
        userRepository.persist(userNotFollowed);
        userNotFollowedId = userNotFollowed.getId();

        var userFollower = new User();
        userFollower.setName("Tom Canabrava");
        userFollower.setAge(30);
        userRepository.persist(userFollower);
        UserFollowerId = userFollower.getId();

        var follower = new Follower();
        follower.setUser(user);
        follower.setFollower(userFollower);
        followersRepository.persist(follower);

        Post post = new Post();
        post.setText("This is a post");
        post.setUser(user);
        postRepository.persist(post);
    }

    @Test
    @DisplayName("should create a post for a user")
    public void createPostTest() {

        var post = new CreatePostRequest();
        post.setText("This is a post");

        var userId = 1;

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(post)
                .pathParam("userId", userId) //define o valor do path param
                .when()
                .post()
                .then()
                .statusCode(201);

    }

    @Test
    @DisplayName("should return error when user does not exist")
    public void createPostUserNotFoundTest() {

        var post = new CreatePostRequest();
        post.setText("This is a post");

        var userId = 2;

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(post)
                .pathParam("userId", userId) //define o valor do path param
                .when()
                .post()
                .then()
                .statusCode(404);

    }

    @Test
    @DisplayName("should return 404 when user does not exist")
    public void listPostsUserNotFoundTest() {
        var userId = 999;

        given()
                .pathParam("userId", userId)
                .when()
                .get()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName(" should return 400 when follower header is not sent")
    public void listPostsFollowerHeaderNotSendTest() {
        var userId = 1;


        given()
                .pathParam("userId", userId)
                .when()
                .get()
                .then()
                .statusCode(400)
                .body(Matchers.is("You forgot the header followerId"));
        ;
    }

    @Test
    @DisplayName("should return 400 when follower does not exist")
    public void listPostFollowerNotFoundTest() {
        var userId = 1;
        var followerId = 999;

        given()
                .pathParam("userId", userId)
                .header("followerId", userNotFollowedId)
                .when()
                .get()
                .then()
                .statusCode(403)
                .body(Matchers.is("You cannot see this content"));
    }

    @Test
    @DisplayName("should return 403 when follower is not following the user")
    public void listPostNotAFollower() {
        var userId = 1;
        var followerId = userNotFollowedId;

        given()
                .pathParam("userId", userId)
                .header("followerId", followerId)
                .when()
                .get()
                .then()
                .statusCode(403)
                .body(Matchers.is("You are not allowed to see this content"));

    }

    @Test
    @DisplayName("should list all posts")
    public void listPostTest() {
        var userId = 1;
        var followerId = UserFollowerId;

        given()
                .pathParam("userId", userId)
                .header("followerId", followerId)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", Matchers.is(0));

    }

}


