package cn.allbs.excel.test.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 多 Sheet 关联导出 - 订单明细
 *
 * @author ChenQi
 * @since 2025-11-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultiSheetOrderItemDTO {

	/**
	 * 订单号（关联键）
	 */
	@ExcelProperty(value = "订单号", index = 0)
	private String orderNo;

	/**
	 * 明细序号
	 */
	@ExcelProperty(value = "序号", index = 1)
	private Integer itemNo;

	/**
	 * 商品名称
	 */
	@ExcelProperty(value = "商品名称", index = 2)
	private String productName;

	/**
	 * SKU
	 */
	@ExcelProperty(value = "SKU", index = 3)
	private String sku;

	/**
	 * 数量
	 */
	@ExcelProperty(value = "数量", index = 4)
	private Integer quantity;

	/**
	 * 单价
	 */
	@ExcelProperty(value = "单价", index = 5)
	private BigDecimal price;

	/**
	 * 小计
	 */
	@ExcelProperty(value = "小计", index = 6)
	private BigDecimal subtotal;

	/**
	 * 备注
	 */
	@ExcelProperty(value = "备注", index = 7)
	private String remark;

}
