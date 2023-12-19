package io.github.mateusferian.quarkussocial.rest;

import io.github.mateusferian.quarkussocial.domain.model.User;
import io.github.mateusferian.quarkussocial.domain.repository.UserRepository;
import io.github.mateusferian.quarkussocial.rest.dto.ResponseError;
import io.github.mateusferian.quarkussocial.rest.dto.UserRequest;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserRepository userRepository;
    private final Validator validator;

    @Inject
    public UserResource(UserRepository userRepository, Validator validator){

        this.userRepository = userRepository;
        this.validator = validator;
    }

    @POST
    @Transactional
    public Response save(UserRequest userRequest){

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);
        if(!violations.isEmpty()){
            return ResponseError.createFromValidation(violations).withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);

        }

        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        userRepository.persist(user);

        return Response
                .status(Response.Status.CREATED.getStatusCode())
                .entity(user)
                .build();
    }

    @GET
    public Response findAll(){
        PanacheQuery<User> query = userRepository.findAll();
        return Response.ok(query.list()).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        User user = userRepository.findById(id);
        if(user != null){
            userRepository.delete(user);
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, UserRequest userRequest) {

        User user = userRepository.findById(id);

        if(user != null){
            user.setName(userRequest.getName());
            user.setAge(userRequest.getAge());
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
