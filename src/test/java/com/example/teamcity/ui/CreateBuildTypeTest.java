package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.api.spec.ValidationResponseSpecifications;
import com.example.teamcity.ui.pages.BuildTypePage;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.admin.CreateBuildTypePage;
import lombok.val;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.BUILD_TYPES;
import static com.example.teamcity.api.enums.Endpoint.PROJECTS;

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

    @Test(description = "User should not be able to create build type with empty Repo URL", groups = {"Negative", "Regression"})
    public void userCreatesBuildTypeWithoutName() {
        // подготовка окружения
        loginAs(testData.getUser());

        // Create project via API
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        var createdProject = userCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        // Create build type via UI + Проверить сообщение об ошибке на UI
        val errorText = CreateBuildTypePage.open(createdProject.getId())
                .submitBuildTypeUnvalidated()
                .getRepoUrlValidationErrorText();
        softy.assertEquals(errorText, "URL must not be empty");

        // Запрашиваем buildType по projectId и проверяем, что ответ 404 и текст ошибки
        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES)
                .read("project:" + createdProject.getId())
                .then().spec(ValidationResponseSpecifications.checkNoBuildTypeFound(createdProject.getId()));
    }
}
