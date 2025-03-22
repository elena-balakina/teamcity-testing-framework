package com.example.teamcity.api;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.mock.FakeObject;
import com.example.teamcity.api.mock.WireMock;
import com.example.teamcity.api.requests.checked.CheckedBase;
import com.example.teamcity.api.spec.Specifications;
import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.post;

@Feature("Start build")
public class StartBuildTest extends BaseApiTest {

    @BeforeMethod
    public void setupWireMockServer() {
        var fakeObject = FakeObject.builder().build();
        WireMock.setupServer(post("/app/rest/fake"), HttpStatus.SC_OK, fakeObject);
    }

    @Test(description = "User should be able to start build (with WireMock)", groups = {"Mock"})
    public void userStartsBuildWithWireMockTest() {
        var checkedBuildQueueRequest = new CheckedBase<FakeObject>(Specifications.mockSpec(), Endpoint.FAKE_OBJECT);
        var fake = checkedBuildQueueRequest.create(FakeObject.builder().build());

        softy.assertEquals(fake.getId(), "fake-id");
        softy.assertEquals(fake.getName(), "fake-name");
    }


    @AfterMethod(alwaysRun = true)
    public void stopWireMockServer() {
        WireMock.stopServer();
    }

}
