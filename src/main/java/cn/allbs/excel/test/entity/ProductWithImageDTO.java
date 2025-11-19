package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.ExcelImage;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 带图片的商品信息 DTO
 * 演示图片导出功能
 *
 * @author ChenQi
 * @since 2025-11-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithImageDTO {

	@ExcelProperty("商品ID")
	private Long productId;

	@ExcelProperty("商品名称")
	private String productName;

	@ExcelProperty("商品价格")
	private BigDecimal price;

	/**
	 * 单张主图（支持本地文件路径或URL）
	 */
	@ExcelProperty("商品主图")
	@ExcelImage(width = 80, height = 80)
	private String mainImage;

	/**
	 * 商品图片列表（支持多张图片）
	 */
	@ExcelProperty(value = "商品图集", converter = cn.allbs.excel.convert.ImageListConverter.class)
	@ExcelImage(width = 60, height = 60)
	private List<String> imageList;

	/**
	 * 二维码（字节数组形式）
	 */
	@ExcelProperty("商品二维码")
	@ExcelImage(width = 100, height = 100, type = ExcelImage.ImageType.BYTES)
	private byte[] qrCode;

	@ExcelProperty("库存数量")
	private Integer stock;

	@ExcelProperty("商品描述")
	private String description;

}
