package cn.allbs.excel.test;

import cn.allbs.excel.listener.ErrorCollectingReadListener;
import cn.allbs.excel.model.ExcelReadError;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Error Collection Test
 *
 * Test import error collection mechanism
 */
public class ErrorCollectionTest {

    public static void main(String[] args) {
        // First, create a test Excel file with some invalid data
        String testFilePath = "E:/temp/error_collection_test_data.xlsx";
        createTestFile(testFilePath);

        // Then, import with error collection
        testErrorCollection(testFilePath);
    }

    private static void createTestFile(String filePath) {
        List<UserImportData> data = new ArrayList<>();

        // Valid data
        data.add(new UserImportData("U001", "Alice", "alice@example.com", 25, new BigDecimal("5000.00"), "2020-01-15"));
        data.add(new UserImportData("U002", "Bob", "bob@example.com", 30, new BigDecimal("6000.00"), "2019-05-20"));

        // Invalid data - various errors
        data.add(new UserImportData("U003", "Charlie", "invalid-email", 18, new BigDecimal("4500.00"), "2021-03-10"));  // Invalid email format
        data.add(new UserImportData("U004", "", "david@example.com", 35, new BigDecimal("7000.00"), "2018-08-05"));     // Empty name
        data.add(new UserImportData("", "Eve", "eve@example.com", 28, new BigDecimal("5500.00"), "2020-11-20"));        // Empty ID
        data.add(new UserImportData("U006", "Frank", "frank@example.com", 200, new BigDecimal("6500.00"), "2019-02-15")); // Invalid age
        data.add(new UserImportData("U007", "Grace", "grace@example.com", 32, new BigDecimal("-1000.00"), "2017-06-30")); // Negative salary

        // More valid data
        data.add(new UserImportData("U008", "Henry", "henry@example.com", 27, new BigDecimal("5200.00"), "2020-09-12"));
        data.add(new UserImportData("U009", "Ivy", "ivy@example.com", 29, new BigDecimal("5800.00"), "2019-12-01"));

        // Invalid date format
        data.add(new UserImportData("U010", "Jack", "jack@example.com", 31, new BigDecimal("6200.00"), "invalid-date"));

        EasyExcel.write(new File(filePath), UserImportData.class)
            .sheet("Users")
            .doWrite(data);

        System.out.println("Test file created: " + filePath);
        System.out.println("Total rows: " + data.size());
        System.out.println("Valid rows: 4");
        System.out.println("Invalid rows: 6");
        System.out.println();
    }

    private static void testErrorCollection(String filePath) {
        System.out.println("=".repeat(80));
        System.out.println("Testing Error Collection");
        System.out.println("=".repeat(80));

        // Create error collecting listener
        ErrorCollectingReadListener<UserImportData> listener = new ErrorCollectingReadListener<>();

        // Configure error handling
        listener.setContinueOnError(true)    // Continue reading even on errors
                .setMaxErrors(100)            // Maximum 100 errors
                .setDataConsumer(validUsers -> {
                    // Process valid data in batches
                    System.out.println("  Processing batch of " + validUsers.size() + " valid records...");
                });

        try {
            // Read Excel with error collection
            EasyExcel.read(new File(filePath), UserImportData.class, listener)
                .sheet()
                .doRead();

            // Display results
            System.out.println("\n" + "=".repeat(80));
            System.out.println("Import Results");
            System.out.println("=".repeat(80));

            System.out.println("Total valid records: " + listener.getValidDataCount());
            System.out.println("Total errors: " + listener.getErrorCount());
            System.out.println();

            // Display valid data
            if (!listener.getValidData().isEmpty()) {
                System.out.println("Valid Data (first 5):");
                listener.getValidData().stream()
                    .limit(5)
                    .forEach(user -> System.out.println("  - " + user.getUserId() + ": " + user.getUserName() + " (" + user.getEmail() + ")"));
                System.out.println();
            }

            // Display errors
            if (listener.hasErrors()) {
                System.out.println("Errors Found:");
                for (ExcelReadError error : listener.getErrors()) {
                    System.out.println("  [Row " + error.getExcelRowNumber() + "] " + error.getFormattedMessage());
                }
                System.out.println();
            }

            // Summary
            System.out.println("=".repeat(80));
            System.out.println("Summary:");
            System.out.println("  Success Rate: " + String.format("%.1f%%",
                (listener.getValidDataCount() * 100.0) / (listener.getValidDataCount() + listener.getErrorCount())));
            System.out.println("=".repeat(80));

        } catch (Exception e) {
            System.err.println("Error during import: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserImportData {

        @ExcelProperty(value = "User ID", index = 0)
        private String userId;

        @ExcelProperty(value = "User Name", index = 1)
        private String userName;

        @ExcelProperty(value = "Email", index = 2)
        private String email;

        @ExcelProperty(value = "Age", index = 3)
        private Integer age;

        @ExcelProperty(value = "Salary", index = 4)
        private BigDecimal salary;

        @ExcelProperty(value = "Join Date", index = 5)
        private String joinDate;
    }
}
