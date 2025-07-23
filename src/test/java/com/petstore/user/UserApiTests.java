package com.petstore.user;

import io.restassured.response.Response;
import com.petstore.utils.ExtentBaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.petstore.utils.SchemaValidator;
import com.petstore.utils.ApiHelper;
import com.petstore.utils.ConfigReader;

public class UserApiTests extends ExtentBaseTest {
    private static final String DATA_PATH = "src/test/resources/data/";
    private static final String BASE_URL = ConfigReader.getBaseUrl();

    @Test
    public void testCreateUserPositive() throws Exception {
        Response response = ApiHelper.post(BASE_URL + "/user", DATA_PATH + "create-user-positive.json");
        Assert.assertEquals(response.getStatusCode(), 200);
        SchemaValidator.validate(response, "src/test/resources/schemas/user.json");
    }

    @Test
    public void testCreateUserNegative() throws Exception {
        Response response = ApiHelper.post(BASE_URL + "/user", DATA_PATH + "create-user-negative.json");
        Assert.assertTrue(response.getStatusCode() >= 400);
    }

    @Test
    public void testCreateUsersWithArrayPositive() throws Exception {
        Response response = ApiHelper.post(BASE_URL + "/user/createWithArray", DATA_PATH + "create-users-array-positive.json");
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testCreateUsersWithArrayNegative() throws Exception {
        Response response = ApiHelper.post(BASE_URL + "/user/createWithArray", DATA_PATH + "create-users-array-negative.json");
        Assert.assertTrue(response.getStatusCode() >= 400);
    }

    @Test
    public void testCreateUsersWithListPositive() throws Exception {
        Response response = ApiHelper.post(BASE_URL + "/user/createWithList", DATA_PATH + "create-users-list-positive.json");
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testCreateUsersWithListNegative() throws Exception {
        Response response = ApiHelper.post(BASE_URL + "/user/createWithList", DATA_PATH + "create-users-list-negative.json");
        Assert.assertTrue(response.getStatusCode() >= 400);
    }

    @Test
    public void testGetUserByUsernamePositive() {
        String username = "johndoe";
        Response response = ApiHelper.get(BASE_URL + "/user/" + username);
        Assert.assertEquals(response.getStatusCode(), 200);
        // Optionally validate schema
    }

    @Test
    public void testGetUserByUsernameNegative() {
        String username = "invaliduser";
        Response response = ApiHelper.get(BASE_URL + "/user/" + username);
        Assert.assertTrue(response.getStatusCode() >= 400);
    }

    @Test
    public void testUpdateUserPositive() throws Exception {
        String username = "johndoe";
        Response response = ApiHelper.put(BASE_URL + "/user/" + username, DATA_PATH + "update-user-positive.json");
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testUpdateUserNegative() throws Exception {
        String username = "invaliduser";
        Response response = ApiHelper.put(BASE_URL + "/user/" + username, DATA_PATH + "update-user-negative.json");
        Assert.assertTrue(response.getStatusCode() >= 400);
    }

    @Test
    public void testLoginUserPositive() {
        Response response = ApiHelper.get(BASE_URL + "/user/login?username=johndoe&password=Password123!");
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testLoginUserNegative() {
        Response response = ApiHelper.get(BASE_URL + "/user/login?username=invaliduser&password=wrongpassword");
        Assert.assertTrue(response.getStatusCode() >= 400);
    }

    @Test
    public void testLogoutUserPositive() {
        Response response = ApiHelper.get(BASE_URL + "/user/logout");
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testDeleteUserPositive() {
        String username = "johndoe";
        Response response = ApiHelper.delete(BASE_URL + "/user/" + username);
        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 204);
    }

    @Test
    public void testDeleteUserNegative() {
        String username = "invaliduser";
        Response response = ApiHelper.delete(BASE_URL + "/user/" + username);
        Assert.assertTrue(response.getStatusCode() >= 400);
    }
}
