package cn.allbs.excel.test;

import cn.allbs.excel.annotation.ExcelSheetStyle;
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
import java.util.Random;

/**
 * Sheet Style Test
 *
 * Test freeze panes, auto-filter, zoom, column width, and grid lines
 */
public class SheetStyleTest {

    public static void main(String[] args) {
        String outputPath = "E:/temp/sheet_style_test.xlsx";

        // Generate test data
        List<EmployeeData> data = generateEmployeeData(100);

        // Export with sheet styles
        EasyExcel.write(new File(outputPath), EmployeeData.class)
            .registerWriteHandler(new ExcelSheetStyleWriteHandler(EmployeeData.class))
            .sheet("Employee List")
            .doWrite(data);

        System.out.println("Sheet style test completed!");
        System.out.println("Output file: " + outputPath);
        System.out.println("\nSheet style features tested:");
        System.out.println("1. Freeze Panes:");
        System.out.println("   - First row (header) is frozen");
        System.out.println("   - First 2 columns (ID, Name) are frozen");
        System.out.println("2. Auto-Filter:");
        System.out.println("   - Filter dropdowns enabled on header row");
        System.out.println("3. Default Column Width: 18 characters");
        System.out.println("4. Zoom Scale: 110%");
        System.out.println("5. Grid Lines: Visible");
        System.out.println("\nTry scrolling the Excel file to see frozen panes in action!");
    }

    private static List<EmployeeData> generateEmployeeData(int count) {
        List<EmployeeData> data = new ArrayList<>();
        Random random = new Random();

        String[] departments = {"IT", "HR", "Sales", "Marketing", "Finance"};
        String[] positions = {"Manager", "Senior", "Junior", "Intern"};
        String[] cities = {"Beijing", "Shanghai", "Guangzhou", "Shenzhen", "Hangzhou"};

        for (int i = 1; i <= count; i++) {
            EmployeeData employee = new EmployeeData();
            employee.setEmployeeId("EMP" + String.format("%04d", i));
            employee.setName("Employee " + i);
            employee.setDepartment(departments[random.nextInt(departments.length)]);
            employee.setPosition(positions[random.nextInt(positions.length)]);
            employee.setCity(cities[random.nextInt(cities.length)]);
            employee.setSalary(new BigDecimal(5000 + random.nextInt(15000)));
            employee.setAge(22 + random.nextInt(38));
            employee.setYearsOfService(1 + random.nextInt(20));

            data.add(employee);
        }

        return data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ExcelSheetStyle(
        freezeRow = 1,              // Freeze header row
        freezeColumn = 2,           // Freeze first 2 columns (ID, Name)
        autoFilter = true,          // Enable auto-filter
        autoFilterStartRow = 0,     // Start filter from header row
        defaultColumnWidth = 18,    // Set column width
        zoomScale = 110,            // 110% zoom
        showGridLines = true        // Show grid lines
    )
    public static class EmployeeData {

        @ExcelProperty(value = "Employee ID", index = 0)
        private String employeeId;

        @ExcelProperty(value = "Name", index = 1)
        private String name;

        @ExcelProperty(value = "Department", index = 2)
        private String department;

        @ExcelProperty(value = "Position", index = 3)
        private String position;

        @ExcelProperty(value = "City", index = 4)
        private String city;

        @ExcelProperty(value = "Salary", index = 5)
        private BigDecimal salary;

        @ExcelProperty(value = "Age", index = 6)
        private Integer age;

        @ExcelProperty(value = "Years of Service", index = 7)
        private Integer yearsOfService;
    }
}
