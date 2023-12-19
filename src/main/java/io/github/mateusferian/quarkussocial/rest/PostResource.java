package io.github.mateusferian.quarkussocial.rest;

import io.github.mateusferian.quarkussocial.domain.model.Post;
import io.github.mateusferian.quarkussocial.domain.model.User;
import io.github.mateusferian.quarkussocial.domain.repository.FollowerRepository;
import io.github.mateusferian.quarkussocial.domain.repository.PostRepository;
import io.github.mateusferian.quarkussocial.domain.repository.UserRepository;
import io.github.mateusferian.quarkussocial.rest.dto.PostRequest;
import io.github.mateusferian.quarkussocial.rest.dto.PostResponse;
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
    public Response save(@PathParam("userid") Long userId, PostRequest postRequest){
        User user = userRepository.findById(userId);

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Post post = new Post();
        post.setText(postRequest.getText());
        post.setUser(user);

        postRepository.persist(post);

        return Response.status(Response.Status.CREATED).entity(post).build();
    }

    @GET
    public Response findAll(
            @PathParam("userid") Long userId,
            @HeaderParam("followerId") Long followerId){

        User user = userRepository.findById(userId);

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(followerId == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("You forget the header followerId")
                    .build();
        }

        User follower = userRepository.findById(followerId);

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
        var postResponseList = list.stream().map(post -> PostResponse.fromEntity(post)).collect(Collectors.toList());

        return Response.ok(postResponseList).build();
    }
}
