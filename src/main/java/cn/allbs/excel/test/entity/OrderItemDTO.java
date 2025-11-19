package cn.allbs.excel.test.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

	@ExcelProperty("Order Number")
	private String orderNo;

	@ExcelProperty("Product Name")
	private String productName;

	@ExcelProperty("Product Code")
	private String productCode;

	@ExcelProperty("Quantity")
	private Integer quantity;

	@ExcelProperty("Unit Price")
	@NumberFormat("#,##0.00")
	private BigDecimal unitPrice;

	@ExcelProperty("Subtotal")
	@NumberFormat("#,##0.00")
	private BigDecimal subtotal;

}
