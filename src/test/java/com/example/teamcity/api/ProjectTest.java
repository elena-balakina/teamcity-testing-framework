package com.example.teamcity.api;

import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.Role;
import com.example.teamcity.api.models.Roles;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.example.teamcity.api.enums.Endpoint.PROJECTS;
import static com.example.teamcity.api.enums.Endpoint.USERS;

@Test(groups = {"Regression"})
public class ProjectTest extends BaseApiTest {

    @Test(description = "Project can be created by Super user", groups = {"Positive", "CRUD"})
    public void superAdminCanCreateProject() {
        var superuserCheckedRequests = new CheckedRequests(Specifications.superUserSpec());
        var createdProject = superuserCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        softy.assertEquals(createdProject.getId(), testData.getProject().getId(), "Actual project id does not match expected");
        softy.assertEquals(createdProject.getName(), testData.getProject().getName(), "Actual project name does not match expected");
    }

    @Test(description = "Project can be created by Admin", groups = {"Positive", "CRUD"})
    public void adminCanCreateProject() {
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        var createdProject = userCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        softy.assertEquals(createdProject.getId(), testData.getProject().getId(), "Actual project id does not match expected");
        softy.assertEquals(createdProject.getName(), testData.getProject().getName(), "Actual project name does not match expected");
    }

    @Test(description = "Project with empty name can not be created by Super user", groups = {"Negative", "CRUD"})
    public void superuserCanNotCreateProjectWithEmptyName() {
        var projectWithEmptyName = Project.builder().name("").build();
        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
                .create(projectWithEmptyName)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project name cannot be empty."));
    }

    @Test(description = "Project with empty name can not be created by Admin", groups = {"Negative", "CRUD"})
    public void userCanNotCreateProjectWithEmptyName() {
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var projectWithEmptyName = Project.builder().name("").build();
        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
                .create(projectWithEmptyName)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project name cannot be empty."));
    }

    @Test(description = "Project with the same id can not be created by Super user", groups = {"Negative", "CRUD"})
    public void superAdminCanNotCreateProjectWithTheSameId() {
        var superuserCheckedRequests = new CheckedRequests(Specifications.superUserSpec());
        superuserCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        var projectWithSameId = Project.builder()
                .id(testData.getProject().getId())
                .name(RandomData.getString())
                .build();
        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
                .create(projectWithSameId)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project ID \"%s\" is already used by another project".formatted(testData.getProject().getId())));
    }

    @Test(description = "Project with the same id can not be created by Admin", groups = {"Negative", "CRUD"})
    public void adminCanNotCreateProjectWithTheSameId() {
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        userCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        var projectWithSameId = Project.builder()
                .id(testData.getProject().getId())
                .name(RandomData.getString())
                .build();
        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
                .create(projectWithSameId)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project ID \"%s\" is already used by another project".formatted(testData.getProject().getId())));
    }

    @Test(description = "Project can not be created by Project viewer", groups = {"Negative", "CRUD"})
    public void projectViewerCanNotCreateProject() {
        var role = Role.builder().roleId("PROJECT_VIEWER").scope("g").build();
        var roles = Roles.builder().role(Arrays.asList(role)).build();
        var projectViewerUser = User.builder()
                .username(RandomData.getString())
                .password(RandomData.getString())
                .roles(roles)
                .build();
        superUserCheckedRequests.getRequest(USERS).create(projectViewerUser);
        new UncheckedBase(Specifications.authSpec(projectViewerUser), PROJECTS)
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .body(Matchers.containsString("You do not have \"Create subproject\" permission in project with internal id: _Root"));
    }
}
