package org.acme.entrypoints.rest;


import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.model.Post;
import org.acme.domain.model.User;
import org.acme.domain.model.dto.CreatePostRequest;
import org.acme.domain.repository.PostRepository;
import org.acme.domain.repository.UserRepository;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users/{userId}/posts")
public class PostResource {

    @Inject
    private UserRepository userRepository;

    @Inject
    private PostRepository postRepository;


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
    public Response listPosts(@PathParam("userId") Long userId){
        User user = userRepository.findById(userId);
        if (user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }
}
