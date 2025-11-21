package cn.allbs.excel.test.controller;

import cn.allbs.excel.annotation.ExportExcel;
import cn.allbs.excel.annotation.ImportExcel;
import cn.allbs.excel.annotation.Sheet;
import cn.allbs.excel.test.entity.ProductImageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

/**
 * 图片导入导出测试控制器
 * <p>
 * 用于测试带图片的 Excel 导入导出功能，确保图片尺寸不被压缩
 * </p>
 *
 * @author ChenQi
 * @since 2025-11-21
 */
@Slf4j
@RestController
@RequestMapping("/api/image")
@CrossOrigin(origins = "*")
public class ImageImportExportController {

	/**
	 * 1. 导出带图片的 Excel（用于测试导入）
	 */
	@GetMapping("/export")
	@ExportExcel(name = "商品图片列表", sheets = @Sheet(sheetName = "商品信息", clazz = ProductImageDTO.class))
	public List<ProductImageDTO> exportWithImages() {
		List<ProductImageDTO> products = new ArrayList<>();

		// 生成示例图片数据（彩色方块）
		byte[] redImage = generateColorSquare(120, 120, 255, 0, 0); // 红色方块
		byte[] greenImage = generateColorSquare(80, 80, 0, 255, 0); // 绿色方块
		byte[] blueImage = generateColorSquare(100, 100, 0, 0, 255); // 蓝色方块

		// 商品1：使用 Base64 图片
		ProductImageDTO product1 = new ProductImageDTO();
		product1.setId(1L);
		product1.setName("iPhone 15 Pro");
		product1.setPrice(new BigDecimal("7999.00"));
		product1.setMainImage("data:image/png;base64," + Base64.getEncoder().encodeToString(redImage));
		product1.setThumbnail(greenImage);
		product1.setImageList(Arrays.asList("data:image/png;base64," + Base64.getEncoder().encodeToString(blueImage),
				"data:image/png;base64," + Base64.getEncoder().encodeToString(redImage)));
		product1.setStock(100);
		product1.setDescription("最新款 iPhone 15 Pro，A17 Pro 芯片");
		products.add(product1);

		// 商品2：使用字节数组
		ProductImageDTO product2 = new ProductImageDTO();
		product2.setId(2L);
		product2.setName("MacBook Pro 16");
		product2.setPrice(new BigDecimal("19999.00"));
		product2.setMainImage("data:image/png;base64," + Base64.getEncoder().encodeToString(blueImage));
		product2.setThumbnail(redImage);
		product2.setImageList(Arrays.asList("data:image/png;base64," + Base64.getEncoder().encodeToString(greenImage),
				"data:image/png;base64," + Base64.getEncoder().encodeToString(blueImage)));
		product2.setStock(50);
		product2.setDescription("16 英寸 MacBook Pro，M3 Max 芯片");
		products.add(product2);

		// 商品3：使用不同尺寸的图片
		byte[] largeImage = generateColorSquare(150, 150, 255, 165, 0); // 橙色方块
		ProductImageDTO product3 = new ProductImageDTO();
		product3.setId(3L);
		product3.setName("iPad Air");
		product3.setPrice(new BigDecimal("4799.00"));
		product3.setMainImage("data:image/png;base64," + Base64.getEncoder().encodeToString(largeImage));
		product3.setThumbnail(greenImage);
		product3.setImageList(Collections.singletonList(
				"data:image/png;base64," + Base64.getEncoder().encodeToString(redImage)));
		product3.setStock(200);
		product3.setDescription("11 英寸 iPad Air，M2 芯片");
		products.add(product3);

		log.info("Exporting {} products with images", products.size());
		return products;
	}

