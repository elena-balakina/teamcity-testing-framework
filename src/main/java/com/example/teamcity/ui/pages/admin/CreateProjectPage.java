package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.pages.ProjectsPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class CreateProjectPage extends CreateBasePage {

    private static final String PROJECT_SHOW_MODE = "createProjectMenu";

    private final SelenideElement projectNameInput = $("#projectName");

    public static CreateProjectPage open(String rootId) {
        return Selenide.open(CREATE_URL.formatted(rootId, PROJECT_SHOW_MODE), CreateProjectPage.class);
    }

    public CreateProjectPage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    public ProjectsPage setupProject(String projectName, String buildTypeName) {
        projectNameInput.shouldBe(visible, Duration.ofSeconds(10));
        projectNameInput.val(projectName);
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
        return Selenide.page(ProjectsPage.class);
    }
}
