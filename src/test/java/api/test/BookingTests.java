package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;
import api.endpoints.AuthEndPoints;
import api.endpoints.BookingEndPoints;
import api.payload.Booking;
import api.payload.BookingDates;
import io.restassured.response.Response;

public class BookingTests 
{
    Faker faker;
    Booking bookingPayload;
    int bookingId;

    private static final Logger log = LogManager.getLogger(BookingTests.class);

    @BeforeClass
    public void setupData() 
    {
        log.info("===== Test Data Setup Started =====");

        faker = new Faker();

        bookingPayload = new Booking();
        bookingPayload.setFirstname(faker.name().firstName());
        bookingPayload.setLastname(faker.name().lastName());
        bookingPayload.setTotalprice(faker.number().numberBetween(100, 1000));
        bookingPayload.setDepositpaid(true);
        bookingPayload.setAdditionalneeds("Breakfast");

        BookingDates dates = new BookingDates();
        dates.setCheckin("2024-01-01");
        dates.setCheckout("2024-01-05");

        bookingPayload.setBookingdates(dates);

        log.info("Test Data: {}", bookingPayload);
        log.info("===== Test Data Setup Completed =====");
    }

    @Test(priority = 1)
    public void testCreateBooking() 
    {
        log.info("===== Creating Booking =====");

        Response response = BookingEndPoints.createBooking(bookingPayload);
        response.then().log().all();

        log.info("Response Status Code: {}", response.getStatusCode());
        log.debug("Response Body: {}", response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 200);

        bookingId = response.jsonPath().getInt("bookingid");
        log.info("Booking created successfully with ID: {}", bookingId);
    }

    @Test(priority = 2)
    public void testGetBooking() 
    {
        log.info("===== Fetching Booking with ID: {} =====", bookingId);

        Response response = BookingEndPoints.getBooking(bookingId);
        response.then().log().all();

        log.info("Response Status Code: {}", response.getStatusCode());

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("firstname"), bookingPayload.getFirstname());

        log.info("Booking details validated successfully");
    }
    
    @Test(priority = 3)
    public void testUpdateBooking() 
    {
        log.info("===== Updating Booking ID: {} =====", bookingId);

        String token = AuthEndPoints.getToken();
        log.info("Auth token generated");

        bookingPayload.setFirstname(faker.name().firstName());
        bookingPayload.setLastname(faker.name().lastName());

        log.info("Updated Data -> Firstname: {}, Lastname: {}", 
                  bookingPayload.getFirstname(), bookingPayload.getLastname());

        Response response = BookingEndPoints.updateBooking(bookingId, bookingPayload, token);
        response.then().log().all();

        log.info("Response Status Code: {}", response.getStatusCode());

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("firstname"), bookingPayload.getFirstname());
        Assert.assertEquals(response.jsonPath().getString("lastname"), bookingPayload.getLastname());

        log.info("Booking updated successfully");
    }
    
    @Test(priority = 4)
    public void testDeleteBooking() 
    {
        log.info("===== Deleting Booking ID: {} =====", bookingId);

        String token = AuthEndPoints.getToken();
        log.info("Auth token generated for delete");

        Response response = BookingEndPoints.deleteBooking(bookingId, token);
        response.then().log().all();

        log.info("Response Status Code: {}", response.getStatusCode());

        Assert.assertEquals(response.getStatusCode(), 201);

        log.info("Booking deleted successfully");
    }
    
    @Test(priority = 5)
    public void testVerifyDeletedBooking() 
    {
        log.info("===== Verifying Deletion for Booking ID: {} =====", bookingId);

        Response response = BookingEndPoints.getBooking(bookingId);
        response.then().log().all();

        log.info("Response Status Code: {}", response.getStatusCode());

        Assert.assertEquals(response.getStatusCode(), 404);

        log.info("Booking deletion verified successfully");
    }
}