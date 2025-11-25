package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.ExcelImage;
import cn.allbs.excel.convert.ImageListConverter;
import cn.allbs.excel.convert.ImageStringConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品图片导入导出测试 DTO
 * <p>
 * 用于测试图片的导入导出功能，确保图片尺寸不被压缩
 * </p>
 *
 * @author ChenQi
 * @since 2025-11-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDTO {

	@ExcelProperty("商品ID")
	private Long id;

	@ExcelProperty("商品名称")
	private String name;

	@ExcelProperty("商品价格")
	private BigDecimal price;

	/**
	 * 商品主图（单张图片）
	 * <p>
	 * 导出时：支持 URL、本地路径、Base64
	 * 导入时：读取为 Base64 字符串
	 * </p>
	 */
	@ExcelProperty(value = "商品主图", converter = ImageStringConverter.class)
	@ExcelImage(width = 120, height = 120)
	private String mainImage;

	/**
	 * 商品缩略图（字节数组）
	 * <p>
	 * 导出时：直接使用字节数组
	 * 导入时：读取为字节数组
	 * </p>
	 */
	@ExcelProperty("商品缩略图")
	@ExcelImage(width = 80, height = 80, type = ExcelImage.ImageType.BYTES)
	private byte[] thumbnail;

	/**
	 * 商品图集（多张图片）
	 * <p>
	 * 导出时：支持多张图片水平排列
	 * 导入时：读取为 Base64 字符串列表
	 * </p>
	 */
	@ExcelProperty(value = "商品图集", converter = ImageListConverter.class)
	@ExcelImage(width = 100, height = 100)
	private List<String> imageList;

	@ExcelProperty("库存数量")
	private Integer stock;

	@ExcelProperty("商品描述")
	private String description;

}
