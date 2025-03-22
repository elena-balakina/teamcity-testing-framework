package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class CreateProjectPage extends CreateBasePage {

    private static final String PROJECT_SHOW_MODE = "createProjectMenu";

    private final SelenideElement projectNameInput = $("#projectName");
    private final SelenideElement autoDetectedSubtitle = $(By.xpath("//h2[@class='noBorder' and text()='Auto-detected Build Steps']"));

    public static CreateProjectPage open(String rootId) {
        return Selenide.open(CREATE_URL.formatted(rootId, PROJECT_SHOW_MODE), CreateProjectPage.class);
    }

    public CreateProjectPage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    // Stabilize UI test
    public void setupProject(String projectName, String buildTypeName) {
        projectNameInput.val(projectName);
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
        autoDetectedSubtitle.should(Condition.appear, BASE_WAITING);
    }
}
