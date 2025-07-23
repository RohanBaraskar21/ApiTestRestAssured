package com.petstore.utils;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import java.nio.file.Paths;

public class SchemaValidator {
    public static void validate(Response response, String schemaPath) {
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(Paths.get(schemaPath).toFile()));
    }
}
