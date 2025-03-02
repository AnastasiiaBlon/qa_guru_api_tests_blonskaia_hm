package tests;


import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;


public class BrowserConfigTest {

    @BeforeAll
    public static void configParams() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }
}