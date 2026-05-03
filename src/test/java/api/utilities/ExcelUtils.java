package api.utilities;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils 
{
    public static Object[][] getExcelData(String fileName, String sheetName) 
    {
        List<Object[]> dataList = new ArrayList<>();

        try 
        {
            InputStream is = ExcelUtils.class
                    .getClassLoader()
                    .getResourceAsStream(fileName);

            if (is == null) 
            {
                throw new RuntimeException("Excel file not found in resources: " + fileName);
            }

            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) 
            {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            int rows = sheet.getPhysicalNumberOfRows();
            int cols = sheet.getRow(0).getPhysicalNumberOfCells();

            for (int i = 1; i < rows; i++) 
            {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Object[] rowData = new Object[cols];
                boolean isRowEmpty = true;

                for (int j = 0; j < cols; j++) 
                {
                    Cell cell = row.getCell(j);
                    Object value = getCellValue(cell, j);

                    if (value != null && !value.toString().trim().isEmpty()) 
                    {
                        isRowEmpty = false;
                    }

                    rowData[j] = value;
                }

                if (!isRowEmpty) 
                {
                    dataList.add(rowData);
                }
            }

            workbook.close();
            is.close();
        } 
        catch (Exception e) 
        {
            throw new RuntimeException("Error reading Excel file: " + e.getMessage());
        }

        Object[][] data = new Object[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) 
        {
            data[i] = dataList.get(i);
        }

        // Debug logs
        System.out.println("===== Excel Data =====");
        for (Object[] row : data) 
        {
            for (Object cell : row) 
            {
                System.out.print(cell + " | ");
            }
            System.out.println();
        }
        System.out.println("======================");

        return data;
    }

    private static Object getCellValue(Cell cell, int columnIndex) 
    {
        if (cell == null) return "";

        switch (cell.getCellType()) 
        {
            case STRING:
                String value = cell.getStringCellValue().trim();

                if (columnIndex == 3) 
                {
                    return Boolean.parseBoolean(value);
                }
                return value;

            case NUMERIC:
                return (int) cell.getNumericCellValue();

            case BOOLEAN:
                return cell.getBooleanCellValue();

            case FORMULA:
                return cell.getStringCellValue();

            default:
                return "";
        }
    }
}