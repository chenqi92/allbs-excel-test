package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.ExcelFormula;
import cn.allbs.excel.annotation.ExcelSheetStyle;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Formula Data DTO
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
    @ExcelFormula(value = "=B{row}*C{row}", enabled = true)
    private BigDecimal totalPrice;

    @ExcelProperty(value = "Tax (10%)", index = 4)
    @ExcelFormula(value = "=D{row}*0.1", enabled = true)
    private BigDecimal taxAmount;

    @ExcelProperty(value = "Final Amount", index = 5)
    @ExcelFormula(value = "=D{row}+E{row}", enabled = true)
    private BigDecimal finalAmount;
}
