package com.example.teamcity.api;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.checked.CheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.Test;

import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class BuildTypeTest extends BaseApiTest {

    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCanCreateBuildType() {
        var user = generate(User.class);

        step("Create user", () -> {
            var requester = new CheckedBase<User>(Specifications.superUserAuth(), Endpoint.USERS);
            requester.create(user);
        });

        step("Create project");
        step("Create build type");
        step("Check build type was created successfully with currect data");

//        var token = RestAssured
//                .given()
//                .spec(Specifications.authSpec(user))
//                .get("authenticationTest.html?cscf")
//                .then()
//                .extract().asString();
//        System.out.printf(token);
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
