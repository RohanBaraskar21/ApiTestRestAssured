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
        // Try to access user info without authorization (simulate by using invalid token/header if API supports)
        String url = BASE_URL + "/user/1";
        System.out.println("[DEBUG] GET " + url);
        Response response = RestAssured.given()
            .header("Authorization", "Bearer invalidtoken")
            .get(url);
        System.out.println("[DEBUG] Response: " + response.asString());
        Assert.assertTrue(response.getStatusCode() == 401 || response.getStatusCode() == 403 || response.getStatusCode() == 400, "Expected 401/403/400 but got " + response.getStatusCode());
    }

    @Test
    public void testSqlInjection() {
        String maliciousInput = "' OR '1'='1";
        String url = BASE_URL + "/user/login?username=" + maliciousInput + "&password=" + maliciousInput;
        System.out.println("[DEBUG] GET " + url);
        Response response = RestAssured.given()
            .param("username", maliciousInput)
            .param("password", maliciousInput)
            .get(BASE_URL + "/user/login");
        System.out.println("[DEBUG] Response: " + response.asString());
        Assert.assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 401 || response.getStatusCode() == 403, "Expected error status but got " + response.getStatusCode());
    }

    // ... Add more security tests for XSS, etc. ...
}
