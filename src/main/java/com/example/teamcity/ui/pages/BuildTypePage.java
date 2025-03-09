package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class BuildTypePage extends BasePage {

    private static final String BUILD_TYPE_URL = "/buildConfiguration/%s?mode=builds";

    public static SelenideElement title = $("h1[class*='BuildTypePageHeader']>span");

    public static BuildTypePage open(String projectId) {
        return Selenide.open(BUILD_TYPE_URL.formatted(projectId), BuildTypePage.class);
    }
}
