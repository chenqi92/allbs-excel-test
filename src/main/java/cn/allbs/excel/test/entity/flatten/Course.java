package cn.allbs.excel.test.entity.flatten;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @ExcelProperty("课程名")
    private String courseName;

    @ExcelProperty("成绩")
    private Integer score;
}
