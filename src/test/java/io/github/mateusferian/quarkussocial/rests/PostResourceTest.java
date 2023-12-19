package io.github.mateusferian.quarkussocial.rests;

import io.github.mateusferian.quarkussocial.domains.models.FollowerModel;
import io.github.mateusferian.quarkussocial.domains.models.PostModel;
import io.github.mateusferian.quarkussocial.domains.models.UserModel;
import io.github.mateusferian.quarkussocial.domains.repositories.FollowerRepository;
import io.github.mateusferian.quarkussocial.domains.repositories.PostRepository;
import io.github.mateusferian.quarkussocial.domains.repositories.UserRepository;
import io.github.mateusferian.quarkussocial.rests.dtos.requests.PostRequestDTO;
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

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    public UserRepository userRepository;

    @Inject
    public FollowerRepository followerRepository;

    @Inject
    public PostRepository postRepository;

    public static Long USER_ID;

    public static Long USER_ID_NONEXISTENT = 999L;

    public static Long USER_ID_FOLLOWER;

    public static Long USER_ID_NOT_FOLLOWER;

    public static Long FOLLOWER_ID_NONEXISTENT = 999L;

    @BeforeEach
    @Transactional
    public void setUP(){
        UserModel user = new UserModel();
        user.setName("testAPI");
        user.setAge(19);
        userRepository.persist(user);
        USER_ID = user.getId();

        UserModel userNotFollower = new UserModel();
        userNotFollower.setName("testFollowerNot");
        userNotFollower.setAge(19);
        userRepository.persist(userNotFollower);
        USER_ID_NOT_FOLLOWER = userNotFollower.getId();

        UserModel userFollower = new UserModel();
        userFollower.setName("testFollower");
        userFollower.setAge(19);
        userRepository.persist(userFollower);
        USER_ID_FOLLOWER = userFollower.getId();

        FollowerModel follower = new FollowerModel();
        follower.setUser(user);
        follower.setFollower(userFollower);
        followerRepository.persist(follower);

        PostModel post = new PostModel();
        post.setText("hello");
        post.setUser(user);
        postRepository.persist(post);
    }

    @Test
    @DisplayName("Should create a post successfully")
    public void savePostTest() {
        PostRequestDTO postRequest = new PostRequestDTO();
        postRequest.setText("Some text");

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParams("userid",USER_ID)
                .when()
                .post()
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("Should return 404 when trying to make a post for an nonexistent user")
    public void postPostUserNotFoundTest() {
        PostRequestDTO postRequest = new PostRequestDTO();
        postRequest.setText("Some text");

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParams("userid",USER_ID_NONEXISTENT)
                .when()
                .post()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Should return posts")
    public void listPostsTest() {
        given()
                .pathParams("userid",USER_ID)
                .header("followerId",USER_ID_FOLLOWER)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("size()",Matchers.is(1));
    }

    @Test
    @DisplayName("Should return 404 when user doesn't exist")
    public void listPostUserNotFoundTest() {

        given()
                .pathParams("userid",USER_ID_NONEXISTENT)
                .when()
                .get()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Should return 40 when followerId header is not present")
    public void listPostPorFollowerHeaderNotSendTest() {
        given()
                .pathParams("userid",USER_ID)
                .when()
                .get()
                .then()
                .statusCode(400)
                .body(Matchers.is("You forget the header followerId"));
    }

    @Test
    @DisplayName("Should return 404 when follower doesn't exist")
    public void listPostFollowerNotFoundTest() {
        given()
                .pathParams("userid",USER_ID)
                .header("followerId",FOLLOWER_ID_NONEXISTENT)
                .when()
                .get()
                .then()
                .statusCode(400)
                .body(Matchers.is("there is no follower with this id"));
    }

    @Test
    @DisplayName("Should return 404 when follower isn't follower")
    public void listPostNotFollowerTest() {
        given()
                .pathParams("userid",USER_ID)
                .header("followerId",USER_ID_NOT_FOLLOWER)
                .when()
                .get()
                .then()
                .statusCode(403)
                .body(Matchers.is("You can't see these posts"));
    }
}
