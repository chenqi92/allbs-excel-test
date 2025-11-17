package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.RelatedSheet;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 多 Sheet 关联导出 - 订单主表
 *
 * @author ChenQi
 * @since 2025-11-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultiSheetOrderDTO {

	/**
	 * 订单号
	 */
	@ExcelProperty(value = "订单号", index = 0)
	private String orderNo;

	/**
	 * 客户名称
	 */
	@ExcelProperty(value = "客户名称", index = 1)
	private String customerName;

	/**
	 * 订单金额
	 */
	@ExcelProperty(value = "订单金额", index = 2)
	private BigDecimal totalAmount;

	/**
	 * 订单状态
	 */
	@ExcelProperty(value = "订单状态", index = 3)
	private String status;

	/**
	 * 创建时间
	 */
	@ExcelProperty(value = "创建时间", index = 4)
	private LocalDateTime createTime;

	/**
	 * 明细数量
	 */
	@ExcelProperty(value = "明细数量", index = 5)
	private Integer itemCount;

	/**
	 * 订单明细（导出到关联 Sheet）
	 */
	@RelatedSheet(sheetName = "订单明细", relationKey = "orderNo", dataType = MultiSheetOrderItemDTO.class, createHyperlink = true, hyperlinkText = "查看明细")
	private List<MultiSheetOrderItemDTO> items;

}
