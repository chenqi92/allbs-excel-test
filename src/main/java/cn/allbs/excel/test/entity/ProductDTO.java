package cn.allbs.excel.test.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 产品DTO - 用于测试列宽、行高等样式功能
 */
@Data
@ContentRowHeight(25)
public class ProductDTO {

    @ExcelProperty(value = "产品ID", index = 0)
    @ColumnWidth(10)
    private Long id;

    @ExcelProperty(value = "产品名称", index = 1)
    @ColumnWidth(20)
    private String name;

    @ExcelProperty(value = "产品分类", index = 2)
    @ColumnWidth(15)
    private String category;

    @ExcelProperty(value = "价格", index = 3)
    @ColumnWidth(15)
    private BigDecimal price;

    @ExcelProperty(value = "库存", index = 4)
    @ColumnWidth(10)
    private Integer stock;

    @ExcelProperty(value = "产品描述", index = 5)
    @ColumnWidth(50)
    private String description;
}
