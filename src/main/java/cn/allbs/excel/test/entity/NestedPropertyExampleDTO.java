package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.NestedProperty;
import cn.allbs.excel.convert.NestedObjectConverter;
import cn.allbs.excel.test.entity.nested.Department;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @NestedProperty 注解示例
 * 演示如何从嵌套对象、集合、Map 中提取字段值
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NestedPropertyExampleDTO {

    @ExcelProperty("员工ID")
    private Long id;

    @ExcelProperty("员工姓名")
    private String name;

    // ==================== 单层嵌套对象 ====================

    @ExcelProperty(value = "部门名称", converter = NestedObjectConverter.class)
    @NestedProperty("name")
    private Department department;

    @ExcelProperty(value = "部门编码", converter = NestedObjectConverter.class)
    @NestedProperty(value = "code", nullValue = "未分配")
    private Department department2;

    // ==================== 多层嵌套对象 ====================

    @ExcelProperty(value = "直属领导", converter = NestedObjectConverter.class)
    @NestedProperty(value = "leader.name", nullValue = "暂无")
    private Department department3;

    @ExcelProperty(value = "领导电话", converter = NestedObjectConverter.class)
    @NestedProperty(value = "leader.phone", nullValue = "-")
    private Department department4;

    // ==================== 集合类型 ====================

    // 技能列表
    private List<String> skills;

    @ExcelProperty(value = "主要技能", converter = NestedObjectConverter.class)
    @NestedProperty(value = "skills[0]", nullValue = "无")
    private List<String> mainSkill;

    @ExcelProperty(value = "所有技能", converter = NestedObjectConverter.class)
    @NestedProperty(value = "skills[*]", separator = ",", maxJoinSize = 5)
    private List<String> allSkills;

    // ==================== Map 类型 ====================

    private Map<String, Object> properties;

    @ExcelProperty(value = "工作城市", converter = NestedObjectConverter.class)
    @NestedProperty(value = "properties[city]", nullValue = "-")
    private Map<String, Object> city;

    @ExcelProperty(value = "入职年份", converter = NestedObjectConverter.class)
    @NestedProperty(value = "properties[joinYear]", nullValue = "-")
    private Map<String, Object> joinYear;
}
