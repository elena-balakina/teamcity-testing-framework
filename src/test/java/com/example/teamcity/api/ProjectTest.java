package com.example.teamcity.api;

import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.api.spec.ValidationResponseSpecifications;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.example.teamcity.api.enums.Endpoint.PROJECTS;
import static com.example.teamcity.api.enums.Endpoint.USERS;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;

@Test(groups = {"Regression"})
public class ProjectTest extends BaseApiTest {

    @Test(description = "Project with correct data can be created by Super user", groups = {"Positive", "CRUD"})
    public void superAdminCanCreateProject() {
        var superuserCheckedRequests = new CheckedRequests(Specifications.superUserSpec());
        var createdProject = superuserCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        Assert.assertEquals(createdProject, testData.getProject(), "Actual project does not match expected");
    }

    @Test(description = "Project with correct data can be created by Admin", groups = {"Positive", "CRUD"})
    public void adminCanCreateProject() {
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        var createdProject = userCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        softy.assertEquals(createdProject.getId(), testData.getProject().getId(), "Actual project id does not match expected");
        softy.assertEquals(createdProject.getName(), testData.getProject().getName(), "Actual project name does not match expected");
    }

    @DataProvider(name = "ValidProjectIds")
    public static Object[] validProjectIds() {
        return new Object[]{"t", "rrrrrrrrr", "gdgdfh56757jhfgjh", "qwertyuiopi".repeat(10)};
    }

    @Test(dataProvider = "ValidProjectIds", description = "Project with valid id can be created by Super user", groups = {"Positive", "CRUD"})
    public void superAdminCanCreateProjectWithValidId(String validId) {
        var superuserCheckedRequests = new CheckedRequests(Specifications.superUserSpec());
        var project = generate(Project.class);
        project.setId(validId);
        var createdProject = superuserCheckedRequests.<Project>getRequest(PROJECTS).create(project);
        Assert.assertEquals(createdProject, project, "Actual project does not match expected");
    }

    @DataProvider(name = "ValidProjectNames")
    public static Object[] validProjectNames() {
        return new Object[]{"йццук",
                "tyuiouyrtytrtyuytrertyuiouyrtytrtyuytrertyuiouyrtytrtyuytrertyuiouyrtytrtyuytrertyuiouyrtytrtyuytrertyuiouyrtytrtyuytrer" +
                        "tyuiouyrtytrtyuytrertyuiouyrtytrtyuytrertyuiouyrtytrtyuytrertyuiouyrtytrtyuytrertyuiouyrtytrtyuytrerrtyuityryrtyrty"};
    }

    @Test(dataProvider = "ValidProjectNames", description = "Project with valid name can be created by Super user", groups = {"Positive", "CRUD"})
    public void superAdminCanCreateProjectWithValidName(String validName) {
        var superuserCheckedRequests = new CheckedRequests(Specifications.superUserSpec());
        var project = generate(Project.class);
        project.setName(validName);
        var createdProject = superuserCheckedRequests.<Project>getRequest(PROJECTS).create(project);
        Assert.assertEquals(createdProject, project, "Actual project does not match expected");
    }

//    @Test(description = "Project with 'copyAllAssociatedSettings' = false can be created by Super user", groups = {"Positive", "CRUD"})
    public void superAdminCanCreateProjectWithCopyAllAssociatedSettingsFalse() {
        var superuserCheckedRequests = new CheckedRequests(Specifications.superUserSpec());
        var project = generate(Project.class);
        project.setCopyAllAssociatedSettings(false);
        var createdProject = superuserCheckedRequests.<Project>getRequest(PROJECTS).create(project);
        Assert.assertEquals(createdProject, project, "Actual project does not match expected");
    }

    @Test(description = "Project with empty name can not be created by Super user", groups = {"Negative", "CRUD"})
    public void superuserCanNotCreateProjectWithEmptyName() {
        var projectWithEmptyName = Project.builder().name("").build();
        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
                .create(projectWithEmptyName)
                .then().spec(ValidationResponseSpecifications.checkProjectWithEmptyName());
    }

    @Test(description = "Project with empty name can not be created by Admin", groups = {"Negative", "CRUD"})
    public void userCanNotCreateProjectWithEmptyName() {
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var projectWithEmptyName = Project.builder().name("").build();
        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
                .create(projectWithEmptyName)
                .then().spec(ValidationResponseSpecifications.checkProjectWithEmptyName());
    }

    @Test(description = "Project with the same id can not be created by Super user", groups = {"Negative", "CRUD"})
    public void superAdminCanNotCreateProjectWithTheSameId() {
        var superuserCheckedRequests = new CheckedRequests(Specifications.superUserSpec());
        superuserCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        var projectWithSameId = generate(Project.class);
        projectWithSameId.setId(testData.getProject().getId());
        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
                .create(projectWithSameId)
                .then().spec(ValidationResponseSpecifications.checkProjectWithIdAlreadyExist(testData.getProject().getId()));
    }

    @Test(description = "Project with the same id can not be created by Admin", groups = {"Negative", "CRUD"})
    public void adminCanNotCreateProjectWithTheSameId() {
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        userCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        var projectWithSameId = generate(Project.class);
        projectWithSameId.setId(testData.getProject().getId());
        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
                .create(projectWithSameId)
                .then().spec(ValidationResponseSpecifications.checkProjectWithIdAlreadyExist(testData.getProject().getId()));
    }

