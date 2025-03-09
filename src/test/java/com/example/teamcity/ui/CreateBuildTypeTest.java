package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.BuildTypePage;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.admin.CreateBuildTypePage;
import lombok.val;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.PROJECTS;
import static io.qameta.allure.Allure.step;

public class CreateBuildTypeTest extends BaseUiTest {

    private static final String REPO_URL = "https://github.com/elena-balakina/teamcity-testing-framework";

    @Test(description = "User should be able to create build type", groups = {"Positive", "Regression"})
    public void userCreatesBuildType() {
        // подготовка окружения
        loginAs(testData.getUser());

        // Create project via API
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        var createdProject = userCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        // Create build type via UI
        CreateBuildTypePage.open(createdProject.getId())
                        .createForm(REPO_URL)
                        .submitBuildType();

        // проверка состояния API (корректность отправки данных с UI на API)
        // Получаем buildType по projectId
        val createdBuildType = userCheckedRequests.<BuildType>getRequest(Endpoint.BUILD_TYPES).read("project:" + createdProject.getId());
        softy.assertNotNull(createdBuildType);

        // Получаем buildType по buildTypeId ({projectId}_Build)
        val buildTypeId = createdProject.getId() + "_Build";
        val createdBuildTypeById = userCheckedRequests.<BuildType>getRequest(Endpoint.BUILD_TYPES).read("id:" + buildTypeId);
        softy.assertNotNull(createdBuildType);

        // Проверяем, что они одинаковые (buildType связан с projectId)
        softy.assertEquals(createdBuildType, createdBuildTypeById);

        // проверка состояния UI
        // (корректность считывания данных и отображение данных на UI)
        BuildTypePage.open(buildTypeId)
                .title.shouldHave(Condition.exactText(createdBuildType.getName()));

        // проверяем, что созданный buildType существует в списке buildType'ов проекта
        val buildTypeExists = ProjectPage.open(createdProject.getId())
                .getBuildTypes()
                .stream()
                .anyMatch(buildType -> buildType.getName().text().equals(createdBuildType.getName()));
        softy.assertTrue(buildTypeExists);
    }

    @Test(description = "User should not be able to create build type without name", groups = {"Negative", "Regression"})
    public void userCreatesBuildTypeWithoutName() {
        // подготовка окружения
        step("Login as user");
        step("Check number of projects");

        // взаимодействие с UI
        step("Open `Create Project Page` (http://localhost:8111/admin/createObjectMenu.html)");
        step("Send all project parameters (repository URL)");
        step("Click `Proceed`");
        step("Set Project Name value is empty");
        step("Click `Proceed`");

        // проверка состояния API
        // (корректность отправки данных с UI на API)
        step("Check that number of projects did not change");

        // проверка состояния UI
        // (корректность считывания данных и отображение данных на UI)
        step("Check that error appears `Project name must not be empty`");
    }
}
