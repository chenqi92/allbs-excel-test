package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.ExcelSheetStyle;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Formula Data DTO
 * Note: Formula calculations removed - values will be exported as data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ExcelSheetStyle(
    freezeRow = 1,
    autoFilter = true,
    defaultColumnWidth = 15
)
public class FormulaDataDTO {

    @ExcelProperty(value = "Product Name", index = 0)
    private String productName;

    @ExcelProperty(value = "Unit Price", index = 1)
    private BigDecimal unitPrice;

    @ExcelProperty(value = "Quantity", index = 2)
    private Integer quantity;

    @ExcelProperty(value = "Total Price", index = 3)
    private BigDecimal totalPrice;

    @ExcelProperty(value = "Tax (10%)", index = 4)
    private BigDecimal taxAmount;

    @ExcelProperty(value = "Final Amount", index = 5)
    private BigDecimal finalAmount;
}
