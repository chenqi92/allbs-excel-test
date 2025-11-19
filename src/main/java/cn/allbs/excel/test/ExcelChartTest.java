package cn.allbs.excel.test;

import cn.allbs.excel.annotation.ExcelChart;
import cn.allbs.excel.handle.ExcelChartWriteHandler;
import cn.allbs.excel.test.entity.ChartDataDTO;
import cn.allbs.excel.test.service.TestDataService;
import com.alibaba.excel.EasyExcel;

import java.util.List;

/**
 * Excel Chart Test
 * <p>
 * Tests Excel chart feature with different chart types
 * </p>
 *
 * @author ChenQi
 * @since 2025-11-19
 */
public class ExcelChartTest {

	public static void main(String[] args) {
		System.out.println("=".repeat(80));
		System.out.println("Excel Chart Test");
		System.out.println("=".repeat(80));

		TestDataService testDataService = new TestDataService();
		List<ChartDataDTO> data = testDataService.generateChartData(12);

		// Test 1: Line chart
		testLineChart(data);

		// Test 2: Column chart
		testColumnChart(data);

		// Test 3: Bar chart
		testBarChart(data);

		// Test 4: Pie chart
		testPieChart(data);

		// Test 5: Area chart
		testAreaChart(data);

		System.out.println("\n" + "=".repeat(80));
		System.out.println("All chart tests completed successfully!");
		System.out.println("=".repeat(80));
		System.out.println("\n💡 Tips:");
		System.out.println("- Open the generated Excel files to see the charts");
		System.out.println("- Charts are interactive in Excel");
		System.out.println("- You can modify chart data and see it update automatically");
		System.out.println("- Charts support multiple data series");
	}

	/**
	 * Test line chart
	 */
	private static void testLineChart(List<ChartDataDTO> data) {
		System.out.println("\n1. Line Chart Test:");
		System.out.println("   " + "-".repeat(70));

		String fileName = "E:/temp/sales_line_chart.xlsx";

		ExcelChart chartConfig = createChart(
			"Monthly Sales Trend",
			ExcelChart.ChartType.LINE,
			"Month",
			new String[]{"Sales", "Cost", "Profit"},
			0, 11, 20, 20,
			"Month", "Amount (USD)",
			true,
			ExcelChart.LegendPosition.BOTTOM
		);

		EasyExcel.write(fileName, ChartDataDTO.class)
			.sheet("Sales Data")
			.registerWriteHandler(new ExcelChartWriteHandler(chartConfig, ChartDataDTO.class, 1, data.size()))
			.doWrite(data);

		System.out.println("   ✓ Created file: " + fileName);
		System.out.println("   ✓ Chart type: Line Chart");
		System.out.println("   ✓ Data series: Sales, Cost, Profit");
		System.out.println("   ✓ Shows monthly sales trend over 12 months");
	}

	/**
	 * Test column chart
	 */
	private static void testColumnChart(List<ChartDataDTO> data) {
		System.out.println("\n2. Column Chart Test:");
		System.out.println("   " + "-".repeat(70));

		String fileName = "E:/temp/sales_column_chart.xlsx";

		ExcelChart chartConfig = createChart(
			"Monthly Sales Comparison",
			ExcelChart.ChartType.COLUMN,
			"Month",
			new String[]{"Sales", "Cost"},
			0, 11, 20, 20,
			"Month", "Amount (USD)",
			true,
			ExcelChart.LegendPosition.TOP
		);

		EasyExcel.write(fileName, ChartDataDTO.class)
			.sheet("Sales Data")
			.registerWriteHandler(new ExcelChartWriteHandler(chartConfig, ChartDataDTO.class, 1, data.size()))
			.doWrite(data);

		System.out.println("   ✓ Created file: " + fileName);
		System.out.println("   ✓ Chart type: Column Chart");
		System.out.println("   ✓ Data series: Sales, Cost");
		System.out.println("   ✓ Compares sales and cost side by side");
	}

