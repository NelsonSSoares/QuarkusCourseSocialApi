package org.acme.entrypoints.rest;


import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.entities.Post;
import org.acme.domain.entities.User;
import org.acme.domain.dto.CreatePostRequest;
import org.acme.domain.dto.PostResponse;
import org.acme.domain.repository.FollowersRepository;
import org.acme.domain.repository.PostRepository;
import org.acme.domain.repository.UserRepository;

import java.util.stream.Collectors;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users/{userId}/posts")
public class PostResource {

    @Inject
    private UserRepository userRepository;

    @Inject
    private PostRepository postRepository;

    @Inject
    private FollowersRepository followersRepository;


    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest createPostRequest){
        User user = userRepository.findById(userId);
        if (user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Post post = new Post();
        post.setText(createPostRequest.getText());
        post.setUser(user);
        postRepository.persist(post);
        return Response.status(Response.Status.CREATED).build();
    }
    @GET
    public Response listPosts(@PathParam("userId") Long userId, @HeaderParam("followerId") Long followerId){
        User user = userRepository.findById(userId);
        if (user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        //para fazer ordenação
        //var query = postRepository.find("user",Sort.by("dateTime"), user);
        User follower = userRepository.findById(followerId);

        if (follower == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Inexistent follower ID").build();
        }

        boolean follows =  followersRepository.isFollowing(follower, user);

        if (!follows){
            return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to see this content").build();
        }
        if (follower == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("You forgot to send follower id in the header").build();
        }

        var query = postRepository.find("user", user);
        var list = query.list();
        var postResponse = list.stream().map(post -> PostResponse.fromEntity(post)).collect(Collectors.toList());
        //.map(PostResponse::fromEntity).collect(Collectors.toList());
        return Response.ok(postResponse).build();
    }
}
