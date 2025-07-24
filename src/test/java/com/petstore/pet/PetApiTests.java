package com.petstore.pet;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import com.petstore.utils.ExtentBaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.petstore.utils.SchemaValidator;
import com.petstore.utils.ApiHelper;
import com.petstore.utils.ConfigReader;

public class PetApiTests extends ExtentBaseTest {
    private static final String DATA_PATH = "src/test/resources/data/";
    private static final String BASE_URL = ConfigReader.getBaseUrl();

    @Test
    public void testAddPetPositive() throws Exception {
        Response response = ApiHelper.post(BASE_URL + "/pet", DATA_PATH + "add-pet-positive.json");
        Assert.assertEquals(response.getStatusCode(), 200);
        SchemaValidator.validate(response, "src/test/resources/schemas/pet.json");
    }

    @Test
    public void testAddPetNegative() throws Exception {
        // Use an empty JSON or missing required fields for negative test
        String url = BASE_URL + "/pet";
        String body = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(DATA_PATH + "add-pet-negative.json")));
        test.info("[REQUEST] POST " + url);
        test.info("[REQUEST BODY] " + body);
        Response response = ApiHelper.post(url, DATA_PATH + "add-pet-negative.json");
        test.info("[RESPONSE] " + response.asString());
        try {
            Assert.assertTrue(response.getStatusCode() >= 400, "Expected error status but got " + response.getStatusCode());
        } catch (AssertionError e) {
            test.fail("Assertion failed: Expected error status but got " + response.getStatusCode());
            throw e;
        }
    }

    @Test
    public void testUpdatePetPositive() throws Exception {
        // Create a pet first
        Response createResponse = ApiHelper.post(BASE_URL + "/pet", DATA_PATH + "add-pet-positive.json");
        Assert.assertEquals(createResponse.getStatusCode(), 200);
        int petId = createResponse.jsonPath().getInt("id");
        // Update the same pet
        org.json.JSONObject updateJson = new org.json.JSONObject(new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(DATA_PATH + "update-pet-positive.json"))));
        updateJson.put("id", petId);
        Response updateResponse = RestAssured.given()
            .contentType("application/json")
            .body(updateJson.toString())
            .put(BASE_URL + "/pet");
        Assert.assertEquals(updateResponse.getStatusCode(), 200);
        SchemaValidator.validate(updateResponse, "src/test/resources/schemas/pet.json");
    }

    @Test
    public void testUpdatePetNegative() throws Exception {
        // Try to update a non-existent pet with invalid data
        org.json.JSONObject updateJson = new org.json.JSONObject();
        updateJson.put("id", -1); // Invalid ID
        String url = BASE_URL + "/pet";
        String body = updateJson.toString();
        test.info("[REQUEST] PUT " + url);
        test.info("[REQUEST BODY] " + body);
        Response response = RestAssured.given()
            .contentType("application/json")
            .body(body)
            .put(url);
        test.info("[RESPONSE] " + response.asString());
        try {
            Assert.assertTrue(response.getStatusCode() >= 400, "Expected error status but got " + response.getStatusCode());
        } catch (AssertionError e) {
            test.fail("Assertion failed: Expected error status but got " + response.getStatusCode());
            throw e;
        }
    }

    @Test
    public void testGetPetByIdPositive() throws Exception {
        // Create a pet first
        Response createResponse = ApiHelper.post(BASE_URL + "/pet", DATA_PATH + "add-pet-positive.json");
        Assert.assertEquals(createResponse.getStatusCode(), 200);
        int petId = createResponse.jsonPath().getInt("id");
        // Wait/retry for persistence
        Response response = ApiHelper.get(BASE_URL + "/pet/" + petId);
        int attempts = 0;
        while (response == null || response.getStatusCode() != 200 && attempts < 2) {
            Thread.sleep(1000);
            response = ApiHelper.get(BASE_URL + "/pet/" + petId);
            attempts++;
        }
        Assert.assertNotNull(response, "Response is null after retries");
        Assert.assertEquals(response.getStatusCode(), 200, "Pet not found after retries");
        // Optionally validate schema
    }

    @Test
    public void testGetPetByIdNegative() {
        int invalidId = -1;
        Response response = ApiHelper.get(BASE_URL + "/pet/" + invalidId);
        Assert.assertTrue(response.getStatusCode() == 404 || response.getStatusCode() == 400, "Expected 404 or 400 but got " + response.getStatusCode());
    }

    @Test
    public void testFindPetsByStatusPositive() {
        Response response = ApiHelper.get(BASE_URL + "/pet/findByStatus?status=available");
        Assert.assertEquals(response.getStatusCode(), 200);
        // Optionally validate schema
    }

    @Test
    public void testFindPetsByStatusNegative() {
        String url = BASE_URL + "/pet/findByStatus?status=invalid";
        test.info("[REQUEST] GET " + url);
        Response response = ApiHelper.get(url);
        test.info("[RESPONSE] " + response.asString());
        try {
            Assert.assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 404, "Expected 400 or 404 but got " + response.getStatusCode());
        } catch (AssertionError e) {
            test.fail("Assertion failed: Expected 400 or 404 but got " + response.getStatusCode());
            throw e;
        }
    }

    @Test
    public void testDeletePetPositive() throws Exception {
        // Create a pet first
        Response createResponse = ApiHelper.post(BASE_URL + "/pet", DATA_PATH + "add-pet-positive.json");
        Assert.assertEquals(createResponse.getStatusCode(), 200);
        int petId = createResponse.jsonPath().getInt("id");
        // Wait/retry for persistence
        Response response = ApiHelper.delete(BASE_URL + "/pet/" + petId);
        int attempts = 0;
        while (response == null || !(response.getStatusCode() == 200 || response.getStatusCode() == 204) && attempts < 2) {
            Thread.sleep(1000);
            response = ApiHelper.delete(BASE_URL + "/pet/" + petId);
            attempts++;
        }
        Assert.assertNotNull(response, "Response is null after retries");
        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 204, "Delete failed after retries");
    }

    @Test
    public void testDeletePetNegative() {
        int invalidId = -1;
        String url = BASE_URL + "/pet/" + invalidId;
        test.info("[REQUEST] DELETE " + url);
        Response response = ApiHelper.delete(url);
        test.info("[RESPONSE] " + response.asString());
        try {
            Assert.assertTrue(response.getStatusCode() == 404 || response.getStatusCode() == 400, "Expected 404 or 400 but got " + response.getStatusCode());
        } catch (AssertionError e) {
            test.fail("Assertion failed: Expected 404 or 400 but got " + response.getStatusCode());
            throw e;
        }
    }
}
