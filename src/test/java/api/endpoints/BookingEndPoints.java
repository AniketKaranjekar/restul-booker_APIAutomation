package api.endpoints;

import static io.restassured.RestAssured.given;
import api.payload.Booking;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class BookingEndPoints 
{

    public static Response createBooking(Booking payload) 
    {
        return given()
                .contentType(ContentType.JSON)
                .body(payload)
        .when()
                .post(Routes.CREATE_BOOKING);
    }

    public static Response getBooking(int bookingId) 
    {
        return given()
                .pathParam("id", bookingId)
        .when()
                .get(Routes.GET_BOOKING);
    }

    public static Response updateBooking(int bookingId, Booking payload) 
    {
        return given()
                .contentType(ContentType.JSON)
                .pathParam("id", bookingId)
                .body(payload)
        .when()
                .put(Routes.UPDATE_BOOKING);
    }

    public static Response deleteBooking(int bookingId)
    {
        return given()
                .pathParam("id", bookingId)
        .when()
                .delete(Routes.DELETE_BOOKING);
    }
    
    public static Response updateBooking(int bookingId, Booking payload, String token) 
    {
    	
    		return given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .pathParam("id", bookingId)
                .body(payload)
        .when()
                .put(Routes.UPDATE_BOOKING);
    }
    
    public static Response deleteBooking(int bookingId, String token) 
    {
    		return given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .pathParam("id", bookingId)
                .log().all()
        .when()
                .delete(Routes.DELETE_BOOKING)
        .then()
                .log().all()
                .extract().response();
    }
}