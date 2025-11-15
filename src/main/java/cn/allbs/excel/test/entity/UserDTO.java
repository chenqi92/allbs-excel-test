package cn.allbs.excel.test.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * 用户DTO - 用于测试基本导入导出功能
 */
@Data
public class UserDTO {

    @ExcelProperty(value = "用户ID", index = 0)
    @NotNull(message = "用户ID不能为空")
    private Long id;

    @ExcelProperty(value = "用户名", index = 1)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度必须在2-20之间")
    private String username;

    @ExcelProperty(value = "邮箱", index = 2)
    @Email(message = "邮箱格式不正确")
    private String email;

    @ExcelProperty(value = "创建时间", index = 3)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ExcelProperty(value = "年龄", index = 4)
    private Integer age;

    @ExcelProperty(value = "状态", index = 5)
    private String status;
}
