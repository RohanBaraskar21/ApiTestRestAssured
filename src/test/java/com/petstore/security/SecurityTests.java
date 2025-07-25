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
        test.info("[REQUEST] GET " + url);
        Response response = RestAssured.given()
            .header("Authorization", "Bearer invalidtoken")
            .get(url);
        test.info("[RESPONSE] " + response.asString());
        try {
            Assert.assertTrue(response.getStatusCode() == 401 || response.getStatusCode() == 403 || response.getStatusCode() == 400, "Expected 401/403/400 but got " + response.getStatusCode());
        } catch (AssertionError e) {
            test.fail("Assertion failed: Expected 401/403/400 but got " + response.getStatusCode());
            throw e;
        }
    }

    @Test
    public void testSqlInjection() {
        String maliciousInput = "' OR '1'='1";
        String url = BASE_URL + "/user/login?username=" + maliciousInput + "&password=" + maliciousInput;
        test.info("[REQUEST] GET " + url);
        Response response = RestAssured.given()
            .param("username", maliciousInput)
            .param("password", maliciousInput)
            .get(BASE_URL + "/user/login");
        test.info("[RESPONSE] " + response.asString());
        try {
            Assert.assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 401 || response.getStatusCode() == 403, "Expected error status but got " + response.getStatusCode());
        } catch (AssertionError e) {
            test.fail("Assertion failed: Expected error status but got " + response.getStatusCode());
            throw e;
        }
    }

    // ... Add more security tests for XSS, etc. ...
}
