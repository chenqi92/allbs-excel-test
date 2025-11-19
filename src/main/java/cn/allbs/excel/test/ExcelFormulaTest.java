package cn.allbs.excel.test;

import cn.allbs.excel.annotation.ExcelFormula;
import cn.allbs.excel.annotation.ExcelSheetStyle;
import cn.allbs.excel.handle.ExcelFormulaWriteHandler;
import cn.allbs.excel.handle.ExcelSheetStyleWriteHandler;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel Formula Test
 *
 * Test Excel formula support with automatic calculations
 */
public class ExcelFormulaTest {

    public static void main(String[] args) {
        String outputPath = "E:/temp/excel_formula_test.xlsx";

        // Generate test data
        List<SalesRecord> data = generateSalesData();

        // Export with formulas
        EasyExcel.write(new File(outputPath), SalesRecord.class)
            .registerWriteHandler(new ExcelFormulaWriteHandler(SalesRecord.class))
            .registerWriteHandler(new ExcelSheetStyleWriteHandler(SalesRecord.class))
            .sheet("Sales Report")
            .doWrite(data);

        System.out.println("Excel formula test completed!");
        System.out.println("Output file: " + outputPath);
        System.out.println("\nFormula demonstrations:");
        System.out.println("1. Total Price = Unit Price * Quantity (Column D)");
        System.out.println("2. Tax Amount = Total Price * 0.1 (Column E)");
        System.out.println("3. Final Amount = Total Price + Tax (Column F)");
        System.out.println("4. Grand Total = SUM of all Total Prices (Last row)");
        System.out.println("\nFeatures tested:");
        System.out.println("- Row placeholders: {row}");
        System.out.println("- Last row placeholder: {lastRow}");
        System.out.println("- Column references: C{row}, D{row}");
        System.out.println("- Aggregate functions: SUM");
        System.out.println("- Frozen header row");
        System.out.println("- Auto-filter enabled");
    }

    private static List<SalesRecord> generateSalesData() {
        List<SalesRecord> data = new ArrayList<>();

        String[] products = {"Laptop", "Mouse", "Keyboard", "Monitor", "Headset"};
        BigDecimal[] prices = {
            new BigDecimal("1200.00"),
            new BigDecimal("25.50"),
            new BigDecimal("75.00"),
            new BigDecimal("350.00"),
            new BigDecimal("89.99")
        };

        for (int i = 0; i < products.length; i++) {
            SalesRecord record = new SalesRecord();
            record.setProductName(products[i]);
            record.setUnitPrice(prices[i]);
            record.setQuantity(i + 1);
            data.add(record);
        }

        // Add summary row
        SalesRecord summary = new SalesRecord();
        summary.setProductName("TOTAL");
        summary.setUnitPrice(null);
        summary.setQuantity(null);
        data.add(summary);

        return data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ExcelSheetStyle(
        freezeRow = 1,           // Freeze header row
        autoFilter = true,       // Enable auto-filter
        defaultColumnWidth = 15,
        zoomScale = 120
    )
    public static class SalesRecord {

        @ExcelProperty(value = "Product Name", index = 0)
        private String productName;

        @ExcelProperty(value = "Unit Price", index = 1)
        private BigDecimal unitPrice;

        @ExcelProperty(value = "Quantity", index = 2)
        private Integer quantity;

        @ExcelProperty(value = "Total Price", index = 3)
        @ExcelFormula(value = "=B{row}*C{row}", enabled = true)
        private BigDecimal totalPrice;

        @ExcelProperty(value = "Tax (10%)", index = 4)
        @ExcelFormula(value = "=D{row}*0.1", enabled = true)
        private BigDecimal taxAmount;

        @ExcelProperty(value = "Final Amount", index = 5)
        @ExcelFormula(value = "=D{row}+E{row}", enabled = true)
        private BigDecimal finalAmount;

        @ExcelProperty(value = "Grand Total", index = 6)
        @ExcelFormula(
            value = "=SUM(D2:D{lastRow})",
            enabled = true,
            limitToRange = true,
            startRow = 6,  // Only apply to last row (summary row)
            endRow = 6
        )
        private BigDecimal grandTotal;
    }
}
