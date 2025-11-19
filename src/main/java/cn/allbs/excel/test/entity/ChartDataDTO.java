package cn.allbs.excel.test.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Chart Data DTO
 * <p>
 * Test DTO for chart feature
 * </p>
 *
 * @author ChenQi
 * @since 2025-11-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartDataDTO {

	@ExcelProperty(value = "Month", index = 0)
	private String month;

	@ExcelProperty(value = "Sales", index = 1)
	private BigDecimal sales;

	@ExcelProperty(value = "Cost", index = 2)
	private BigDecimal cost;

	@ExcelProperty(value = "Profit", index = 3)
	private BigDecimal profit;

	@ExcelProperty(value = "Growth Rate (%)", index = 4)
	private Double growthRate;

	@ExcelProperty(value = "Units Sold", index = 5)
	private Integer unitsSold;
}
