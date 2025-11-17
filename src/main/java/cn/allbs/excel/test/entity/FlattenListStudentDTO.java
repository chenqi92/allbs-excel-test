package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.FlattenList;
import cn.allbs.excel.test.entity.flatten.Award;
import cn.allbs.excel.test.entity.flatten.Course;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @FlattenList 注解示例 - 学生多 List 展开
 * 演示如何处理多个 List 字段
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlattenListStudentDTO {

    @ExcelProperty("学生姓名")
    private String name;

    @ExcelProperty("学号")
    private String studentNo;

    @ExcelProperty("班级")
    private String className;

    // 多个 List - 使用 MAX_LENGTH 策略（默认）
    @FlattenList(prefix = "课程-")
    private List<Course> courses;

    @FlattenList(prefix = "奖项-")
    private List<Award> awards;
}
