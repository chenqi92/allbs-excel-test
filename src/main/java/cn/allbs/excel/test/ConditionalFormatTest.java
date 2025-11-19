package cn.allbs.excel.test;

import cn.allbs.excel.annotation.ConditionalFormat;
import cn.allbs.excel.handle.ConditionalFormatWriteHandler;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Conditional Format Test
 *
 * Test advanced conditional formatting with data bars, color scales, and icon sets
 */
public class ConditionalFormatTest {

    public static void main(String[] args) {
        String outputPath = "E:/temp/conditional_format_test.xlsx";

        // Generate test data
        List<PerformanceData> data = generatePerformanceData(20);

        // Export with conditional formatting
        EasyExcel.write(new File(outputPath), PerformanceData.class)
            .registerWriteHandler(new ConditionalFormatWriteHandler(PerformanceData.class))
            .sheet("Performance Report")
            .doWrite(data);

        System.out.println("Conditional format test completed!");
        System.out.println("Output file: " + outputPath);
        System.out.println("\nConditional formatting applied:");
        System.out.println("1. Score Column:");
        System.out.println("   - Icon Set: 3 Traffic Lights");
        System.out.println("   - Red: Low scores, Yellow: Medium, Green: High");
        System.out.println("2. Sales Column:");
        System.out.println("   - Icon Set: 3 Arrows");
        System.out.println("   - Shows trend direction");
        System.out.println("3. Completion Rate Column:");
        System.out.println("   - Icon Set: 3 Flags");
        System.out.println("   - Visual indicators for completion status");
        System.out.println("4. Rating Column:");
        System.out.println("   - Icon Set: 5 Quarters");
        System.out.println("   - 5-level rating visualization");
        System.out.println("\nNote: Due to POI limitations, some advanced features");
        System.out.println("(like data bars and color scales) use icon sets instead.");
    }

    private static List<PerformanceData> generatePerformanceData(int count) {
        List<PerformanceData> data = new ArrayList<>();
        Random random = new Random();

        String[] employees = {"Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Henry", "Ivy", "Jack"};
        String[] departments = {"Sales", "IT", "HR", "Marketing", "Finance"};

        for (int i = 0; i < count; i++) {
            PerformanceData record = new PerformanceData();
            record.setEmployeeName(employees[i % employees.length] + " " + (i / employees.length + 1));
            record.setDepartment(departments[random.nextInt(departments.length)]);
            record.setScore(60 + random.nextInt(40));  // 60-100
            record.setSales(new BigDecimal(10000 + random.nextInt(90000)));  // 10k-100k
            record.setCompletionRate(50.0 + random.nextDouble() * 50.0);  // 50%-100%
            record.setRating(1 + random.nextInt(5));  // 1-5

            data.add(record);
        }

        return data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceData {

        @ExcelProperty(value = "Employee", index = 0)
        private String employeeName;

        @ExcelProperty(value = "Department", index = 1)
        private String department;

        @ExcelProperty(value = "Score", index = 2)
        @ConditionalFormat(
            type = ConditionalFormat.FormatType.ICON_SET,
            iconSet = ConditionalFormat.IconSetType.THREE_TRAFFIC_LIGHTS_1
        )
        private Integer score;

        @ExcelProperty(value = "Sales Amount", index = 3)
        @ConditionalFormat(
            type = ConditionalFormat.FormatType.ICON_SET,
            iconSet = ConditionalFormat.IconSetType.THREE_ARROWS
        )
        private BigDecimal sales;

        @ExcelProperty(value = "Completion Rate", index = 4)
        @ConditionalFormat(
            type = ConditionalFormat.FormatType.ICON_SET,
            iconSet = ConditionalFormat.IconSetType.THREE_FLAGS
        )
        private Double completionRate;

        @ExcelProperty(value = "Rating", index = 5)
        @ConditionalFormat(
            type = ConditionalFormat.FormatType.ICON_SET,
            iconSet = ConditionalFormat.IconSetType.FIVE_QUARTERS
        )
        private Integer rating;
    }
}
