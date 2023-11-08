package com.censhare;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.equalTo;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        Response schemas = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept("*/*")
                .when()
                .get("https://www.googleapis.com/books/v1/volumes?q=isbn:0-553-10354-7");

        LOGGER.info(schemas.getBody().asString());

        schemas.then()
                .statusCode(200).and()
                .body("items[0].volumeInfo.title", equalTo("A Game of Thrones"));
    }
}
