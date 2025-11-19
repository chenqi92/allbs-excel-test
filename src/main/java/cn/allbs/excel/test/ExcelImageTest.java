package cn.allbs.excel.test;

import cn.allbs.excel.handle.ImageWriteHandler;
import cn.allbs.excel.test.entity.ProductWithImageDTO;
import cn.allbs.excel.test.service.TestDataService;
import com.alibaba.excel.EasyExcel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

/**
 * Excel Image Test
 * <p>
 * Tests Excel image export feature
 * </p>
 *
 * @author ChenQi
 * @since 2025-11-19
 */
public class ExcelImageTest {

	public static void main(String[] args) throws Exception {
		System.out.println("=".repeat(80));
		System.out.println("Excel Image Test");
		System.out.println("=".repeat(80));

		TestDataService testDataService = new TestDataService();
		List<ProductWithImageDTO> data = testDataService.generateProductWithImageData(10);

		// 确保输出目录存在
		File outputDir = new File("E:/temp");
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}

		// Test: Image export
		testImageExport(data);

		System.out.println("\n" + "=".repeat(80));
		System.out.println("All image tests completed successfully!");
		System.out.println("=".repeat(80));
		System.out.println("\n💡 Tips:");
		System.out.println("- Open the generated Excel file to see the images");
		System.out.println("- Images are embedded in Excel cells");
		System.out.println("- Support both URL and byte[] image formats");
	}

	/**
	 * Test image export
	 */
	private static void testImageExport(List<ProductWithImageDTO> data) {
		System.out.println("\n1. Image Export Test:");
		System.out.println("   " + "-".repeat(70));

		String fileName = "E:/temp/products_with_images.xlsx";

		EasyExcel.write(fileName, ProductWithImageDTO.class)
			.sheet("商品列表")
			.registerWriteHandler(new ImageWriteHandler())
			.doWrite(data);

		System.out.println("   ✓ Created file: " + fileName);
		System.out.println("   ✓ Exported " + data.size() + " products with images");
		System.out.println("   ✓ Each product has main image and thumbnail");
	}

	/**
	 * Create a simple test image (placeholder)
	 */
	public static byte[] createTestImage(int width, int height, String text) {
		try {
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = image.createGraphics();

			// Fill background
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.fillRect(0, 0, width, height);

			// Draw border
			g2d.setColor(Color.DARK_GRAY);
			g2d.drawRect(0, 0, width - 1, height - 1);

			// Draw text
			g2d.setColor(Color.BLACK);
			g2d.setFont(new Font("Arial", Font.PLAIN, 12));
			FontMetrics fm = g2d.getFontMetrics();
			int textWidth = fm.stringWidth(text);
			int textHeight = fm.getHeight();
			int x = (width - textWidth) / 2;
			int y = (height - textHeight) / 2 + fm.getAscent();
			g2d.drawString(text, x, y);

			g2d.dispose();

			// Convert to byte array
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", baos);
			return baos.toByteArray();
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to create test image", e);
		}
	}

}
