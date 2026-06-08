package com.toolshop.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ExcelReader — Utility for reading test data from an external .xlsx file
 * using Apache POI. This class is used exclusively by ExcelDataProvider.
 *
 * Expected Excel layout (per sheet):
 *   Row 0  → Header row  (skipped automatically)
 *   Row 1+ → Data rows
 *
 *   Columns depend on the sheet being read — see ExcelDataProvider for the
 *   exact column mapping per test module.
 */
public class ExcelReader {

    private final String filePath;

    /**
     * @param filePath  Absolute or classpath-relative path to the .xlsx file.
     */
    public ExcelReader(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads all data rows from a named sheet and returns them as a 2-D
     * Object array suitable for a TestNG @DataProvider.
     *
     * @param sheetName  Exact name of the sheet tab to read.
     * @param columns    Number of data columns to extract from each row.
     * @return           Object[][] where each inner array is one test case row.
     * @throws RuntimeException if the file or sheet cannot be opened.
     */
    public Object[][] readSheet(String sheetName, int columns) {
        List<Object[]> dataList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException(
                        "[ExcelReader] Sheet not found: \"" + sheetName + "\" in " + filePath);
            }

            // Row 0 is the header — start from row 1
            int lastRow = sheet.getLastRowNum();
            for (int rowIndex = 1; rowIndex <= lastRow; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                // Skip rows where the first cell is empty 
                Cell firstCell = row.getCell(0);
                if (firstCell == null || getCellValue(firstCell).trim().isEmpty()) {
                    continue;
                }

                Object[] rowData = new Object[columns];
                for (int col = 0; col < columns; col++) {
                    Cell cell = row.getCell(col);
                    rowData[col] = (cell != null) ? getCellValue(cell) : "";
                }
                dataList.add(rowData);
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "[ExcelReader] Failed to read Excel file: " + filePath, e);
        }

        return dataList.toArray(new Object[0][]);
    }

    /**
     * Reads a single column from a named sheet and returns a flat String array.
     * Useful when only one value per row is needed.
     *
     * @param sheetName   Exact name of the sheet tab.
     * @param columnIndex Zero-based column index.
     * @return            String[] of cell values (header row excluded).
     */
    public String[] readColumn(String sheetName, int columnIndex) {
        Object[][] rawData = readSheet(sheetName, columnIndex + 1);
        String[] column = new String[rawData.length];
        for (int i = 0; i < rawData.length; i++) {
            column[i] = String.valueOf(rawData[i][columnIndex]);
        }
        return column;
    }

    // ─── Private Helpers ─────────────────────────────────────────────────────

    /**
     * Normalizes any cell type to a plain String value.
     * Numeric cells that represent whole numbers are returned without decimals
     * (e.g. 42.0 → "42") so passwords like "123456" aren't corrupted.
     */
    private String getCellValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                // Avoid "1.0", "2.0" etc. for whole-number cells
                double numericValue = cell.getNumericCellValue();
                if (numericValue == Math.floor(numericValue)) {
                    return String.valueOf((long) numericValue);
                }
                return String.valueOf(numericValue);

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case FORMULA:
                // Evaluate formulas and return the cached result as a string
                FormulaEvaluator evaluator = cell.getSheet()
                        .getWorkbook()
                        .getCreationHelper()
                        .createFormulaEvaluator();
                CellValue evaluated = evaluator.evaluate(cell);
                return getCellValueFromEvaluated(evaluated);

            case BLANK:
            default:
                return "";
        }
    }

    private String getCellValueFromEvaluated(CellValue cv) {
        switch (cv.getCellType()) {
            case STRING:  return cv.getStringValue().trim();
            case NUMERIC: {
                double v = cv.getNumberValue();
                return (v == Math.floor(v)) ? String.valueOf((long) v) : String.valueOf(v);
            }
            case BOOLEAN: return String.valueOf(cv.getBooleanValue());
            default:      return "";
        }
    }
}
