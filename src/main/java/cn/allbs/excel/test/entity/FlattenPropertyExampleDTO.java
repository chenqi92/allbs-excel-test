package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.FlattenProperty;
import cn.allbs.excel.test.entity.nested.Department;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @FlattenProperty 注解示例
 * 演示如何自动展开嵌套对象的所有字段
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlattenPropertyExampleDTO {

    @ExcelProperty("员工ID")
    private Long id;

    @ExcelProperty("员工姓名")
    private String name;

    @ExcelProperty("年龄")
    private Integer age;

    // 自动展开部门的所有 @ExcelProperty 字段
    @FlattenProperty(prefix = "部门-")
    private Department department;

    // 自动展开上级部门的所有字段，使用不同的前缀避免冲突
    @FlattenProperty(prefix = "上级部门-")
    private Department parentDept;

    // 递归展开（会自动展开 department 内部的 leader 对象）
    @FlattenProperty(prefix = "主管-", recursive = true, maxDepth = 2)
    private Department managerDept;
}
