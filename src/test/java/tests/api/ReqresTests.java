package tests.api;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;


public class ReqresTests {

    String userUrl = "/users/2",
            regUrl = "/register",
            notFoundUrl = "/unknown/23";

    @BeforeEach
    void testSetUp(){
        baseURI = "https://reqres.in/";
    }

    @Test
    void getUser (){
        String name = "Janet",
                lastName = "Weaver";
        int id = 2;

        get(userUrl)
                .then()
                .log().body()
                .log().status()
                .statusCode(200)
                .body("data.id", equalTo(id))
                .body("data.first_name", is(name))
                .body("data.last_name", is(lastName));
    }


    @Test
    void updateUser(){
        String name = "morpheus",
                job = "zion resident",
                reqBody = "{\"name\": \"morpheus\",\"job\": \"zion resident\"}";

        given()
                .body(reqBody)
                .log().body()
                .contentType(JSON)
                .when()
                .put(userUrl)
                .then()
                .statusCode(200)
                .body("name", is(name))
                .body("job", is(job));
    }

    @Test
    void delete (){
        when()
                .delete(userUrl)
                .then()
                .log().status()
                .statusCode(204);
    }

    @Test
    void unsuccessfulReg(){
        String req = "{ \"email\": \"sydney@fife\" }",
                error = "Missing email or username";

        given()
                .body(req)
                .when()
                .post(regUrl)
                .then()
                .statusCode(400)
                .body("error", is(error));
    }

    @Test
    void notFound(){
        get(notFoundUrl)
                .then()
                .statusCode(404);

    }

}