	/**
	 * 2. 导入带图片的 Excel
	 */
	@PostMapping("/import")
	public ResponseEntity<?> importWithImages(@ImportExcel List<ProductImageDTO> products) {

		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		result.put("message", "导入成功");
		result.put("count", products.size());

		// 处理导入的数据
		List<Map<String, Object>> productInfos = new ArrayList<>();
		for (ProductImageDTO product : products) {
			Map<String, Object> info = new HashMap<>();
			info.put("id", product.getId());
			info.put("name", product.getName());
			info.put("price", product.getPrice());
			info.put("stock", product.getStock());
			info.put("description", product.getDescription());

			// 图片信息
			info.put("hasMainImage", product.getMainImage() != null && !product.getMainImage().isEmpty());
			info.put("hasThumbnail", product.getThumbnail() != null && product.getThumbnail().length > 0);
			info.put("imageListCount",
					product.getImageList() != null ? product.getImageList().size() : 0);

			// 如果需要，可以在这里保存图片到服务器
			// saveImage(product.getMainImage(), "main_" + product.getId());
			// saveImage(product.getThumbnail(), "thumbnail_" + product.getId());

			productInfos.add(info);
		}

		result.put("products", productInfos);

		log.info("Imported {} products with images", products.size());
		return ResponseEntity.ok(result);
	}

	/**
	 * 3. 下载导入模板
	 */
	@GetMapping("/template")
	@ExportExcel(name = "商品图片导入模板", sheets = @Sheet(sheetName = "商品信息", clazz = ProductImageDTO.class))
	public List<ProductImageDTO> downloadTemplate() {
		// 返回一个示例数据
		byte[] exampleImage = generateColorSquare(120, 120, 128, 128, 128); // 灰色方块

		ProductImageDTO example = new ProductImageDTO();
		example.setId(1L);
		example.setName("示例商品");
		example.setPrice(new BigDecimal("99.99"));
		example.setMainImage("data:image/png;base64," + Base64.getEncoder().encodeToString(exampleImage));
		example.setThumbnail(exampleImage);
		example.setImageList(
				Collections.singletonList("data:image/png;base64," + Base64.getEncoder().encodeToString(exampleImage)));
		example.setStock(100);
		example.setDescription("这是一个示例商品，用于演示图片导入导出功能");

		return Collections.singletonList(example);
	}

	/**
	 * 生成一个彩色方块图片（PNG 格式）
	 *
	 * @param width  宽度
	 * @param height 高度
	 * @param r      红色分量 (0-255)
	 * @param g      绿色分量 (0-255)
	 * @param b      蓝色分量 (0-255)
	 * @return PNG 图片字节数组
	 */
	private byte[] generateColorSquare(int width, int height, int r, int g, int b) {
		try {
			// 创建简单的 PNG 图片（使用 Java AWT）
			java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(width, height,
					java.awt.image.BufferedImage.TYPE_INT_RGB);
			java.awt.Graphics2D graphics = image.createGraphics();

			// 填充背景色
			graphics.setColor(new java.awt.Color(r, g, b));
			graphics.fillRect(0, 0, width, height);

			// 绘制边框
			graphics.setColor(java.awt.Color.BLACK);
			graphics.drawRect(0, 0, width - 1, height - 1);

			// 绘制尺寸文字
			graphics.setColor(java.awt.Color.WHITE);
			graphics.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
			String text = width + "x" + height;
			java.awt.FontMetrics fm = graphics.getFontMetrics();
			int textWidth = fm.stringWidth(text);
			int textHeight = fm.getHeight();
			graphics.drawString(text, (width - textWidth) / 2, (height + textHeight) / 2 - 4);

			graphics.dispose();

			// 转换为 PNG 字节数组
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			javax.imageio.ImageIO.write(image, "png", baos);
			return baos.toByteArray();
		}
		catch (Exception e) {
			log.error("Failed to generate color square: {}", e.getMessage(), e);
			return new byte[0];
		}
	}

	/**
	 * 保存图片到服务器（示例方法）
	 *
	 * @param imageData 图片数据（Base64 或字节数组）
	 * @param filename  文件名
	 */
	@SuppressWarnings("unused")
	private void saveImage(Object imageData, String filename) {
		// 实际应用中，可以将图片保存到服务器或云存储
		// 这里只是示例，不做实际保存
		log.debug("Would save image: {}", filename);
	}

}
