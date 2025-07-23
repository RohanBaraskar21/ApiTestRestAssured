package com.petstore.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ApiHelper {
    public static Response get(String url) {
        return RestAssured.given().get(url);
    }

    public static Response post(String url, String jsonFilePath) throws Exception {
        String requestBody = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        return RestAssured.given()
            .contentType("application/json")
            .body(requestBody)
            .post(url);
    }

    public static Response put(String url, String jsonFilePath) throws Exception {
        String requestBody = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        return RestAssured.given()
            .contentType("application/json")
            .body(requestBody)
            .put(url);
    }

    public static Response delete(String url) {
        return RestAssured.given().delete(url);
    }
}
