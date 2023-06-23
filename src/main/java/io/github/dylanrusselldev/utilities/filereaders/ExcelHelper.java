/*
 * Filename: ExcelHelper.java
 * Author: Dylan Russell
 * Purpose: Class that enables the use of reading data from an Excel file
 */

package io.github.dylanrusselldev.utilities.filereaders;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class ExcelHelper {

    private final Sheet sheet;
    private final String path;
    private final Workbook wb;
    private int startRow = 0;
    private Iterator<Row> rowIterator;

    public ExcelHelper(String filePath, String sheetName) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);

        wb = WorkbookFactory.create(fis);
        sheet = wb.getSheet(sheetName);
        rowIterator = sheet.iterator();
        path = filePath;
        fis.close();
    } // end constructor

    /**
     * Get the value from a cell based off the row number and column name.
     *
     * @param rowIndex the row number
     * @param colName  the name of the column
     * @return the value found in the cell
     */
    public String readCellData(int rowIndex, String colName) {

        // Get column index from column name
        int colIndex = getColumnIndexByName(colName);
        int rowNum;

        rowNum = startRow + (rowIndex - 1);
        Row row = sheet.getRow(rowNum);

        return getDataByColumnIndex(row, colIndex);
    } // end readCellData()

    /**
     * Write a string value to a cell based off the row number and column name
     *
     * @param rowIndex the row number
     * @param colName  the name of the column
     * @param value    the value to write to the cell
     */
    public void setCellData(int rowIndex, String colName, String value) throws IOException {

        // Get the column number based off the header name
        int colIndex = getColumnIndexByName(colName);

        // Get the row number while accounting for column headers/names
        int rowNum = startRow + (rowIndex - 1);
        Row row = sheet.getRow(rowNum);

        // Create the cell and set the value
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(value);

        // Write to the file
        FileOutputStream fos = new FileOutputStream(path);
        wb.write(fos);
        fos.close();

    }

    /**
     * Get the data from the cell.
     *
     * @param cell the cell object
     * @return a string of the data found in the cell
     */
    private String getCellData(Cell cell) {
        DataFormatter dataFormatter = new DataFormatter();
        return dataFormatter.formatCellValue(cell);
    } // end getCellData()

    /**
     * Gets the column number based off the given column name.
     *
     * @param colName the column name
     * @return the column number
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

    /**
     * Get the value based off a column number.
     *
     * @param row      the Row object
     * @param colIndex the column number
     * @return the value from the cell based off the column number
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

} // end ExcelHelper.java
