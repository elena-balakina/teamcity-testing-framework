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

    public static ResponseSpecification checkProjectIdWithNonLetterCharacter(String projectId) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        responseSpecBuilder.expectBody(Matchers.containsString(("Project ID \"%s\" is invalid: starts with non-letter character '%s'. ID should start with a latin letter " +
                "and contain only latin letters, digits and underscores (at most 225 characters).").formatted(projectId, projectId.charAt(0))));
        return responseSpecBuilder.build();
    }

    public static ResponseSpecification checkProjectIdWithNonLatinCharacter(String projectId) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        responseSpecBuilder.expectBody(Matchers.containsString(("Project ID \"%s\" is invalid: contains non-latin letter '%s'. ID should start with a latin letter " +
                "and contain only latin letters, digits and underscores (at most 225 characters).").formatted(projectId, projectId.charAt(0))));
        return responseSpecBuilder.build();
    }

    public static ResponseSpecification checkProjectIdTooLong(String projectId) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        responseSpecBuilder.expectBody(Matchers.containsString(("Project ID \"%s\" is invalid: it is %s characters long while the maximum length is 225. " +
                "ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters).").formatted(projectId, projectId.length())));
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

    public static ResponseSpecification checkNoBuildTypeFound(String projectId) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_NOT_FOUND);
        responseSpecBuilder.expectBody(Matchers.containsString(("Nothing is found by locator 'count:1,project:%s'").formatted(projectId)));
        return responseSpecBuilder.build();
    }
}
