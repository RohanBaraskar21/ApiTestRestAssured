package com.petstore.security;

import io.restassured.RestAssured;
import com.petstore.utils.ExtentBaseTest;
import com.petstore.utils.ConfigReader;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SecurityTests extends ExtentBaseTest {
    private static final String BASE_URL = ConfigReader.getBaseUrl();

    @Test
    public void testUnauthorizedAccess() {
        Allure.step("GET /user/1 without authorization");
        Response response = RestAssured.given()
            .get(BASE_URL + "/user/1");
        Assert.assertTrue(response.getStatusCode() == 401 || response.getStatusCode() == 403);
    }

    @Test
    public void testSqlInjection() {
        Allure.step("GET /user/login with SQL injection payload");
        String maliciousInput = "' OR '1'='1";
        Response response = RestAssured.given()
            .param("username", maliciousInput)
            .get(BASE_URL + "/user/login");
        Assert.assertTrue(response.getStatusCode() >= 400);
    }

    // ... Add more security tests for XSS, etc. ...
}
