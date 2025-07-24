package com.petstore.store;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import com.petstore.utils.ExtentBaseTest;
import io.qameta.allure.Allure;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.petstore.utils.SchemaValidator;
import com.petstore.utils.ApiHelper;
import com.petstore.utils.ConfigReader;

public class StoreApiTests extends ExtentBaseTest {
    private static final String DATA_PATH = "src/test/resources/data/";
    private static final String BASE_URL = ConfigReader.getBaseUrl();

    @Test
    public void testPlaceOrderPositive() throws Exception {
        org.json.JSONObject order = new org.json.JSONObject();
        int orderId = (int) (System.currentTimeMillis() % 100000);
        order.put("id", orderId);
        order.put("petId", 10000);
        order.put("quantity", 1);
        order.put("shipDate", java.time.Instant.now().toString());
        order.put("status", "placed");
        order.put("complete", true);
        Response response = RestAssured.given()
            .contentType("application/json")
            .body(order.toString())
            .post(BASE_URL + "/store/order");
        Assert.assertEquals(response.getStatusCode(), 200);
        SchemaValidator.validate(response, "src/test/resources/schemas/order.json");
    }

    @Test
    public void testPlaceOrderNegative() throws Exception {
        org.json.JSONObject invalidOrder = new org.json.JSONObject();
        String url = BASE_URL + "/store/order";
        String body = invalidOrder.toString();
        System.out.println("[DEBUG] POST " + url);
        System.out.println("[DEBUG] Body: " + body);
        Response response = RestAssured.given()
            .contentType("application/json")
            .body(body)
            .post(url);
        System.out.println("[DEBUG] Response: " + response.asString());
        Assert.assertTrue(response.getStatusCode() >= 400, "Expected error status but got " + response.getStatusCode());
    }

    @Test
    public void testGetOrderByIdPositive() throws Exception {
        // Create an order first
        org.json.JSONObject order = new org.json.JSONObject();
        int orderId = (int) (System.currentTimeMillis() % 100000);
        order.put("id", orderId);
        order.put("petId", 10000);
        order.put("quantity", 1);
        order.put("shipDate", java.time.Instant.now().toString());
        order.put("status", "placed");
        order.put("complete", true);
        Response createResponse = RestAssured.given()
            .contentType("application/json")
            .body(order.toString())
            .post(BASE_URL + "/store/order");
        Assert.assertEquals(createResponse.getStatusCode(), 200);
        // Get the same order
        Response response = ApiHelper.get(BASE_URL + "/store/order/" + orderId);
        Assert.assertEquals(response.getStatusCode(), 200);
        // Optionally validate schema
    }

    @Test
    public void testGetOrderByIdNegative() {
        int invalidId = -1;
        String url = BASE_URL + "/store/order/" + invalidId;
        System.out.println("[DEBUG] GET " + url);
        Response response = ApiHelper.get(url);
        System.out.println("[DEBUG] Response: " + response.asString());
        Assert.assertTrue(response.getStatusCode() == 404 || response.getStatusCode() == 400, "Expected 404 or 400 but got " + response.getStatusCode());
    }

    @Test
    public void testDeleteOrderPositive() throws Exception {
        // Create an order first
        org.json.JSONObject order = new org.json.JSONObject();
        int orderId = (int) (System.currentTimeMillis() % 100000);
        order.put("id", orderId);
        order.put("petId", 10000);
        order.put("quantity", 1);
        order.put("shipDate", java.time.Instant.now().toString());
        order.put("status", "placed");
        order.put("complete", true);
        Response createResponse = RestAssured.given()
            .contentType("application/json")
            .body(order.toString())
            .post(BASE_URL + "/store/order");
        Assert.assertEquals(createResponse.getStatusCode(), 200);
        // Delete the same order
        Response response = ApiHelper.delete(BASE_URL + "/store/order/" + orderId);
        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 204);
    }

    @Test
    public void testDeleteOrderNegative() {
        int invalidId = -1;
        String url = BASE_URL + "/store/order/" + invalidId;
        System.out.println("[DEBUG] DELETE " + url);
        Response response = ApiHelper.delete(url);
        System.out.println("[DEBUG] Response: " + response.asString());
        Assert.assertTrue(response.getStatusCode() == 404 || response.getStatusCode() == 400, "Expected 404 or 400 but got " + response.getStatusCode());
    }
}
