package com.example.teamcity.api;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.checked.CheckedBase;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.Test;

import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class ProjectTest extends BaseApiTest {

    @Test(description = "Project with empty name can not be created", groups = {"Negative", "CRUD"})
    public void userCanNotCreateProjectWithEmptyName() {
        var user = generate(User.class);

        step("Create user", () -> {
            var requester = new CheckedBase<User>(Specifications.superUserSpec(), Endpoint.USERS);
            requester.create(user);
        });

        var project = Project.builder().name("").build();

        step("Create project by user", () -> {
            var requester = new UncheckedBase(Specifications.authSpec(user), Endpoint.PROJECTS);
            var response = requester.create(project);

            softy.assertEquals(response.statusCode(), 400, "Expected code is 400, but actual is " + response.statusCode());
            softy.assertTrue(response.getBody().asString().contains("Project name cannot be empty."), "Details do not contain 'Project name cannot be empty.'");
        });
    }
}
