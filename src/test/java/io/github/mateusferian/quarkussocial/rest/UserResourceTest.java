package io.github.mateusferian.quarkussocial.rest;

import io.github.mateusferian.quarkussocial.rest.dto.ResponseError;
import io.github.mateusferian.quarkussocial.rest.dto.UserRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceTest {

    private static String URL_USERS = "/users";

    @Test
    @DisplayName("Should create a user successfully")
    @Order(1)
    public void saveTest() {
        UserRequest user = new UserRequest();
        user.setName("testAPI");
        user.setAge(19);

        var response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(URL_USERS)
                .then()
                .log().all()
                .extract().response();

        assertEquals(201, response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("Should return error whn json is not valid")
    @Order(2)
    public void saveUserValidationErrorTest(){

        UserRequest user = new UserRequest();
        user.setName(null);
        user.setAge(0);

        var response = given()
                            .contentType(ContentType.JSON)
                            .body(user)
                        .when()
                            .post(URL_USERS)
                        .then()
                            .log().all()
                        .extract().response();

        assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.statusCode());
        assertEquals("Validation error",response.jsonPath().getString("message"));

        List<Map<String,String>> errors = response.jsonPath().getList("errors");

        assertNotNull(errors.get(0).get("message"));
        assertNotNull(errors.get(1).get("message"));

    }

    @Test
    @DisplayName("should list all users")
    @Order(3)
    public void listAllUserTest(){

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL_USERS)
                .then()
                .statusCode(200);
//                .body("size()", Matchers.is(1));

    }
}