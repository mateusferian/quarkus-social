package io.github.mateusferian.quarkussocial.rests;

import io.github.mateusferian.quarkussocial.domains.models.FollowerModel;
import io.github.mateusferian.quarkussocial.domains.models.UserModel;
import io.github.mateusferian.quarkussocial.domains.repositories.FollowerRepository;
import io.github.mateusferian.quarkussocial.domains.repositories.UserRepository;
import io.github.mateusferian.quarkussocial.rests.dtos.requests.FollowerRequestDTO;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
class FollowerResourceTest {

    @Inject
    public UserRepository userRepository;

    @Inject
    public FollowerRepository followerRepository;

    public static Long USER_ID;

    public static Long USER_ID_NONEXISTENT = 999L;

    public static Long USER_ID_FOLLOWER;

    @BeforeEach
    @Transactional
    public void setUp(){
        UserModel user = new UserModel();
        user.setName("testAPI");
        user.setAge(19);
        userRepository.persist(user);
        USER_ID = user.getId();

        UserModel userFollower = new UserModel();
        userFollower.setName("testFollower");
        userFollower.setAge(19);
        userRepository.persist(userFollower);
        USER_ID_FOLLOWER = userFollower.getId();

        FollowerModel follower = new FollowerModel();
        follower.setUser(user);
        follower.setFollower(userFollower);
        followerRepository.persist(follower);
    }

    @Test
    @DisplayName("Should return 409 when followerId is equal to user id")
    public void someUserAsFollowerTest(){

        FollowerRequestDTO followerRequest = new FollowerRequestDTO();
        followerRequest.setIdFollower(USER_ID);

        given()
                .contentType(ContentType.JSON)
                .body(followerRequest)
                .pathParams("userid",USER_ID)
                .when()
                .put()
                .then()
                .statusCode(409)
                .body(Matchers.is("You can't follower yourself"));
    }

    @Test
    @DisplayName("Should return 404 on follower a user when user id doesn't exist")
    public void userNotFoundWhenTryingToFollowerTest(){

        FollowerRequestDTO followerRequest = new FollowerRequestDTO();
        followerRequest.setIdFollower(USER_ID);

        given()
                .contentType(ContentType.JSON)
                .body(followerRequest)
                .pathParams("userid",USER_ID_NONEXISTENT)
                .when()
                .put()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Should follow a user")
    public void followerUserTest(){

        FollowerRequestDTO followerRequest = new FollowerRequestDTO();
        followerRequest.setIdFollower(USER_ID_FOLLOWER);

        given()
                .contentType(ContentType.JSON)
                .body(followerRequest)
                .pathParams("userid",USER_ID)
                .when()
                .put()
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("Should return 404 on list user follower and user when user id doesn't exist")
    public void userNotFoundWhenListingFollowersTest(){

        given()
                .contentType(ContentType.JSON)
                .pathParams("userid",USER_ID_NONEXISTENT)
                .when()
                .get()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Should list a user's followers")
    public void listFollowersTest(){

        var response = given()
                .contentType(ContentType.JSON)
                .pathParams("userid",USER_ID)
                .when()
                .get()
                .then()
                .extract().response();

        var followersCount = response.jsonPath().get("followersCount");
        var followerListContent = response.jsonPath().getList("content");

        assertEquals(200, response.statusCode());
        assertEquals(followersCount, 1);
        assertEquals(1, followerListContent.size());
    }

    @Test
    @DisplayName("Should return 404 on follower a user and user id doesn't exist")
    public void userNotFoundWhenUnFollowingAUserTest(){

        given()
                .pathParams("userid",USER_ID_NONEXISTENT)
                .queryParam("followerId",USER_ID_FOLLOWER)
                .when()
                .delete()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Should Unfollow an user")
    public void unfollowUserTest(){

        given()
                .pathParams("userid",USER_ID)
                .queryParam("followerId",USER_ID_FOLLOWER)
                .when()
                .delete()
                .then()
                .statusCode(204);
    }
}