    @Test(description = "Project with the same name can not be created by Super user", groups = {"Negative", "CRUD"})
    public void superAdminCanNotCreateProjectWithTheSameName() {
        var superuserCheckedRequests = new CheckedRequests(Specifications.superUserSpec());
        superuserCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        var projectWithSameName = generate(Project.class);
        projectWithSameName.setName(testData.getProject().getName());
        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
                .create(projectWithSameName)
                .then().spec(ValidationResponseSpecifications.checkProjectWithNameAlreadyExist(testData.getProject().getName()));
    }

    @Test(description = "Project with the same name can not be created by Admin", groups = {"Negative", "CRUD"})
    public void adminCanNotCreateProjectWithTheSameName() {
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        userCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        var projectWithSameName = generate(Project.class);
        projectWithSameName.setName(testData.getProject().getName());
        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
                .create(projectWithSameName)
                .then().spec(ValidationResponseSpecifications.checkProjectWithNameAlreadyExist(testData.getProject().getName()));
    }

    @DataProvider(name = "InvalidProjectIdsStartsWithNonLetter")
    public static Object[] invalidProjectIds() {
        return new Object[]{"1qwert", "~-&51*%#__ 223", "_qwert"};
    }

    @Test(dataProvider = "InvalidProjectIdsStartsWithNonLetter", description = "Project with id starts with non letter can not be created by Super user", groups = {"Negative", "CRUD"})
    public void superuserCanNotCreateProjectWithIdStartsWithNonLetter(String invalidId) {
        var projectWithInvalidId = generate(Project.class);
        projectWithInvalidId.setId(invalidId);
        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
                .create(projectWithInvalidId)
                .then().spec(ValidationResponseSpecifications.checkProjectIdWithNonLetterCharacter(invalidId));
    }

    @Test(description = "Project with non-latin id can not be created by Super user", groups = {"Negative", "CRUD"})
    public void superuserCanNotCreateProjectWithNonLatinId() {
        String projectId = "йцукке";
        var projectWithInvalidId = generate(Project.class);
        projectWithInvalidId.setId(projectId);
        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
                .create(projectWithInvalidId)
                .then().spec(ValidationResponseSpecifications.checkProjectIdWithNonLatinCharacter(projectId));
    }

    @Test(description = "Project with id > 225 symbols can not be created by Super user", groups = {"Negative", "CRUD"})
    public void superuserCanNotCreateProjectIdMoreThan225Symbols() {
        String projectId = "qwertyuiopp".repeat(25);
        var projectWithInvalidId = generate(Project.class);
        projectWithInvalidId.setId(projectId);
        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
                .create(projectWithInvalidId)
                .then().spec(ValidationResponseSpecifications.checkProjectIdTooLong(projectId));
    }

    @Test(description = "Project with empty id and name can not be created by Super user", groups = {"Negative", "CRUD"})
    public void superuserCanNotCreateProjectWithEmptyIdAndName() {
        var projectWithEmptyIdAndName = generate(Project.class);
        projectWithEmptyIdAndName.setId("");
        projectWithEmptyIdAndName.setName("");
        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
                .create(projectWithEmptyIdAndName)
                .then().spec(ValidationResponseSpecifications.checkProjectWithEmptyName());
    }

    @Test(description = "Project with invalid id and empty name can not be created by Super user", groups = {"Negative", "CRUD"})
    public void superuserCanNotCreateProjectWithXInvalidIdAndEmptyName() {
        var projectWithInvalidIdAndEmptyName = generate(Project.class);
        projectWithInvalidIdAndEmptyName.setId("йцукке");
        projectWithInvalidIdAndEmptyName.setName("");
        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
                .create(projectWithInvalidIdAndEmptyName)
                .then().spec(ValidationResponseSpecifications.checkProjectWithEmptyName());
    }

    @Test(description = "User should not be able to create a copy of non existing project", groups = {"Negative", "Copy"})
    public void userCanNotCopyProject() {
        var superuserCheckedRequests = new CheckedRequests(Specifications.superUserSpec());
        superuserCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
                .create(testData.getProject())
                .then().spec(ValidationResponseSpecifications.checkProjectWithNameAlreadyExist(testData.getProject().getName()));
    }

    @Test(description = "User should not be able to create a copy with empty info about source project", groups = {"Negative", "Copy"})
    public void userCanCreateProjectWithEmtpySourceProject() {
        var projectWithEmtpySourceProject = generate(Project.class);
        var locator = Locator.builder().locator(" ").build();
        projectWithEmtpySourceProject.setParentProject(locator);
        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
                .create(projectWithEmtpySourceProject)
                .then().spec(ValidationResponseSpecifications.checkNoSourceProjectFound(projectWithEmtpySourceProject.getParentProject().getLocator()));
    }

    @DataProvider(name = "Roles")
    public static Object[] roles() {
        return new Object[]{"PROJECT_VIEWER", "PROJECT_DEVELOPER", "AGENT_MANAGER"};
    }

    @Test(dataProvider = "Roles", description = "Project can not be created by user with certain role", groups = {"Roles", "Negative", "CRUD"})
    public void userCanNotCreateProject(String userRole) {
        var user = generate(User.class);
        var role = Role.builder().roleId(userRole).scope("g").build();
        var roles = Roles.builder().role(Arrays.asList(role)).build();
        user.setRoles(roles);
        superUserCheckedRequests.getRequest(USERS).create(user);
        new UncheckedBase(Specifications.authSpec(user), PROJECTS)
                .create(testData.getProject())
                .then().spec(ValidationResponseSpecifications.checkUserCanNotCreateProject());
    }
}
