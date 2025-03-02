package tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiListUsersTests extends BrowserConfigTest{

    @Test
    @DisplayName("Check that users with ids as 7, 8, 9, 10, 11, 12 are in the response")
    void getUsersListTest() {
        given()
                .queryParam("page", 2)  // We want users on page 2
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("data.size()", greaterThan(0))
                .body("data.find { it.id == 7 }", notNullValue())
                .body("data.find { it.id == 8 }", notNullValue())
                .body("data.find { it.id == 9 }", notNullValue())
                .body("data.find { it.id == 10 }", notNullValue())
                .body("data.find { it.id == 11 }", notNullValue())
                .body("data.find { it.id == 12 }", notNullValue())
                .body("data[0].id", notNullValue())
                .body("data[0].first_name", notNullValue())
                .body("data[0].last_name", notNullValue())
                .body("data[0].email", matchesPattern("^[A-Za-z0-9+_.-]+@(.+)$"));
    }

    @Test
    @DisplayName("Check that user is created")
    void createUserTest() {
        String requestBody = "{ \"name\": \"John\", \"job\": \"QA Engineer\" }";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("John"))
                .body("job", equalTo("QA Engineer"))
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Check that user's data is correctly updated")
    void updateUserTest() {
        String requestBody = "{ \"name\": \"Mike\", \"job\": \"Senior QA\" }";
        int userId = 2;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/users/" + userId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Mike"))
                .body("job", equalTo("Senior QA"));
    }

    @Test
    @DisplayName("Check that user is deleted")
    void deleteUserTest() {
        int userId = 2;

        given()
                .when()
                .delete("/users/" + userId)
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("Check that email (eve.holt@reqres.in) exists in the user list")
    void userEmailExistsTest() {
        String email = "eve.holt@reqres.in";

        given()
                .queryParam("page", 1)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("data.email", hasItem(email));
    }

}
