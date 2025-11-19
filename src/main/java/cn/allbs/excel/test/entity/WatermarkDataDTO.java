package cn.allbs.excel.test.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Watermark Data DTO
 * <p>
 * Test DTO for watermark feature
 * </p>
 *
 * @author ChenQi
 * @since 2025-11-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatermarkDataDTO {

	@ExcelProperty(value = "Document ID", index = 0)
	private String documentId;

	@ExcelProperty(value = "Document Title", index = 1)
	private String title;

	@ExcelProperty(value = "Category", index = 2)
	private String category;

	@ExcelProperty(value = "Author", index = 3)
	private String author;

	@ExcelProperty(value = "Department", index = 4)
	private String department;

	@ExcelProperty(value = "Creation Date", index = 5)
	private String creationDate;

	@ExcelProperty(value = "Confidential Level", index = 6)
	private String confidentialLevel;

	@ExcelProperty(value = "File Size (KB)", index = 7)
	private Integer fileSize;

	@ExcelProperty(value = "Budget Amount", index = 8)
	private BigDecimal budgetAmount;

	@ExcelProperty(value = "Status", index = 9)
	private String status;
}
