package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.Desensitize;
import cn.allbs.excel.annotation.ExcelDict;
import cn.allbs.excel.convert.DesensitizeConverter;
import cn.allbs.excel.convert.DictConverter;
import cn.allbs.excel.enums.DesensitizeType;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 敏感信息用户DTO - 用于测试数据脱敏和字典转换功能
 */
@Data
public class SensitiveUserDTO {

    @ExcelProperty(value = "用户ID", index = 0)
    private Long id;

    @ExcelProperty(value = "姓名", index = 1, converter = DesensitizeConverter.class)
    @Desensitize(type = DesensitizeType.NAME)
    private String name;

    @ExcelProperty(value = "手机号", index = 2, converter = DesensitizeConverter.class)
    @Desensitize(type = DesensitizeType.MOBILE_PHONE)
    private String phone;

    @ExcelProperty(value = "身份证", index = 3, converter = DesensitizeConverter.class)
    @Desensitize(type = DesensitizeType.ID_CARD)
    private String idCard;

    @ExcelProperty(value = "邮箱", index = 4, converter = DesensitizeConverter.class)
    @Desensitize(type = DesensitizeType.EMAIL)
    private String email;

    @ExcelProperty(value = "银行卡", index = 5, converter = DesensitizeConverter.class)
    @Desensitize(type = DesensitizeType.BANK_CARD)
    private String bankCard;

    @ExcelProperty(value = "地址", index = 6, converter = DesensitizeConverter.class)
    @Desensitize(type = DesensitizeType.ADDRESS)
    private String address;

    @ExcelProperty(value = "性别", index = 7, converter = DictConverter.class)
    @ExcelDict(dictType = "sys_user_sex")
    private String sex;

    @ExcelProperty(value = "状态", index = 8, converter = DictConverter.class)
    @ExcelDict(dictType = "sys_user_status")
    private String status;
}
