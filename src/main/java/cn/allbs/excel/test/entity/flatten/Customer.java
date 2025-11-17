package cn.allbs.excel.test.entity.flatten;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("城市")
    private String city;
}
