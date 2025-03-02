package com.example.teamcity.ui;

import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;

public class CreateProjectTest extends  BaseUiTest {

    @Test (description = "User should be able to create project", groups = {"Positive", "Regression"})
    public void userCreatesProject() {
        // подготовка окружения
        step("Login as user");
//        superUserCheckRequests.getRequest(Endpoint.USERS).create(testData.getUser());
//        LoginPage.open().login(testData.getUser());

        // взаимодействие с UI
        step("Open URL 'Create Project Page': admin/createObjectMenu.html");
        step("Send all project parameters (repository Url) - https://github.com/AlexPshe/spring-core-for-qa");
        step("Click 'Proceed'");
        step("Fix Project name and Build type values'");
        step("Click 'Proceed'");

        // проверка состояния API
        // (корректность отправки данных с UI на API)
        step("Check that all entities (project, build type) was successfully created with correct data on API level");

        // проверка состояния UI
        // (корректность считывания данных и отображение данных на UI)
        step("Check that project is visible on Projects Page (http://localhost:8111/favorite/projects)");
    }

    @Test(description = "User should not be able to create project without name", groups = {"Negative", "Regression"})
    public void userCreatesProjectWithoutName() {
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
