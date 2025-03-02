package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Selenide;

public class ProjectsPage extends BasePage {

    private static final String PROJECTS_URL = "/favorite/projects?mode=builds";

    public static ProjectsPage open() {
        return Selenide.open(PROJECTS_URL, ProjectsPage.class);
    }
}
