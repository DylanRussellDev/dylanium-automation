package io.github.dylanrusselldev.utilities.helpers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.util.Iterator;

/*
 * Filename: ExcelHelper.java
 * Purpose: Class that enables the use of reading data from an Excel file
 */
public class ExcelHelper {

    private final Sheet sheet;
    private int startRow = 0;
    private Iterator<Row> rowIterator;

    public ExcelHelper(String filePath, String sheetName) throws Exception {
        FileInputStream file = new FileInputStream(filePath);

        Workbook wb = WorkbookFactory.create(file);
        sheet = wb.getSheet(sheetName);
        rowIterator = sheet.iterator();

        file.close();
    } // end constructor

    public String readCellData(int rowIndex, String columnName) {

        // Get column index from column name
        int colIndex = getColumnIndexByName(columnName);
        int rowNum;

        rowNum = startRow + (rowIndex - 1);
        Row row = sheet.getRow(rowNum);

        return getDataByColumnIndex(row, colIndex);
    } // end readCellData()

    private String getDataByColumnIndex(Row row, int colIndex) {

        Iterator<Cell> cellIterator = row.cellIterator();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            if (colIndex == cell.getColumnIndex()) {
                return getCellData(cell);
            } // end if

        } // end while

        return null;
    } // end getDataByColumnIndex()

    private String getCellData(Cell cell) {
        DataFormatter df = new DataFormatter();
        return df.formatCellValue(cell);
    } // end getCellData()

    private int getColumnIndexByName(String headerName) {

        rowIterator = sheet.iterator();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            Iterator<Cell> cellIterator = row.cellIterator();
            int currentColumn = -1;

            while (cellIterator.hasNext()) {
                ++currentColumn;

                Cell cell = cellIterator.next();
                String cellData = getCellData(cell);

                if (cellData != null && cellData.equalsIgnoreCase(headerName)) {
                    startRow = row.getRowNum() + 1;
                    return currentColumn;
                } // end if

            } // end inner while

        } // end outer while

        return -1;
    } // end getColumnIndexByName()

} // end ExcelHelper.java
