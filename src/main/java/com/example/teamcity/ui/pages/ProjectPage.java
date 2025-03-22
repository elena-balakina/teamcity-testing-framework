package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.elements.BuildTypeElement;
import io.qameta.allure.Step;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProjectPage extends BasePage {

    private static final String PROJECT_URL = "/project/%s?mode=builds";

    public static SelenideElement title = $("span[class*='ProjectPageHeader']");

    private ElementsCollection buildTypeElements = $$("div[class*='BuildTypes__item']");

    private SelenideElement header = $(".MainPanel__router--gF > div");

    public ProjectPage() {
        header.shouldBe(Condition.visible, BASE_WAITING);
    }

    @Step("Open Project page")
    public static ProjectPage open(String projectId) {
        return Selenide.open(PROJECT_URL.formatted(projectId), ProjectPage.class);
    }

    public List<BuildTypeElement> getBuildTypes() {
        return generatePageElements(buildTypeElements, BuildTypeElement::new);
    }
}
