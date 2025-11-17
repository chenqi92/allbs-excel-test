package cn.allbs.excel.test.entity.nested;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门实体 - 用于嵌套对象测试
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @ExcelProperty("部门编码")
    private String code;

    @ExcelProperty("部门名称")
    private String name;

    @ExcelProperty("部门类型")
    private String type;

    private String internalId;  // 无 @ExcelProperty，不会被导出

    private Leader leader;
}
