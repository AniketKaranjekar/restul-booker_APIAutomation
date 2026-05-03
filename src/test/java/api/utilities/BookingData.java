package api.utilities;

import org.testng.annotations.DataProvider;

public class BookingData 
{
    @DataProvider(name = "excelData")
    public static Object[][] getData() {

        String path = System.getProperty("user.dir") + "/src/test/resources/BookingData.xlsx";

        return ExcelUtils.getExcelData(path, "Sheet1");
    }
}