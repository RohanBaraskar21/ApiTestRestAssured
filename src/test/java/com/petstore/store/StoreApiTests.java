package com.petstore.store;

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
        Response response = ApiHelper.post(BASE_URL + "/store/order", DATA_PATH + "place-order-positive.json");
        Assert.assertEquals(response.getStatusCode(), 200);
        SchemaValidator.validate(response, "src/test/resources/schemas/order.json");
    }

    @Test
    public void testPlaceOrderNegative() throws Exception {
        Response response = ApiHelper.post(BASE_URL + "/store/order", DATA_PATH + "place-order-negative.json");
        Assert.assertTrue(response.getStatusCode() >= 400);
    }

    @Test
    public void testGetOrderByIdPositive() {
        Allure.step("GET /store/order/{orderId} with valid id");
        int orderId = 2001;
        Response response = ApiHelper.get(BASE_URL + "/store/order/" + orderId);
        Assert.assertEquals(response.getStatusCode(), 200);
        // Optionally validate schema
    }

    @Test
    public void testGetOrderByIdNegative() {
        int invalidId = -1;
        Response response = ApiHelper.get(BASE_URL + "/store/order/" + invalidId);
        Assert.assertTrue(response.getStatusCode() >= 400);
    }

    @Test
    public void testDeleteOrderPositive() {
        int orderId = 2001;
        Response response = ApiHelper.delete(BASE_URL + "/store/order/" + orderId);
        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 204);
    }

    @Test
    public void testDeleteOrderNegative() {
        int invalidId = -1;
        Response response = ApiHelper.delete(BASE_URL + "/store/order/" + invalidId);
        Assert.assertTrue(response.getStatusCode() >= 400);
    }
}
