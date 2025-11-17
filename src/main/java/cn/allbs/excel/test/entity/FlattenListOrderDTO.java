package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.FlattenList;
import cn.allbs.excel.annotation.FlattenProperty;
import cn.allbs.excel.test.entity.flatten.Customer;
import cn.allbs.excel.test.entity.flatten.OrderItem;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @FlattenList 注解示例 - 订单导出
 * 演示如何将 List 展开为多行，并自动合并单元格
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlattenListOrderDTO {

    @ExcelProperty("订单号")
    private String orderNo;

    @ExcelProperty("下单时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;

    @ExcelProperty("订单状态")
    private String status;

    // 使用 @FlattenProperty 自动展开客户信息
    @FlattenProperty(prefix = "客户-")
    private Customer customer;

    // 使用 @FlattenList 自动展开订单明细
    @FlattenList(prefix = "商品-")
    private List<OrderItem> items;
}
