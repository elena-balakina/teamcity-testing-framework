package com.example.teamcity.api.spec;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;

public class ValidationResponseSpecifications {

    public static ResponseSpecification checkProjectWithEmptyName() {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_BAD_REQUEST);
        responseSpecBuilder.expectBody(Matchers.containsString("Project name cannot be empty."));
        return responseSpecBuilder.build();
    }

    public static ResponseSpecification checkProjectWithIdAlreadyExist(String projectId) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_BAD_REQUEST);
        responseSpecBuilder.expectBody(Matchers.containsString("Project ID \"%s\" is already used by another project".formatted(projectId)));
        return responseSpecBuilder.build();
    }

    public static ResponseSpecification checkProjectWithNameAlreadyExist(String projectName) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_BAD_REQUEST);
        responseSpecBuilder.expectBody(Matchers.containsString("Project with this name already exists: %s".formatted(projectName)));
        return responseSpecBuilder.build();
    }

    public static ResponseSpecification checkUserCanNotCreateProject() {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_FORBIDDEN);
        responseSpecBuilder.expectBody(Matchers.containsString("You do not have \"Create subproject\" permission in project with internal id: _Root"));
        return responseSpecBuilder.build();
    }

    public static ResponseSpecification checkNoSourceProjectFound(String sourceProjectId) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_NOT_FOUND);
        responseSpecBuilder.expectBody(Matchers.containsString(("No project found by name or internal/external id '%s'.\n" +
                "Could not find the entity requested. Check the reference is correct and the user has permissions to access the entity.").formatted(sourceProjectId)));
        return responseSpecBuilder.build();
    }

    public static ResponseSpecification checkProjectIdIsInvalid(String projectId) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        responseSpecBuilder.expectBody(Matchers.containsString("Project ID \"%s\" is invalid:".formatted(projectId)));
        return responseSpecBuilder.build();
    }


}
