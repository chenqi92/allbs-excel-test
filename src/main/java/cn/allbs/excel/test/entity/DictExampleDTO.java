package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.ExcelDict;
import cn.allbs.excel.convert.DictConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典转换示例DTO - 专门用于演示字典转换功能
 */
@Data
public class DictExampleDTO {

    @ExcelProperty(value = "用户ID", index = 0)
    private Long id;

    @ExcelProperty(value = "用户名", index = 1)
    private String username;

    /**
     * 性别字典转换示例
     * 导出时: 0 → 女, 1 → 男, 2 → 未知
     * 导入时: 女 → 0, 男 → 1, 未知 → 2
     */
    @ExcelProperty(value = "性别", index = 2, converter = DictConverter.class)
    @ExcelDict(dictType = "sys_user_sex")
    private String sex;

    /**
     * 状态字典转换示例
     * 导出时: 0 → 正常, 1 → 禁用, 2 → 锁定
     * 导入时: 正常 → 0, 禁用 → 1, 锁定 → 2
     */
    @ExcelProperty(value = "状态", index = 3, converter = DictConverter.class)
    @ExcelDict(dictType = "sys_user_status")
    private String status;

    @ExcelProperty(value = "部门", index = 4)
    private String department;

    @ExcelProperty(value = "创建时间", index = 5)
    private LocalDateTime createTime;

    @ExcelProperty(value = "备注", index = 6)
    private String remark;
}
