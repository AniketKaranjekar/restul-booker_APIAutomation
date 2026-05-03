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
import io.restassured.response.Response;

public class BookingDataDrivenTests 
{
    private static final Logger log = LogManager.getLogger(BookingDataDrivenTests.class);

    String token;

    @BeforeClass
    public void setup() 
    {
        log.info("===== Test Suite Setup Started =====");

        token = AuthEndPoints.getToken();
        log.info("Auth token generated successfully");

        log.info("===== Test Suite Setup Completed =====");
    }

    @Test(dataProvider = "excelData", dataProviderClass = BookingData.class)
    public void testEndToEndFlowFromExcel(String fname, String lname, int price,
                                          boolean deposit, String needs, String updatedName) 
    {
        log.info("--------------------------------------------------");
        log.info("Starting Test with Data -> {} {} | Price: {} | Deposit: {} | Needs: {} | UpdateName: {}",
                fname, lname, price, deposit, needs, updatedName);

        // Build payload
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

        log.info("Payload created");

        // Create
        log.info("Sending CREATE booking request");
        Response createRes = BookingEndPoints.createBooking(booking);
        createRes.then().log().all();

        log.info("Create Response Code: {}", createRes.getStatusCode());
        log.debug("Create Response Body: {}", createRes.asPrettyString());

        Assert.assertEquals(createRes.getStatusCode(), 200);

        int bookingId = createRes.jsonPath().getInt("bookingid");
        log.info("Booking created with ID: {}", bookingId);
        Assert.assertTrue(bookingId > 0);

        // Get
        log.info("Fetching booking with ID: {}", bookingId);
        Response getRes = BookingEndPoints.getBooking(bookingId);

        log.info("Get Response Code: {}", getRes.getStatusCode());
        log.debug("Get Response Body: {}", getRes.asPrettyString());

        Assert.assertEquals(getRes.getStatusCode(), 200);
        Assert.assertEquals(getRes.jsonPath().getString("firstname"), fname);
        Assert.assertEquals(getRes.jsonPath().getString("lastname"), lname);
        Assert.assertEquals(getRes.jsonPath().getInt("totalprice"), price);

        log.info("GET validation passed");

        // Update
        log.info("Updating booking ID: {}", bookingId);
        booking.setFirstname(updatedName);

        Response updateRes = BookingEndPoints.updateBooking(bookingId, booking, token);

        log.info("Update Response Code: {}", updateRes.getStatusCode());
        log.debug("Update Response Body: {}", updateRes.asPrettyString());

        Assert.assertEquals(updateRes.getStatusCode(), 200);
        Assert.assertEquals(updateRes.jsonPath().getString("firstname"), updatedName);

        log.info("Update successful");

        // Delete
        log.info("Deleting booking ID: {}", bookingId);
        Response deleteRes = BookingEndPoints.deleteBooking(bookingId, token);

        log.info("Delete Response Code: {}", deleteRes.getStatusCode());

        Assert.assertEquals(deleteRes.getStatusCode(), 201);

        log.info("Delete successful");

        // Verify Delete
        log.info("Verifying deletion for booking ID: {}", bookingId);
        Response verifyRes = BookingEndPoints.getBooking(bookingId);

        log.info("Verify Response Code: {}", verifyRes.getStatusCode());

        Assert.assertEquals(verifyRes.getStatusCode(), 404);

        log.info("Deletion verified successfully");
        log.info("Test Completed for Booking ID: {}", bookingId);
        log.info("--------------------------------------------------");
    }
}