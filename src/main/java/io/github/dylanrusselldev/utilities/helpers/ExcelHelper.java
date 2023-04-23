/*
 * Filename: ExcelHelper.java
 * Author: Dylan Russell
 * Purpose: Class that enables the use of reading data from an Excel file
 */

package io.github.dylanrusselldev.utilities.helpers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.util.Iterator;

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

    /**
     * Get the value from a cell based off the row number and column anme
     *
     * @param rowIndex   The row number
     * @param columnName The name of the column
     */
    public String readCellData(int rowIndex, String columnName) {

        // Get column index from column name
        int colIndex = getColumnIndexByName(columnName);
        int rowNum;

        rowNum = startRow + (rowIndex - 1);
        Row row = sheet.getRow(rowNum);

        return getDataByColumnIndex(row, colIndex);
    } // end readCellData()

    /**
     * Get the value based off a column number
     *
     * @param row      The Row
     * @param colIndex The column number
     */
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

    /**
     * Get the data from the cell
     *
     * @param cell  The cell object
     */
    private String getCellData(Cell cell) {
        DataFormatter df = new DataFormatter();
        return df.formatCellValue(cell);
    } // end getCellData()

    /**
     * Gets the column number based off the given column name
     *
     * @param colName the column name
     */
    private int getColumnIndexByName(String colName) {

        rowIterator = sheet.iterator();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            Iterator<Cell> cellIterator = row.cellIterator();
            int currentColumn = -1;

            while (cellIterator.hasNext()) {
                ++currentColumn;

                Cell cell = cellIterator.next();
                String cellData = getCellData(cell);

                if (cellData != null && cellData.equalsIgnoreCase(colName)) {
                    startRow = row.getRowNum() + 1;
                    return currentColumn;
                } // end if

            } // end inner while

        } // end outer while

        return -1;
    } // end getColumnIndexByName()

} // end ExcelHelper.java
