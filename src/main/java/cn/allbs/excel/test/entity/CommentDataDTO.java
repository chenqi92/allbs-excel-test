package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.ExcelComment;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Comment Data DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDataDTO {

    @ExcelProperty(value = "Product ID", index = 0)
    @ExcelComment(
        headerComment = "Unique product identifier\nFormat: P + 4 digits",
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

    @ExcelProperty(value = "Stock", index = 3)
    @ExcelComment(
        headerComment = "Current inventory\nUpdated daily",
        author = "Warehouse",
        visible = false
    )
    private Integer stock;
}
