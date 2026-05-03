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
import api.utilities.RetryAnalyzer;
import io.restassured.response.Response;

public class BookingTests 
{
    Faker faker;
    Booking bookingPayload;
    int bookingId;
    String token;

    private static final Logger log = LogManager.getLogger(BookingTests.class);

    @BeforeClass
    public void setupData() 
    {
        log.info("===== Setup Started =====");

        faker = new Faker();
        token = AuthEndPoints.getToken();

        log.info("Token generated");

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

        log.info("Initial Payload Ready");
        log.info("===== Setup Completed =====");
    }

    @Test(priority = 1, retryAnalyzer = RetryAnalyzer.class)
    public void testCreateBooking() 
    {
        log.info("===== CREATE BOOKING =====");

        Response response = BookingEndPoints.createBooking(bookingPayload);
        response.then().log().all();

        int status = response.getStatusCode();
        log.info("Create Status Code: {}", status);

        Assert.assertTrue(status == 200 || status == 201, "Create failed");

        bookingId = response.jsonPath().getInt("bookingid");
        log.info("Booking ID generated: {}", bookingId);

        Assert.assertTrue(bookingId > 0, "Invalid booking ID");
    }

    @Test(priority = 2, dependsOnMethods = "testCreateBooking", retryAnalyzer = RetryAnalyzer.class)
    public void testGetBooking() 
    {
        log.info("===== GET BOOKING ===== ID: {}", bookingId);

        Response response = BookingEndPoints.getBooking(bookingId);
        response.then().log().all();

        log.info("Get Status Code: {}", response.getStatusCode());

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("firstname"), bookingPayload.getFirstname());

        log.info("GET validation successful");
    }
    
    @Test(priority = 3, dependsOnMethods = "testCreateBooking", retryAnalyzer = RetryAnalyzer.class)
    public void testUpdateBooking() 
    {
        log.info("===== UPDATE BOOKING ===== ID: {}", bookingId);

        bookingPayload.setFirstname(faker.name().firstName());
        bookingPayload.setLastname(faker.name().lastName());

        log.info("Updated Data -> {} {}", 
                bookingPayload.getFirstname(), 
                bookingPayload.getLastname());

        Response response = BookingEndPoints.updateBooking(bookingId, bookingPayload, token);
        response.then().log().all();

        log.info("Update Status Code: {}", response.getStatusCode());

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("firstname"), bookingPayload.getFirstname());

        log.info("UPDATE successful");
    }
    
    @Test(priority = 4, dependsOnMethods = "testCreateBooking", retryAnalyzer = RetryAnalyzer.class)
    public void testDeleteBooking() 
    {
        log.info("===== DELETE BOOKING ===== ID: {}", bookingId);

        Response response = BookingEndPoints.deleteBooking(bookingId, token);
        response.then().log().all();

        int status = response.getStatusCode();
        log.info("Delete Status Code: {}", status);

        Assert.assertTrue(status == 201 || status == 200, "Delete failed");

        log.info("DELETE successful");
    }
    
    @Test(priority = 5, dependsOnMethods = "testDeleteBooking", retryAnalyzer = RetryAnalyzer.class)
    public void testVerifyDeletedBooking() 
    {
        log.info("===== VERIFY DELETE ===== ID: {}", bookingId);

        Response response = BookingEndPoints.getBooking(bookingId);
        response.then().log().all();

        log.info("Verify Status Code: {}", response.getStatusCode());

        Assert.assertEquals(response.getStatusCode(), 404);

        log.info("Deletion verified");
    }
}