	/**
	 * Test bar chart
	 */
	private static void testBarChart(List<ChartDataDTO> data) {
		System.out.println("\n3. Bar Chart Test:");
		System.out.println("   " + "-".repeat(70));

		String fileName = "E:/temp/profit_bar_chart.xlsx";

		ExcelChart chartConfig = createChart(
			"Monthly Profit Analysis",
			ExcelChart.ChartType.BAR,
			"Month",
			new String[]{"Profit"},
			0, 11, 20, 20,
			"Month", "Profit (USD)",
			true,
			ExcelChart.LegendPosition.RIGHT
		);

		EasyExcel.write(fileName, ChartDataDTO.class)
			.sheet("Sales Data")
			.registerWriteHandler(new ExcelChartWriteHandler(chartConfig, ChartDataDTO.class, 1, data.size()))
			.doWrite(data);

		System.out.println("   ✓ Created file: " + fileName);
		System.out.println("   ✓ Chart type: Bar Chart (Horizontal)");
		System.out.println("   ✓ Data series: Profit");
		System.out.println("   ✓ Shows profit comparison across months");
	}

	/**
	 * Test pie chart
	 */
	private static void testPieChart(List<ChartDataDTO> data) {
		System.out.println("\n4. Pie Chart Test:");
		System.out.println("   " + "-".repeat(70));

		String fileName = "E:/temp/sales_pie_chart.xlsx";

		ExcelChart chartConfig = createChart(
			"Sales Distribution by Month",
			ExcelChart.ChartType.PIE,
			"Month",
			new String[]{"Sales"},
			0, 11, 20, 20,
			"", "",
			true,
			ExcelChart.LegendPosition.RIGHT
		);

		EasyExcel.write(fileName, ChartDataDTO.class)
			.sheet("Sales Data")
			.registerWriteHandler(new ExcelChartWriteHandler(chartConfig, ChartDataDTO.class, 1, data.size()))
			.doWrite(data);

		System.out.println("   ✓ Created file: " + fileName);
		System.out.println("   ✓ Chart type: Pie Chart");
		System.out.println("   ✓ Data series: Sales");
		System.out.println("   ✓ Shows sales distribution percentage");
	}

	/**
	 * Test area chart
	 */
	private static void testAreaChart(List<ChartDataDTO> data) {
		System.out.println("\n5. Area Chart Test:");
		System.out.println("   " + "-".repeat(70));

		String fileName = "E:/temp/sales_area_chart.xlsx";

		ExcelChart chartConfig = createChart(
			"Cumulative Sales and Cost",
			ExcelChart.ChartType.AREA,
			"Month",
			new String[]{"Sales", "Cost"},
			0, 11, 20, 20,
			"Month", "Amount (USD)",
			true,
			ExcelChart.LegendPosition.BOTTOM
		);

		EasyExcel.write(fileName, ChartDataDTO.class)
			.sheet("Sales Data")
			.registerWriteHandler(new ExcelChartWriteHandler(chartConfig, ChartDataDTO.class, 1, data.size()))
			.doWrite(data);

		System.out.println("   ✓ Created file: " + fileName);
		System.out.println("   ✓ Chart type: Area Chart");
		System.out.println("   ✓ Data series: Sales, Cost");
		System.out.println("   ✓ Shows cumulative trend with filled areas");
	}

	/**
	 * Helper method to create ExcelChart annotation
	 */
	private static ExcelChart createChart(
		String title, ExcelChart.ChartType type,
		String xAxisField, String[] yAxisFields,
		int startRow, int startColumn, int endRow, int endColumn,
		String xAxisTitle, String yAxisTitle,
		boolean showLegend, ExcelChart.LegendPosition legendPosition
	) {
		return new ExcelChart() {
			@Override
			public String title() { return title; }

			@Override
			public ChartType type() { return type; }

			@Override
			public String xAxisField() { return xAxisField; }

			@Override
			public String[] yAxisFields() { return yAxisFields; }

			@Override
			public int startRow() { return startRow; }

			@Override
			public int startColumn() { return startColumn; }

			@Override
			public int endRow() { return endRow; }

			@Override
			public int endColumn() { return endColumn; }

			@Override
			public String xAxisTitle() { return xAxisTitle; }

			@Override
			public String yAxisTitle() { return yAxisTitle; }

			@Override
			public boolean showLegend() { return showLegend; }

			@Override
			public LegendPosition legendPosition() { return legendPosition; }

			@Override
			public Class<? extends java.lang.annotation.Annotation> annotationType() {
				return ExcelChart.class;
			}
		};
	}
}
