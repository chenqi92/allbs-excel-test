package cn.allbs.excel.test.entity.flatten;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 订单明细实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @ExcelProperty("商品名称")
    private String productName;

    @ExcelProperty("SKU")
    private String sku;

    @ExcelProperty("数量")
    private Integer quantity;

    @ExcelProperty("单价")
    private BigDecimal price;

    @ExcelProperty("小计")
    private BigDecimal subtotal;
}
