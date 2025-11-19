package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.RelatedSheet;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单DTO - 用于测试多Sheet导出
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    /**
     * 订单明细列表（关联到 "订单明细" Sheet）
     */
    @ExcelProperty(value = "查看明细", index = 6)
    @RelatedSheet(sheetName = "订单明细", relationKey = "orderNo", dataType = OrderItemDTO.class, createHyperlink = true)
    private List<OrderItemDTO> items;
}
