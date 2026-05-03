package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import api.endpoints.AuthEndPoints;
import api.endpoints.BookingEndPoints;
import api.payload.Booking;
import api.payload.BookingDates;
import api.utilities.BookingData;
import api.utilities.RetryAnalyzer;
import io.restassured.response.Response;

public class BookingDataDrivenTests 
{
    private static final Logger log = LogManager.getLogger(BookingDataDrivenTests.class);

    String token;

    @BeforeClass
    public void setup() 
    {
        log.info("=== Setup Started ===");
        token = AuthEndPoints.getToken();
        log.info("Token generated");
    }

    @Test(dataProvider = "excelData", 
          dataProviderClass = BookingData.class,
          retryAnalyzer = RetryAnalyzer.class)
    public void testEndToEndFlowFromExcel(String fname, String lname, int price,
                                          boolean deposit, String needs, String updatedName) 
    {
        log.info("Running test with data: {} {} | {}", fname, lname, price);

        // Payload
        Booking booking = new Booking();
        booking.setFirstname(fname);
        booking.setLastname(lname);
        booking.setTotalprice(price);
        booking.setDepositpaid(deposit);
        booking.setAdditionalneeds(needs);

        BookingDates dates = new BookingDates();
        dates.setCheckin("2024-01-01");
        dates.setCheckout("2024-01-05");
        booking.setBookingdates(dates);

        // CREATE
        Response createRes = BookingEndPoints.createBooking(booking);
        log.info("Create Status: {}", createRes.getStatusCode());

        Assert.assertTrue(
            createRes.getStatusCode() == 200 || createRes.getStatusCode() == 201,
            "Create failed"
        );

        int bookingId = createRes.jsonPath().getInt("bookingid");
        log.info("Booking ID: {}", bookingId);

        Assert.assertTrue(bookingId > 0, "Invalid booking ID");

        // GET
        Response getRes = BookingEndPoints.getBooking(bookingId);
        log.info("Get Status: {}", getRes.getStatusCode());

        Assert.assertEquals(getRes.getStatusCode(), 200);
        Assert.assertEquals(getRes.jsonPath().getString("firstname"), fname);

        // UPDATE
        booking.setFirstname(updatedName);

        Response updateRes = BookingEndPoints.updateBooking(bookingId, booking, token);
        log.info("Update Status: {}", updateRes.getStatusCode());

        Assert.assertEquals(updateRes.getStatusCode(), 200);
        Assert.assertEquals(
            updateRes.jsonPath().getString("firstname"), 
            updatedName
        );

        // DELETE
        Response deleteRes = BookingEndPoints.deleteBooking(bookingId, token);
        log.info("Delete Status: {}", deleteRes.getStatusCode());

        Assert.assertTrue(
            deleteRes.getStatusCode() == 201 || deleteRes.getStatusCode() == 200,
            "Delete failed"
        );

        // VERIFY DELETE
        Response verifyRes = BookingEndPoints.getBooking(bookingId);
        log.info("Verify Status: {}", verifyRes.getStatusCode());

        Assert.assertEquals(verifyRes.getStatusCode(), 404);

        log.info("Test completed for Booking ID: {}", bookingId);
    }
}