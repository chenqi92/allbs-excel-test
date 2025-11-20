package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.ExcelLine;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 行号测试DTO - 用于测试@ExcelLine注解功能
 *
 * @ExcelLine 注解会自动为导出的每一行数据填充行号
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RowNumberDTO {

    /**
     * Excel行号 (自动填充,从1开始)
     */
    @ExcelLine
    @ExcelProperty(value = "行号", index = 0)
    private Long rowNum;

    @ExcelProperty(value = "产品名称", index = 1)
    private String productName;

    @ExcelProperty(value = "产品代码", index = 2)
    private String productCode;

    @ExcelProperty(value = "价格", index = 3)
    private Double price;

    @ExcelProperty(value = "库存", index = 4)
    private Integer stock;

    @ExcelProperty(value = "分类", index = 5)
    private String category;

    /**
     * 构造方法(不包含rowNum,rowNum由@ExcelLine自动填充)
     */
    public RowNumberDTO(String productName, String productCode, Double price, Integer stock, String category) {
        this.productName = productName;
        this.productCode = productCode;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }
}
