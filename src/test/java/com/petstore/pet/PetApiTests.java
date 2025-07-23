package com.petstore.pet;

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
        Response response = ApiHelper.post(BASE_URL + "/pet", DATA_PATH + "add-pet-negative.json");
        Assert.assertTrue(response.getStatusCode() >= 400);
    }

    @Test
    public void testUpdatePetPositive() throws Exception {
        Response response = ApiHelper.put(BASE_URL + "/pet", DATA_PATH + "update-pet-positive.json");
        Assert.assertEquals(response.getStatusCode(), 200);
        SchemaValidator.validate(response, "src/test/resources/schemas/pet.json");
    }

    @Test
    public void testUpdatePetNegative() throws Exception {
        Response response = ApiHelper.put(BASE_URL + "/pet", DATA_PATH + "update-pet-negative.json");
        Assert.assertTrue(response.getStatusCode() >= 400);
    }

    @Test
    public void testGetPetByIdPositive() {
        int petId = 1001; // Use unique test data per test
        Response response = ApiHelper.get(BASE_URL + "/pet/" + petId);
        Assert.assertEquals(response.getStatusCode(), 200);
        // Optionally validate schema
    }

    @Test
    public void testGetPetByIdNegative() {
        int invalidId = -1;
        Response response = ApiHelper.get(BASE_URL + "/pet/" + invalidId);
        Assert.assertTrue(response.getStatusCode() >= 400);
    }

    @Test
    public void testFindPetsByStatusPositive() {
        Response response = ApiHelper.get(BASE_URL + "/pet/findByStatus?status=available");
        Assert.assertEquals(response.getStatusCode(), 200);
        // Optionally validate schema
    }

    @Test
    public void testFindPetsByStatusNegative() {
        Response response = ApiHelper.get(BASE_URL + "/pet/findByStatus?status=invalid");
        Assert.assertTrue(response.getStatusCode() >= 400);
    }

    @Test
    public void testDeletePetPositive() {
        int petId = 1001;
        Response response = ApiHelper.delete(BASE_URL + "/pet/" + petId);
        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 204);
    }

    @Test
    public void testDeletePetNegative() {
        int invalidId = -1;
        Response response = ApiHelper.delete(BASE_URL + "/pet/" + invalidId);
        Assert.assertTrue(response.getStatusCode() >= 400);
    }
}
