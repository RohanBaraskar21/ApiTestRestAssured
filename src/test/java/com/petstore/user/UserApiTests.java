package com.petstore.user;

import io.restassured.RestAssured;
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
        org.json.JSONObject invalidJson = new org.json.JSONObject();
        String url = BASE_URL + "/user";
        String body = invalidJson.toString();
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
    public void testCreateUsersWithArrayPositive() throws Exception {
        // Create array of users with dynamic usernames
        org.json.JSONArray users = new org.json.JSONArray();
        for (int i = 0; i < 2; i++) {
            org.json.JSONObject user = new org.json.JSONObject();
            user.put("id", 10000 + i);
            user.put("username", "user_array_" + System.currentTimeMillis() + i);
            user.put("firstName", "First" + i);
            user.put("lastName", "Last" + i);
            user.put("email", "user_array_" + i + "@test.com");
            user.put("password", "pass123");
            user.put("phone", "1234567890");
            user.put("userStatus", 1);
            users.put(user);
        }
        Response response = RestAssured.given()
            .contentType("application/json")
            .body(users.toString())
            .post(BASE_URL + "/user/createWithArray");
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testCreateUsersWithArrayNegative() throws Exception {
        org.json.JSONArray invalidUsers = new org.json.JSONArray();
        invalidUsers.put(new org.json.JSONObject()); // Empty user
        String url = BASE_URL + "/user/createWithArray";
        String body = invalidUsers.toString();
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
    public void testCreateUsersWithListPositive() throws Exception {
        // Create list of users with dynamic usernames
        org.json.JSONArray users = new org.json.JSONArray();
        for (int i = 0; i < 2; i++) {
            org.json.JSONObject user = new org.json.JSONObject();
            user.put("id", 20000 + i);
            user.put("username", "user_list_" + System.currentTimeMillis() + i);
            user.put("firstName", "First" + i);
            user.put("lastName", "Last" + i);
            user.put("email", "user_list_" + i + "@test.com");
            user.put("password", "pass123");
            user.put("phone", "1234567890");
            user.put("userStatus", 1);
            users.put(user);
        }
        Response response = RestAssured.given()
            .contentType("application/json")
            .body(users.toString())
            .post(BASE_URL + "/user/createWithList");
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testCreateUsersWithListNegative() throws Exception {
        org.json.JSONArray invalidUsers = new org.json.JSONArray();
        invalidUsers.put(new org.json.JSONObject()); // Empty user
        String url = BASE_URL + "/user/createWithList";
        String body = invalidUsers.toString();
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
    public void testGetUserByUsernamePositive() throws Exception {
        // Create a user first
        org.json.JSONObject user = new org.json.JSONObject();
        String username = "user_get_" + System.currentTimeMillis();
        user.put("id", 30000);
        user.put("username", username);
        user.put("firstName", "First");
        user.put("lastName", "Last");
        user.put("email", "getuser@test.com");
        user.put("password", "pass123");
        user.put("phone", "1234567890");
        user.put("userStatus", 1);
        Response createResponse = RestAssured.given()
            .contentType("application/json")
            .body(user.toString())
            .post(BASE_URL + "/user");
        Assert.assertEquals(createResponse.getStatusCode(), 200);
        // Get the same user
        Response response = ApiHelper.get(BASE_URL + "/user/" + username);
        Assert.assertEquals(response.getStatusCode(), 200);
        // Optionally validate schema
    }

    @Test
    public void testGetUserByUsernameNegative() {
        String username = "invaliduser";
        String url = BASE_URL + "/user/" + username;
        System.out.println("[DEBUG] GET " + url);
        Response response = ApiHelper.get(url);
        System.out.println("[DEBUG] Response: " + response.asString());
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
        String url = BASE_URL + "/user/" + username;
        System.out.println("[DEBUG] PUT " + url);
        Response response = ApiHelper.put(url, DATA_PATH + "update-user-negative.json");
        System.out.println("[DEBUG] Response: " + response.asString());
        Assert.assertTrue(response.getStatusCode() >= 400);
    }

    @Test
    public void testLoginUserPositive() {
        Response response = ApiHelper.get(BASE_URL + "/user/login?username=johndoe&password=Password123!");
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testLoginUserNegative() {
        String url = BASE_URL + "/user/login?username=invaliduser&password=wrongpassword";
        System.out.println("[DEBUG] GET " + url);
        Response response = ApiHelper.get(url);
        System.out.println("[DEBUG] Response: " + response.asString());
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
        String url = BASE_URL + "/user/" + username;
        System.out.println("[DEBUG] DELETE " + url);
        Response response = ApiHelper.delete(url);
        System.out.println("[DEBUG] Response: " + response.asString());
        Assert.assertTrue(response.getStatusCode() >= 400);
    }
}
