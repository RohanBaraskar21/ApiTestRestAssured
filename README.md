# Petstore API Automation Testing

## Overview
This project is a comprehensive Java Maven automation suite for the Petstore API (https://petstore.swagger.io/v2/swagger.json) using Rest Assured and TestNG. It covers positive and negative test cases, schema validation, security testing, parallel execution, and browser-based operations via Playwright MCP Server.

## Features
- Positive and negative test cases for all API endpoints
- JSON Schema validation for all responses
- Security tests (auth, input validation, SQLi, XSS)
- Parallel test execution (TestNG + Maven Surefire)
- Browser-based operations using Playwright MCP Server

## Project Structure
- `src/main/java/com/petstore/utils/SchemaValidator.java`: JSON schema validation utility
- `src/test/java/com/petstore/pet/PetApiTests.java`: Pet API tests
- `src/test/java/com/petstore/store/StoreApiTests.java`: Store API tests
- `src/test/java/com/petstore/user/UserApiTests.java`: User API tests
- `src/test/java/com/petstore/security/SecurityTests.java`: Security tests
- `src/test/resources/schemas/`: JSON schemas for validation

## Setup Instructions
1. Install JDK 8+
2. Install Maven
3. Clone this repository
4. Run `mvn clean test` to execute all tests

## Parallel Test Execution
- Configured via Maven Surefire plugin in `pom.xml`
- You can adjust thread count and parallelism in the plugin configuration

## Playwright MCP Server
- For browser-based scenarios, set up Playwright MCP Server as per [official docs](https://modelcontextprotocol.io/llms-full.txt)
- Documented usage and setup in this README

## Adding Schemas
- Place JSON schema files in `src/test/resources/schemas/`
- Reference them in your test classes for validation

## Extending Tests
- Add new test classes in the respective package
- Use `SchemaValidator.validate()` for response validation

## Contact
For questions, open an issue or contact the maintainer.
