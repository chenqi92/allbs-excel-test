package cn.allbs.excel.test.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单DTO - 用于测试多Sheet导出
 */
@Data
public class OrderDTO {

    @ExcelProperty(value = "订单号", index = 0)
    private String orderNo;

    @ExcelProperty(value = "客户姓名", index = 1)
    private String customerName;

    @ExcelProperty(value = "订单金额", index = 2)
    @NumberFormat("#,##0.00")
    private BigDecimal amount;

    @ExcelProperty(value = "订单状态", index = 3)
    private String status;

    @ExcelProperty(value = "创建时间", index = 4)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ExcelProperty(value = "备注", index = 5)
    private String remark;
}
