package io.github.mateusferian.quarkussocial.rests;

import io.github.mateusferian.quarkussocial.domains.models.FollowerModel;
import io.github.mateusferian.quarkussocial.domains.repositories.FollowerRepository;
import io.github.mateusferian.quarkussocial.domains.repositories.UserRepository;
import io.github.mateusferian.quarkussocial.rests.dtos.responses.FollowerPerUserResponseDTO;
import io.github.mateusferian.quarkussocial.rests.dtos.requests.FollowerRequestDTO;
import io.github.mateusferian.quarkussocial.rests.dtos.responses.FollowerResponseDTO;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.stream.Collectors;

@Path("/users/{userid}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    private final UserRepository userRepository;

    private final FollowerRepository followerRepository;

    @Inject
    public FollowerResource(FollowerRepository followerRepository, UserRepository userRepository){

        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
    }

    @PUT
    @Transactional
    public Response followerUser(
            @PathParam("userid") Long userId, FollowerRequestDTO request){

        if(userId.equals(request.getIdFollower())){
            return  Response.status(Response.Status.CONFLICT)
                    .entity("You can't follower yourself")
                    .build();
        }

        var user = userRepository.findById(userId);
        if(user ==  null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var follower = userRepository.findById(request.getIdFollower());

        boolean followers = followerRepository.followers(follower, user);

        if(!followers){
            var entity = new FollowerModel();
            entity.setFollower(follower);
            entity.setUser(user);

            followerRepository.persist(entity);
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    public Response listFollowers(@PathParam("userid") Long userId){

        var user = userRepository.findById(userId);
        if(user ==  null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        var list = followerRepository.findByUser(userId);

        FollowerPerUserResponseDTO responseObject = new FollowerPerUserResponseDTO();

        responseObject.setFollowersCount(list.size());

        var followersList =
                list.stream().map(FollowerResponseDTO::new).collect(Collectors.toList());

        responseObject.setContent(followersList);

        return Response.ok(responseObject).build();
    }

    @DELETE
    @Transactional
    public Response unFollowUser(
            @PathParam("userid") Long userId,
            @QueryParam("followerId") Long followerId){
        var user = userRepository.findById(userId);
        if(user ==  null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        followerRepository.deleteByFollowerAndUser(followerId, userId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
