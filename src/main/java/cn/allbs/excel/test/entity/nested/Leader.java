package cn.allbs.excel.test.entity.nested;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 领导实体 - 用于多层嵌套测试
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Leader {

    @ExcelProperty("领导姓名")
    private String name;

    @ExcelProperty("职位")
    private String position;

    @ExcelProperty("手机号")
    private String phone;
}
