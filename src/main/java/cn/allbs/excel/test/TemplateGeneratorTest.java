package cn.allbs.excel.test;

import cn.allbs.excel.template.ExcelTemplateGenerator;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Template Generator Test
 *
 * Test Excel import template generation
 */
public class TemplateGeneratorTest {

    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("Excel Template Generator Test");
        System.out.println("=".repeat(80));

        // Test 1: Generate empty template (header only)
        String emptyTemplate = "E:/temp/user_import_template_empty.xlsx";
        ExcelTemplateGenerator.generateTemplate(
            emptyTemplate,
            UserImportTemplate.class,
            0,  // No sample data
            "User Import Template"
        );
        System.out.println("\n1. Empty Template (Header Only):");
        System.out.println("   File: " + emptyTemplate);
        System.out.println("   Use this for actual imports");

        // Test 2: Generate template with 3 sample rows
        String sampleTemplate = "E:/temp/user_import_template_with_samples.xlsx";
        ExcelTemplateGenerator.generateTemplate(
            sampleTemplate,
            UserImportTemplate.class,
            3,  // 3 sample rows
            "User Import Template"
        );
        System.out.println("\n2. Template with Sample Data (3 rows):");
        System.out.println("   File: " + sampleTemplate);
        System.out.println("   Use this to see data format examples");

        // Test 3: Generate product template
        String productTemplate = "E:/temp/product_import_template.xlsx";
        ExcelTemplateGenerator.generateTemplate(
            productTemplate,
            ProductImportTemplate.class,
            5,  // 5 sample rows
            "Product Import"
        );
        System.out.println("\n3. Product Import Template (5 samples):");
        System.out.println("   File: " + productTemplate);

        System.out.println("\n" + "=".repeat(80));
        System.out.println("All templates generated successfully!");
        System.out.println("=".repeat(80));
        System.out.println("\nFeatures demonstrated:");
        System.out.println("- Empty template generation (header only)");
        System.out.println("- Template with sample data");
        System.out.println("- Intelligent sample data generation:");
        System.out.println("  * Names, emails, phones with realistic formats");
        System.out.println("  * Appropriate numeric ranges for age, quantity, price");
        System.out.println("  * Date fields with random past dates");
        System.out.println("  * Auto-sized columns for better readability");
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserImportTemplate {

        @ExcelProperty(value = "User ID", index = 0)
        private String userId;

        @ExcelProperty(value = "User Name", index = 1)
        private String userName;

        @ExcelProperty(value = "Email", index = 2)
        private String email;

        @ExcelProperty(value = "Phone Number", index = 3)
        private String phoneNumber;

        @ExcelProperty(value = "Age", index = 4)
        private Integer age;

        @ExcelProperty(value = "Department", index = 5)
        private String department;

        @ExcelProperty(value = "Salary", index = 6)
        private BigDecimal salary;

        @ExcelProperty(value = "Join Date", index = 7)
        private String joinDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductImportTemplate {

        @ExcelProperty(value = "Product Code", index = 0)
        private String productCode;

        @ExcelProperty(value = "Product Name", index = 1)
        private String productName;

        @ExcelProperty(value = "Category", index = 2)
        private String category;

        @ExcelProperty(value = "Price", index = 3)
        private BigDecimal price;

        @ExcelProperty(value = "Quantity", index = 4)
        private Integer quantity;

        @ExcelProperty(value = "Supplier Name", index = 5)
        private String supplierName;

        @ExcelProperty(value = "Description", index = 6)
        private String description;

        @ExcelProperty(value = "Active", index = 7)
        private Boolean active;
    }
}
