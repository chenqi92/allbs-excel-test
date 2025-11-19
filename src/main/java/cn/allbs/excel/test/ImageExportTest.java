package cn.allbs.excel.test;

import cn.allbs.excel.handle.ImageWriteHandler;
import cn.allbs.excel.test.entity.ProductWithImageDTO;
import com.alibaba.excel.EasyExcel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 图片导出功能测试
 *
 * @author ChenQi
 * @since 2025-11-19
 */
public class ImageExportTest {

	private static final String TEST_IMAGES_DIR = "test_images";

	public static void main(String[] args) throws IOException {
		System.out.println("========================================");
		System.out.println("图片导出功能测试");
		System.out.println("========================================");

		// 1. 创建测试图片目录
		File imagesDir = new File(TEST_IMAGES_DIR);
		if (!imagesDir.exists()) {
			imagesDir.mkdirs();
			System.out.println("创建测试图片目录: " + imagesDir.getAbsolutePath());
		}

		// 2. 生成测试图片
		System.out.println();
		System.out.println("生成测试图片...");
		generateTestImages(imagesDir);

		// 3. 创建测试数据
		System.out.println();
		System.out.println("创建测试数据...");
		List<ProductWithImageDTO> dataList = createTestData(imagesDir);
		System.out.println("✓ 创建了 " + dataList.size() + " 条测试数据");

		// 4. 导出到Excel
		String fileName = "product_with_images_test.xlsx";
		File file = new File(fileName);

		try {
			System.out.println();
			System.out.println("开始导出到文件: " + file.getAbsolutePath());

			EasyExcel.write(file, ProductWithImageDTO.class)
					.registerWriteHandler(new ImageWriteHandler(ProductWithImageDTO.class)).sheet("商品图片示例")
					.doWrite(dataList);

			System.out.println();
			System.out.println("✓ 导出成功！文件大小: " + file.length() + " 字节");

			if (file.length() == 0) {
				System.err.println("✗ 错误：文件大小为 0！");
			}
			else {
				System.out.println("✓ 文件内容正常");
				System.out.println();
				System.out.println("请打开文件检查图片效果：" + file.getAbsolutePath());
				System.out.println();
				System.out.println("预期效果：");
				System.out.println("----------------------------------------");
				System.out.println("1. 每个商品都有一张主图（80x80像素）");
				System.out.println("2. 商品图集列显示多张小图（60x60像素）");
				System.out.println("3. 二维码列显示二维码图片（100x100像素）");
				System.out.println("4. 行高已自动调整以适应图片");
				System.out.println("5. 包含多张图片的单元格会水平排列");
				System.out.println("----------------------------------------");
			}

		}
		catch (Exception e) {
			System.err.println("✗ 导出失败！");
			e.printStackTrace();
		}

		System.out.println();
		System.out.println("========================================");
		System.out.println("测试完成！");
		System.out.println("========================================");
	}

	/**
	 * 生成测试图片
	 */
	private static void generateTestImages(File imagesDir) throws IOException {
		String[] colors = { "red", "blue", "green", "yellow", "orange", "purple" };
		Color[] colorValues = { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE,
				new Color(128, 0, 128) };

		for (int i = 0; i < colors.length; i++) {
			String colorName = colors[i];
			Color color = colorValues[i];

			// 生成主图 (80x80)
			File mainImageFile = new File(imagesDir, colorName + "_main.png");
			generateColorImage(mainImageFile, color, 80, 80, "主图");

			// 生成缩略图 (60x60)
			for (int j = 1; j <= 3; j++) {
				File thumbnailFile = new File(imagesDir, colorName + "_thumb_" + j + ".png");
				generateColorImage(thumbnailFile, color, 60, 60, "图" + j);
			}
		}

		System.out.println("✓ 生成了测试图片");
	}

	/**
	 * 生成指定颜色的测试图片
	 */
	private static void generateColorImage(File file, Color color, int width, int height, String text)
			throws IOException {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();

		// 填充背景色
		g2d.setColor(color);
		g2d.fillRect(0, 0, width, height);

		// 绘制边框
		g2d.setColor(Color.BLACK);
		g2d.drawRect(0, 0, width - 1, height - 1);

		// 绘制文字
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Arial", Font.BOLD, 12));
		FontMetrics fm = g2d.getFontMetrics();
		int textWidth = fm.stringWidth(text);
		int textHeight = fm.getHeight();
		g2d.drawString(text, (width - textWidth) / 2, (height + textHeight / 2) / 2);

		g2d.dispose();

		ImageIO.write(image, "png", file);
	}

	/**
	 * 生成二维码图片（简化版，用彩色方块代替）
	 */
	private static byte[] generateQRCode(String text, Color color) throws IOException {
		int size = 100;
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();

		// 白色背景
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, size, size);

		// 绘制彩色方块作为二维码模拟
		g2d.setColor(color);
		int blockSize = 10;
		for (int i = 0; i < size / blockSize; i++) {
			for (int j = 0; j < size / blockSize; j++) {
				if ((i + j) % 2 == 0) {
					g2d.fillRect(i * blockSize, j * blockSize, blockSize, blockSize);
				}
			}
		}

		// 绘制文字
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Arial", Font.BOLD, 10));
		FontMetrics fm = g2d.getFontMetrics();
		int textWidth = fm.stringWidth(text);
		g2d.setColor(Color.WHITE);
		g2d.fillRect((size - textWidth) / 2 - 5, size / 2 - 10, textWidth + 10, 20);
		g2d.setColor(Color.BLACK);
		g2d.drawString(text, (size - textWidth) / 2, size / 2 + 5);

		g2d.dispose();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		return baos.toByteArray();
	}

	/**
	 * 创建测试数据
	 */
	private static List<ProductWithImageDTO> createTestData(File imagesDir) throws IOException {
		List<ProductWithImageDTO> dataList = new ArrayList<>();

		String[] colors = { "red", "blue", "green", "yellow", "orange", "purple" };
		String[] productNames = { "经典款T恤", "时尚牛仔裤", "运动鞋", "休闲夹克", "连衣裙", "毛衣" };
		Color[] colorValues = { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE,
				new Color(128, 0, 128) };

		for (int i = 0; i < colors.length; i++) {
			String color = colors[i];
			Color colorValue = colorValues[i];

			ProductWithImageDTO dto = new ProductWithImageDTO();
			dto.setProductId((long) (i + 1));
			dto.setProductName(productNames[i] + " - " + color.toUpperCase());
			dto.setPrice(new BigDecimal("99.99").add(new BigDecimal(i * 10)));

			// 设置主图路径
			dto.setMainImage(new File(imagesDir, color + "_main.png").getAbsolutePath());

			// 设置图片列表
			List<String> imageList = new ArrayList<>();
			for (int j = 1; j <= 3; j++) {
				imageList.add(new File(imagesDir, color + "_thumb_" + j + ".png").getAbsolutePath());
			}
			dto.setImageList(imageList);

			// 生成二维码
			dto.setQrCode(generateQRCode("P" + (i + 1), colorValue));

			dto.setStock(100 + i * 50);
			dto.setDescription("这是一件" + productNames[i] + "，颜色：" + color + "，质量上乘，价格实惠！");

			dataList.add(dto);
		}

		return dataList;
	}

}
