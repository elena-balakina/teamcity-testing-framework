package com.example.teamcity.api;

import com.example.teamcity.api.models.User;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.List;

public class DummyTest extends BaseApiTest {

    @Test
    public void userShouldBeAbleGetAllProjects() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .filters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()))
                .get("http://admin:admin@172.24.208.1:8111/app/rest/projects/");
    }

    @Test
    public void unauthTest() {
        RestAssured
                .given()
                .spec(Specifications.unauthSpec())
                .get("/app/rest/projects/");
    }

    @Test
    public void authTest() {
        User user = User.builder().username("admin").password("admin").build();
        RestAssured
                .given()
                .spec(Specifications.authSpec(user))
                .get("/app/rest/projects/");
    }

}
