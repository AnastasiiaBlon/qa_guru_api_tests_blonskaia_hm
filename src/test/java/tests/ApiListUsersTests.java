package tests;

import models.lombok.LombokBodyModel;
import models.lombok.LombokCheckEmailModel;
import models.lombok.LombokResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static specs.GeneralSpecs.*;

public class ApiListUsersTests extends TestBase {

    @Test
    @Tag("apiTest")
    @DisplayName("Check that users with ids as 7, 8, 9, 10, 11, 12 are in the response")
    void getUsersListTest() {
        List<Integer> expectedIds = List.of(7, 8, 9, 10, 11, 12);

        List<Integer> actualIds = step("Make request and get user IDs", () ->
                given(generalRequestSpec)
                        .queryParam("page", 2)
                        .when()
                        .get("/users")
                        .then()
                        .statusCode(200)
                        .body("data.size()", greaterThan(0))
                        .extract()
                        .jsonPath()
                        .getList("data.id", Integer.class)
        );

        step("Verify that all expected user IDs are present", () ->
                assertTrue(actualIds.containsAll(expectedIds),
                        "Response contains all expected user IDs"));
    }

    @Test
    @Tag("apiTest")
    @DisplayName("Check that user is created")
    void createStepsCustomLombokAllureUserTest() {
        LombokBodyModel requestBody = new LombokBodyModel();
        requestBody.setName("John");
        requestBody.setJob("QA Engineer");

        LombokResponseModel response = step("Make request", ()->
             given(generalRequestSpec)
                .body(requestBody)

                .when()
                     .post("/users")

                .then()
                     .spec(generalResponse201Spec)
                     .extract().as(LombokResponseModel.class));

        step("Check response", ()-> {
            assertEquals("John", response.getName());
            assertEquals("QA Engineer", response.getJob());
        });
    }

    @Test
    @Tag("apiTest")
    @DisplayName("Check that user's data is correctly updated")
    void updateUserTest() {
        LombokBodyModel requestBody = new LombokBodyModel();
        requestBody.setName("Mike");
        requestBody.setJob("Senior QA");
        int userId = 2;

        LombokResponseModel response = step("Make request to update user", () ->
                given(generalRequestSpec)
                        .body(requestBody)
                        .when()
                        .put("/users/" + userId)
                        .then()
                        .spec(generalResponse200Spec)
                        .extract().as(LombokResponseModel.class)
        );

        step("Verify response contains updated user data", () -> {
            assertEquals("Mike", response.getName());
            assertEquals("Senior QA", response.getJob());
        });
    }

    @Test
    @Tag("apiTest")
    @DisplayName("Check that user is deleted")
    void deleteUserTest() {
        int userId = 2;

        step("Send DELETE request to remove the user", () ->
                given(generalRequestSpec)
                        .when()
                        .delete("/users/" + userId)
                        .then()
                        .spec(generalResponse204Spec)
        );
    }

    @Test
    @Tag("apiTest")
    @DisplayName("Check that email (eve.holt@reqres.in) exists in the user list")
    void userEmailExistsTest() {
        String expectedEmail = "eve.holt@reqres.in";

        LombokCheckEmailModel responseEmailModel = step("Make request to get user emails", () ->
                given(generalRequestSpec)
                        .queryParam("page", 1)
                        .when()
                        .get("/users")
                        .then()
                        .spec(generalResponse200Spec)
                        .extract()
                        .as(LombokCheckEmailModel.class) // Deserialize JSON response into Lombok model
        );

        step("Verify that the expected email is present in the response", () ->
                assertTrue(responseEmailModel.getData().stream()
                                .anyMatch(user -> user.getEmail().equals(expectedEmail)),
                        "Response does not contain the expected email"));
    }
}
