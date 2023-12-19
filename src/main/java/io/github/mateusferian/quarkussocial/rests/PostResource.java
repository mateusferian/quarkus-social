package io.github.mateusferian.quarkussocial.rests;

import io.github.mateusferian.quarkussocial.domains.models.PostModel;
import io.github.mateusferian.quarkussocial.domains.models.UserModel;
import io.github.mateusferian.quarkussocial.domains.repositories.FollowerRepository;
import io.github.mateusferian.quarkussocial.domains.repositories.PostRepository;
import io.github.mateusferian.quarkussocial.domains.repositories.UserRepository;
import io.github.mateusferian.quarkussocial.rests.dtos.requests.PostRequestDTO;
import io.github.mateusferian.quarkussocial.rests.dtos.responses.PostResponseDTO;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.stream.Collectors;

@Path("/users/{userid}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final FollowerRepository followerRepository;

    @Inject
    public PostResource(UserRepository userRepository, PostRepository postRepository, FollowerRepository followerRepository){

        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.followerRepository = followerRepository;
    }

    @POST
    @Transactional
    public Response save(@PathParam("userid") Long userId, PostRequestDTO postRequest){
        UserModel user = userRepository.findById(userId);

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        PostModel post = new PostModel();
        post.setText(postRequest.getText());
        post.setUser(user);

        postRepository.persist(post);

        return Response.status(Response.Status.CREATED).entity(post).build();
    }

    @GET
    public Response findAll(
            @PathParam("userid") Long userId,
            @HeaderParam("followerId") Long followerId){

        UserModel user = userRepository.findById(userId);

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(followerId == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("You forget the header followerId")
                    .build();
        }

        UserModel follower = userRepository.findById(followerId);

        if(follower == null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("there is no follower with this id")
                    .build();
        }

        boolean followers = followerRepository.followers(follower, user);

        if(!followers){
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("You can't see these posts")
                    .build();
        }

        var query= postRepository.find("user", Sort.by("dateTime", Sort.Direction.Descending),user);
        var list = query.list();
        var postResponseList = list.stream().map(post -> PostResponseDTO.fromEntity(post)).collect(Collectors.toList());

        return Response.ok(postResponseList).build();
    }
}
