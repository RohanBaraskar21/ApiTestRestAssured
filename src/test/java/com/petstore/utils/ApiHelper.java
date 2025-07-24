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
        // Use Faker to inject dynamic data
        com.github.javafaker.Faker faker = new com.github.javafaker.Faker();
        org.json.JSONObject json = new org.json.JSONObject(requestBody);

        // Example: Replace fields if present
        if (json.has("name")) {
            json.put("name", faker.animal().name());
        }
        if (json.has("id")) {
            json.put("id", faker.number().numberBetween(10000, 99999));
        }
        if (json.has("username")) {
            json.put("username", faker.name().username());
        }
        // Add more fields as needed for your API

        return RestAssured.given()
            .contentType("application/json")
            .body(json.toString())
            .post(url);
    }

    public static Response put(String url, String jsonFilePath) throws Exception {
        String requestBody = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        // Use Faker to inject dynamic data
        com.github.javafaker.Faker faker = new com.github.javafaker.Faker();
        org.json.JSONObject json = new org.json.JSONObject(requestBody);

        // Example: Replace fields if present
        if (json.has("name")) {
            json.put("name", faker.animal().name());
        }
        if (json.has("id")) {
            json.put("id", faker.number().numberBetween(10000, 99999));
        }
        if (json.has("username")) {
            json.put("username", faker.name().username());
        }
        // Add more fields as needed for your API

        return RestAssured.given()
            .contentType("application/json")
            .body(json.toString())
            .put(url);
    }

    public static Response delete(String url) {
        return RestAssured.given().delete(url);
    }
}
