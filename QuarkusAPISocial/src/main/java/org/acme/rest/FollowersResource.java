package org.acme.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.dto.FollowerRequest;
import org.acme.domain.entities.Follower;
import org.acme.domain.repository.FollowersRepository;
import org.acme.domain.repository.UserRepository;

@Path("/users/{userId}/followers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FollowersResource {

    @Inject
    FollowersRepository followersRepository;
    @Inject
    UserRepository userRepository;

    @PUT
    @Transactional
    public Response followUser(@PathParam("userId") Long userId, FollowerRequest request){
        var user = userRepository.findById(userId);
        if (user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        var follower = userRepository.findById(request.getFollowerId());

        if (follower == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        boolean follows = followersRepository.isFollowing(follower, user);
        if(!follows){
            Follower newFollower = new Follower();
            newFollower.setUser(user);
            newFollower.setFollower(follower);
            followersRepository.persist(newFollower);
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }


}
