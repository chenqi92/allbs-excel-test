package cn.allbs.excel.test;

import cn.allbs.excel.annotation.ExcelWatermark;
import cn.allbs.excel.handle.ExcelWatermarkWriteHandler;
import cn.allbs.excel.test.entity.WatermarkDataDTO;
import cn.allbs.excel.test.service.TestDataService;
import com.alibaba.excel.EasyExcel;

import java.util.List;

/**
 * Excel Watermark Test
 * <p>
 * Tests Excel watermark feature with different configurations
 * </p>
 *
 * @author ChenQi
 * @since 2025-11-19
 */
public class ExcelWatermarkTest {

	public static void main(String[] args) {
		System.out.println("=".repeat(80));
		System.out.println("Excel Watermark Test");
		System.out.println("=".repeat(80));

		TestDataService testDataService = new TestDataService();
		List<WatermarkDataDTO> data = testDataService.generateWatermarkData(30);

		// Test 1: Confidential watermark (default settings)
		testConfidentialWatermark(data);

		// Test 2: Custom watermark with username and date
		testCustomWatermark(data);

		// Test 3: Diagonal watermark
		testDiagonalWatermark(data);

		// Test 4: Bold red watermark
		testBoldRedWatermark(data);

		System.out.println("\n" + "=".repeat(80));
		System.out.println("All watermark tests completed successfully!");
		System.out.println("=".repeat(80));
		System.out.println("\n💡 Tips:");
		System.out.println("- Open the generated Excel files to see the watermark effects");
		System.out.println("- Watermarks are rendered as background images");
		System.out.println("- Adjust opacity and rotation for different visual effects");
		System.out.println("- Use watermarks to protect confidential documents");
	}

	/**
	 * Test confidential watermark with default settings
	 */
	private static void testConfidentialWatermark(List<WatermarkDataDTO> data) {
		System.out.println("\n1. Confidential Watermark (Default Settings):");
		System.out.println("   " + "-".repeat(70));

		String fileName = "E:/temp/confidential_document.xlsx";

		// Create watermark annotation manually for testing
		ExcelWatermark watermark = createWatermark(
			"CONFIDENTIAL",
			true,
			"Arial",
			48,
			"#D3D3D3",
			-45,
			0.3,
			200,
			150,
			0,
			0
		);

		EasyExcel.write(fileName, WatermarkDataDTO.class)
			.sheet("Confidential Data")
			.registerWriteHandler(new ExcelWatermarkWriteHandler(watermark))
			.doWrite(data);

		System.out.println("   ✓ Created file: " + fileName);
		System.out.println("   ✓ Watermark: CONFIDENTIAL");
		System.out.println("   ✓ Settings: Default (light gray, -45°, 30% opacity)");
	}

	/**
	 * Test custom watermark with username and timestamp
	 */
	private static void testCustomWatermark(List<WatermarkDataDTO> data) {
		System.out.println("\n2. Custom Watermark with Username and Date:");
		System.out.println("   " + "-".repeat(70));

		String fileName = "E:/temp/custom_watermark_document.xlsx";

		ExcelWatermark watermark = createWatermark(
			"DRAFT - ${user.name} - ${date}",
			true,
			"Arial",
			40,
			"#FFA500",
			-30,
			0.25,
			250,
			180,
			0,
			0
		);

		EasyExcel.write(fileName, WatermarkDataDTO.class)
			.sheet("Draft Document")
			.registerWriteHandler(new ExcelWatermarkWriteHandler(watermark))
			.doWrite(data);

		System.out.println("   ✓ Created file: " + fileName);
		System.out.println("   ✓ Watermark: DRAFT - [Username] - [Date]");
		System.out.println("   ✓ Settings: Orange color, -30°, 25% opacity");
	}

	/**
	 * Test diagonal watermark
	 */
	private static void testDiagonalWatermark(List<WatermarkDataDTO> data) {
		System.out.println("\n3. Diagonal Watermark:");
		System.out.println("   " + "-".repeat(70));

		String fileName = "E:/temp/diagonal_watermark_document.xlsx";

		ExcelWatermark watermark = createWatermark(
			"INTERNAL USE ONLY",
			true,
			"Arial",
			52,
			"#4169E1",
			-50,
			0.2,
			220,
			160,
			0,
			0
		);

		EasyExcel.write(fileName, WatermarkDataDTO.class)
			.sheet("Internal Data")
			.registerWriteHandler(new ExcelWatermarkWriteHandler(watermark))
			.doWrite(data);

		System.out.println("   ✓ Created file: " + fileName);
		System.out.println("   ✓ Watermark: INTERNAL USE ONLY");
		System.out.println("   ✓ Settings: Royal blue, -50°, 20% opacity");
	}

	/**
	 * Test bold red watermark
	 */
	private static void testBoldRedWatermark(List<WatermarkDataDTO> data) {
		System.out.println("\n4. Bold Red Watermark:");
		System.out.println("   " + "-".repeat(70));

		String fileName = "E:/temp/bold_red_watermark_document.xlsx";

		ExcelWatermark watermark = createWatermark(
			"DO NOT DISTRIBUTE",
			true,
			"Arial",
			56,
			"#DC143C",
			-40,
			0.4,
			230,
			170,
			0,
			0
		);

		EasyExcel.write(fileName, WatermarkDataDTO.class)
			.sheet("Restricted Data")
			.registerWriteHandler(new ExcelWatermarkWriteHandler(watermark))
			.doWrite(data);

		System.out.println("   ✓ Created file: " + fileName);
		System.out.println("   ✓ Watermark: DO NOT DISTRIBUTE");
		System.out.println("   ✓ Settings: Crimson red, -40°, 40% opacity (more visible)");
	}

	/**
	 * Helper method to create ExcelWatermark annotation
	 */
	private static ExcelWatermark createWatermark(
		String text, boolean enabled, String fontName, int fontSize,
		String color, int rotation, double opacity,
		int horizontalSpacing, int verticalSpacing,
		int startRow, int startColumn
	) {
		return new ExcelWatermark() {
			@Override
			public String text() { return text; }

			@Override
			public boolean enabled() { return enabled; }

			@Override
			public String fontName() { return fontName; }

			@Override
			public int fontSize() { return fontSize; }

			@Override
			public String color() { return color; }

			@Override
			public int rotation() { return rotation; }

			@Override
			public double opacity() { return opacity; }

			@Override
			public int horizontalSpacing() { return horizontalSpacing; }

			@Override
			public int verticalSpacing() { return verticalSpacing; }

			@Override
			public int startRow() { return startRow; }

			@Override
			public int startColumn() { return startColumn; }

			@Override
			public Class<? extends java.lang.annotation.Annotation> annotationType() {
				return ExcelWatermark.class;
			}
		};
	}
}
