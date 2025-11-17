package cn.allbs.excel.test.entity.flatten;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 奖项实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Award {

    @ExcelProperty("奖项名称")
    private String awardName;

    @ExcelProperty("获奖时间")
    private String awardDate;
}
