package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.ExcelMerge;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 员工DTO - 用于测试合并单元格功能
 */
@Data
public class EmployeeDTO {

    @ExcelProperty(value = "部门", index = 0)
    @ExcelMerge  // 标记需要合并的字段
    private String department;

    @ExcelProperty(value = "姓名", index = 1)
    @ExcelMerge(dependOn = "department")  // 依赖部门列
    private String name;

    @ExcelProperty(value = "职位", index = 2)
    @ExcelMerge(dependOn = "name")  // 依赖姓名列
    private String position;

    @ExcelProperty(value = "工资", index = 3)
    private BigDecimal salary;

    @ExcelProperty(value = "入职日期", index = 4)
    private String joinDate;
}
