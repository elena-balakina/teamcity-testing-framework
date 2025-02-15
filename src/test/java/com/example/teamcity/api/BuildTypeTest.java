package com.example.teamcity.api;

import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.*;
import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class BuildTypeTest extends BaseApiTest {

    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCanCreateBuildType() {
        // тестовые данные генерируются в BaseTest

        // создаем юзера и реквестер для созданного юзера
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        // создаем проект
        userCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        // создаем build type
        userCheckedRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        // получаем созданный build type по id
        var createdBuildType = userCheckedRequests.<BuildType>getRequest(BUILD_TYPES).read(testData.getBuildType().getId());
        softy.assertEquals(createdBuildType.getName(), testData.getBuildType().getName(), "Build type name is not correct");
    }

    @Test(description = "User should not be able to create two build types with the same id", groups = {"Negative", "CRUD"})
    public void userCanNotCreateTwoBuildTypesWithTheSameId() {
        // тестовые данные генерируются в BaseTest
        // собираем buildTypeWithSameId с id из сгенерированного в начале теста buildType1
        var buildTypeWithSameId = BuildType.builder()
                .id(testData.getBuildType().getId())
                .name(RandomData.getString())
                .project(testData.getProject())
                .build();

        // создаем юзера и реквестер для созданного юзера
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        // создаем проект
        userCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        // создаем buildType1
        userCheckedRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        // пытаемся создать buildTypeWithSameId
        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES)
                .create(buildTypeWithSameId)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("The build configuration / template ID \"%s\" is already used by another configuration or template".formatted(testData.getBuildType().getId())));
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
