package org.acme.entrypoints.rest;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users/{userId}/posts")
public class PostResource {

    @POST
    public Response savePost(){
        return Response.status(Response.Status.CREATED).build();
    }
    @GET
    public Response listPosts(){
        return Response.ok().build();
    }
}
