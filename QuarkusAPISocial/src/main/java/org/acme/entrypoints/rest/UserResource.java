package org.acme.entrypoints.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.acme.domain.entities.User;
import org.acme.domain.dto.UserRequest;
import org.acme.domain.repository.UserRepository;
import org.acme.exceptions.ResponseError;

import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class UserResource {

    private final UserRepository userRepository;
    private final Validator validator;

    //ADICIONAR DEPENDENCIA "rest-jackson" PARA LER O JSON
    @POST
    @Transactional
    public Response createUser(@Valid UserRequest userRequest){
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);
        if (!violations.isEmpty()){
//            ConstraintViolation<UserRequest> error = violations.stream().findAny().get();
//            String message = error.getMessage();
//            return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
            return ResponseError.createFromValidation(violations).withStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        }

        User user = new User();
        user.setName(userRequest.getName());
        user.setAge(userRequest.getAge());
        System.out.println(userRequest);
        userRepository.persist(user);
        return Response.status(Response.Status.CREATED).entity(user).build();

    }

    @GET
    public Response listAllUsers(){
        PanacheQuery<User> users = userRepository.findAll();
        return Response.ok(users.list()).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id){
       User user = userRepository.findById(id);
       if (user == null){
           return Response.status(Response.Status.NOT_FOUND).build();
       }else {
           userRepository.delete(user);
           return Response.noContent().build();
       }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, UserRequest userRequest){

        User user = userRepository.findById(id);
        if (user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }else {
            user.setName(userRequest.getName());
            user.setAge(userRequest.getAge());
            //userRepository.update(user);
            return Response.ok(user).build();
        }

    }

}
