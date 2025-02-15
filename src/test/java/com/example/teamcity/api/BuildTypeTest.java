package com.example.teamcity.api;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.checked.CheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class BuildTypeTest extends BaseApiTest {

    @Test(description = "(with steps) User should be able to create build type", groups = {"Positive", "CRUD"})
    public void withStepsUserCanCreateBuildType() {
        var user = generate(User.class);

        step("Create user", () -> {
            var requester = new CheckedBase<User>(Specifications.superUserSpec(), USERS);
            requester.create(user);
        });

        var project = generate(Project.class);
        AtomicReference<String> projectId = new AtomicReference<>("");

        step("Create project by user", () -> {
            var requester = new CheckedBase<Project>(Specifications.authSpec(user), Endpoint.PROJECTS);
            projectId.set(requester.create(project).getId());
        });

        var buildType = generate(BuildType.class);
        buildType.setProject(Project.builder().id(projectId.get()).locator(null).build());

        var requester = new CheckedBase<BuildType>(Specifications.superUserSpec(), Endpoint.BUILD_TYPES);
        AtomicReference<String> buildTypeId = new AtomicReference<>("");

        step("Create build type for project by user", () -> {
            buildTypeId.set(requester.create(buildType).getId());
        });
        step("Check build type was created successfully with correct data", () -> {
            var createdBuildType = requester.read(buildTypeId.get());
            softy.assertEquals(createdBuildType.getName(), buildType.getName(), "Build type name is not correct");
        });
    }

    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCanCreateBuildType() {
        var user = generate(User.class);
        var userRequester = new CheckedBase<User>(Specifications.superUserSpec(), USERS);
        userRequester.create(user);

        var project = generate(Project.class);
        var projectRequester = new CheckedBase<Project>(Specifications.authSpec(user), Endpoint.PROJECTS);
        // кладем полученный созданный project в ту же переменную
        project = projectRequester.create(project);

        var buildType = generate(Arrays.asList(project), BuildType.class);

        var buildTypeRequester = new CheckedBase<BuildType>(Specifications.authSpec(user), Endpoint.BUILD_TYPES);
        buildTypeRequester.create(buildType);

        var createdBuildType = buildTypeRequester.read(buildType.getId());
        softy.assertEquals(createdBuildType.getName(), buildType.getName(), "Build type name is not correct");
    }

    @Test(description = "(with Facade) User should be able to create build type", groups = {"Positive", "CRUD"})
    public void withFacadeUserCanCreateBuildType() {
        // создаем юзера
        var user = generate(User.class);
        superUserCheckedRequests.getRequest(USERS).create(user);

        // создаем реквестер для созданного юзера
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(user));

        // создаем проект и кладем полученный созданный project в ту же переменную
        var project = generate(Project.class);
        project = userCheckedRequests.<Project>getRequest(PROJECTS).create(project);

        // создаем build type
        var buildType = generate(Arrays.asList(project), BuildType.class);
        userCheckedRequests.getRequest(BUILD_TYPES).create(buildType);

        // получаем созданный build type по id
        var createdBuildType = userCheckedRequests.<BuildType>getRequest(BUILD_TYPES).read(buildType.getId());
        softy.assertEquals(createdBuildType.getName(), buildType.getName(), "Build type name is not correct");
    }

    @Test(description = "User should not be able to create two build types with the same id", groups = {"Negative", "CRUD"})
    public void userCanNotCreateTwoBuildTypesWithTheSameId() {
        step("Create user");
        step("Create project");
        step("Create buildType1");
        step("Create buildType2 with the same id as buildType1");
        step("Check user can not create two build types with the same id (400 error). Check that buildType2 was not created");

    }

    @Test(description = "Project admin should be able to create build type for their project", groups = {"Positive", "Roles"})
    public void projectAdminCanCreateBuildType() {
        step("Create user");
        step("Create project");
        step("Grant user PROJECT_ADMIN role in project");
        step("Create buildType by user (PROJECT_ADMIN)");
        step("Check that buildType was created successfully");
    }

    @Test(description = "Project admin should not be able to create build type for not their project", groups = {"Negative", "Roles"})
    public void projectAdminCanNotCreateBuildTypeForAnotherUser() {
        step("Create user1");
        step("Create project1");
        step("Grant user1 PROJECT_ADMIN role in project1");

        step("Create user2");
        step("Create project2");
        step("Grant user2 PROJECT_ADMIN role in project2");

        step("Create buildType for project1 by user2");
        step("Check that buildType was not created (error 403)");
    }

}
