package tests.api;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tests.api.lombok.*;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static tests.api.Specs.*;
import static org.hamcrest.Matchers.*;

public class ReqresWithLombokTests {

    String userUrl = "/users/2",
            listUsersUrl = "/users?page=2",
            loginUrl = "/login";

    @Test
    void getUser() {
        LombokUserData data = given()
                .spec(request)
                .when()
                .get(userUrl)
                .then()
                .spec(response200)
                .extract().as(LombokUserData.class);
        assertEquals(2, data.getUser().getId());
        assertEquals("Janet", data.getUser().getFirstName());
        assertEquals("Weaver", data.getUser().getLastName());
    }

    @Test
    void updateUser() {
        UserUpdateJobRequest updateJobRequest = new UserUpdateJobRequest();
        updateJobRequest.setName("morpheus");
        updateJobRequest.setJob("zion resident");
        Date date = new Date();
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        String d = formatter.format(date);

        UserUpdateJobResponse data = given()
                .spec(request)
                .body(updateJobRequest)
                .when()
                .put(userUrl)
                .then()
                .log().body()
                .spec(response200)
                .extract().as(UserUpdateJobResponse.class);
        assertEquals(updateJobRequest.getJob(), data.getJob());
        assertTrue(data.getUpdatedAt().contains(d));
    }

    @Test
    void delete() {
        given()
                .spec(request)
                .when()
                .delete(userUrl)
                .then()
                .spec(response204);
    }

    @Test
    void getUserWithFSurname() {
        given()
                .spec(request)
                .when()
                .get(listUsersUrl)
                .then()
                .log().body()
                .body("data.findAll{it.last_name =~ /F+/}.last_name.flatten()",
                        hasItem("Funke"));
    }

    @Test
    void successfulLogin() {
        RegisterRequest regReq = new RegisterRequest();
        regReq.setEmail("eve.holt@reqres.in");
        regReq.setPassword("cityslicka");

        RegisterResponse data = given()
                .spec(request)
                .body(regReq)
                .when()
                .post(loginUrl)
                .then()
                .spec(response200)
                .extract().as(RegisterResponse.class);
        assertNotNull(data.getToken());

    }


    @Test
    void unsuccessfulLogin() {
        RegisterRequest regReq = new RegisterRequest();
        regReq.setEmail("peter@klaven");

        RegisterResponse data = given()
                .spec(request)
                .body(regReq)
                .when()
                .post(loginUrl)
                .then()
                .spec(response400)
                .extract().as(RegisterResponse.class);
        assertTrue(data.getError().contains("Missing password"));
    }
}