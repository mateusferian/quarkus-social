package io.github.mateusferian.quarkussocial.rests;

import io.github.mateusferian.quarkussocial.domains.models.UserModel;
import io.github.mateusferian.quarkussocial.domains.repositories.UserRepository;
import io.github.mateusferian.quarkussocial.rests.dtos.errors.ResponseError;
import io.github.mateusferian.quarkussocial.rests.dtos.requests.UserRequestDTO;
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
    public Response save(UserRequestDTO userRequest){

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(userRequest);
        if(!violations.isEmpty()){
            return ResponseError.createFromValidation(violations).withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);

        }

        UserModel user = new UserModel();
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
        PanacheQuery<UserModel> query = userRepository.findAll();
        return Response.ok(query.list()).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        UserModel user = userRepository.findById(id);
        if(user != null){
            userRepository.delete(user);
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, UserRequestDTO userRequest) {

        UserModel user = userRepository.findById(id);

        if(user != null){
            user.setName(userRequest.getName());
            user.setAge(userRequest.getAge());
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
