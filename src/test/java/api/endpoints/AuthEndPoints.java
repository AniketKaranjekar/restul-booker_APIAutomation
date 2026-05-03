package api.endpoints;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class AuthEndPoints 
{
	public static String getToken() 
	{
		String payload = "{ \"username\": \"admin\", \"password\": \"password123\" }";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(payload)
        .when()
                .post(Routes.AUTH);

        return response.jsonPath().getString("token");
    }
}