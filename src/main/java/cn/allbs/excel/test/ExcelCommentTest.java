package cn.allbs.excel.test;

import cn.allbs.excel.annotation.ExcelComment;
import cn.allbs.excel.handle.ExcelCommentWriteHandler;
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
 * Excel Comment Test
 *
 * Test Excel cell comments/notes feature
 */
public class ExcelCommentTest {

    public static void main(String[] args) {
        String outputPath = "E:/temp/excel_comment_test.xlsx";

        // Generate test data
        List<ProductData> data = generateProductData();

        // Export with comments
        EasyExcel.write(new File(outputPath), ProductData.class)
            .registerWriteHandler(new ExcelCommentWriteHandler(ProductData.class))
            .sheet("Product Catalog")
            .doWrite(data);

        System.out.println("Excel comment test completed!");
        System.out.println("Output file: " + outputPath);
        System.out.println("\nComments added:");
        System.out.println("1. Product ID Column:");
        System.out.println("   - Header: Unique identifier for each product");
        System.out.println("2. Product Name Column:");
        System.out.println("   - Header: Official product name");
        System.out.println("   - Data: Shows product name in comment");
        System.out.println("3. Price Column:");
        System.out.println("   - Header: Price in USD");
        System.out.println("   - Larger comment box (width=4, height=3)");
        System.out.println("4. Stock Column:");
        System.out.println("   - Header: Current inventory");
        System.out.println("   - Always visible comment");
        System.out.println("\nHover over cells with red triangles to see comments!");
    }

    private static List<ProductData> generateProductData() {
        List<ProductData> data = new ArrayList<>();

        data.add(new ProductData("P001", "Laptop Pro 15", new BigDecimal("1299.99"), 50, "Electronics"));
        data.add(new ProductData("P002", "Wireless Mouse", new BigDecimal("29.99"), 200, "Accessories"));
        data.add(new ProductData("P003", "Mechanical Keyboard", new BigDecimal("89.99"), 150, "Accessories"));
        data.add(new ProductData("P004", "27\" Monitor", new BigDecimal("399.99"), 75, "Electronics"));
        data.add(new ProductData("P005", "USB-C Hub", new BigDecimal("49.99"), 300, "Accessories"));
        data.add(new ProductData("P006", "Webcam HD", new BigDecimal("79.99"), 100, "Electronics"));
        data.add(new ProductData("P007", "Desk Lamp LED", new BigDecimal("39.99"), 120, "Office"));
        data.add(new ProductData("P008", "Ergonomic Chair", new BigDecimal("299.99"), 45, "Furniture"));
        data.add(new ProductData("P009", "Standing Desk", new BigDecimal("499.99"), 30, "Furniture"));
        data.add(new ProductData("P010", "Noise-Canceling Headphones", new BigDecimal("199.99"), 80, "Electronics"));

        return data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductData {

        @ExcelProperty(value = "Product ID", index = 0)
        @ExcelComment(
            headerComment = "Unique identifier for each product\nFormat: P + 3 digits",
            author = "System Admin"
        )
        private String productId;

        @ExcelProperty(value = "Product Name", index = 1)
        @ExcelComment(
            headerComment = "Official product name\nMust be unique",
            author = "Product Manager"
        )
        private String productName;

        @ExcelProperty(value = "Price (USD)", index = 2)
        @ExcelComment(
            headerComment = "Retail price in US Dollars\nIncludes taxes",
            author = "Finance Dept",
            width = 4,
            height = 3
        )
        private BigDecimal price;

        @ExcelProperty(value = "Stock Quantity", index = 3)
        @ExcelComment(
            headerComment = "Current inventory level\nUpdated daily at midnight",
            author = "Warehouse Manager",
            visible = false,
            width = 3,
            height = 2
        )
        private Integer stockQuantity;

        @ExcelProperty(value = "Category", index = 4)
        @ExcelComment(
            headerComment = "Product category\nMain classification",
            author = "Catalog Team"
        )
        private String category;
    }
}
