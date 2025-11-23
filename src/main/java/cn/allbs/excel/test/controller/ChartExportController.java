package cn.allbs.excel.test.controller;

import cn.allbs.excel.annotation.ExcelChart;
import cn.allbs.excel.annotation.ExportExcel;
import cn.allbs.excel.annotation.Sheet;
import cn.allbs.excel.test.entity.ChartDataDTO;
import cn.allbs.excel.test.service.TestDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 图表导出控制器
 * <p>
 * 演示 6 种不同类型的 Excel 图表导出功能
 * </p>
 *
 * @author ChenQi
 * @since 2025-11-21
 */
@Slf4j
@RestController
@RequestMapping("/api/chart")
@CrossOrigin(origins = "*")
public class ChartExportController {

	@Autowired
	private TestDataService testDataService;

	/**
	 * 1. 折线图导出
	 * <p>
	 * 用于展示数据随时间的变化趋势
	 * 适合：销售趋势、温度变化、股价走势等
	 * </p>
	 */
	@GetMapping("/line")
	@ExportExcel(
		name = "折线图-销售趋势",
		sheets = @Sheet(sheetName = "Sales Data"),
		chart = @ExcelChart(
			title = "Monthly Sales Trend",
			enabled = true,
			type = ExcelChart.ChartType.LINE,
			xAxisField = "Month",
			yAxisFields = {"Sales", "Cost", "Profit"},
			startRow = 0,
			startColumn = 8,
			endRow = 20,
			endColumn = 18,
			xAxisTitle = "Month",
			yAxisTitle = "Amount (USD)",
			showLegend = true,
			legendPosition = ExcelChart.LegendPosition.BOTTOM
		)
	)
	public List<ChartDataDTO> exportLineChart(@RequestParam(defaultValue = "12") int months) {
		log.info("Exporting line chart with {} months of data", months);
		return testDataService.generateChartData(Math.min(months, 12));
	}

	/**
	 * 2. 柱状图导出（纵向）
	 * <p>
	 * 用于比较不同类别的数据
	 * 适合：销售对比、成本分析、业绩排名等
	 * </p>
	 */
	@GetMapping("/column")
	@ExportExcel(
		name = "柱状图-销售对比",
		sheets = @Sheet(sheetName = "Sales Comparison"),
		chart = @ExcelChart(
			title = "Monthly Sales vs Cost",
			enabled = true,
			type = ExcelChart.ChartType.COLUMN,
			xAxisField = "Month",
			yAxisFields = {"Sales", "Cost"},
			startRow = 0,
			startColumn = 8,
			endRow = 20,
			endColumn = 18,
			xAxisTitle = "Month",
			yAxisTitle = "Amount (USD)",
			showLegend = true,
			legendPosition = ExcelChart.LegendPosition.TOP
		)
	)
	public List<ChartDataDTO> exportColumnChart(@RequestParam(defaultValue = "12") int months) {
		log.info("Exporting column chart with {} months of data", months);
		return testDataService.generateChartData(Math.min(months, 12));
	}

	/**
	 * 3. 条形图导出（横向）
	 * <p>
	 * 用于比较不同项目，横向展示更易读
	 * 适合：部门对比、产品销量排行等
	 * </p>
	 */
	@GetMapping("/bar")
	@ExportExcel(
		name = "条形图-月度利润",
		sheets = @Sheet(sheetName = "Monthly Profit"),
		chart = @ExcelChart(
			title = "Monthly Profit Analysis",
			enabled = true,
			type = ExcelChart.ChartType.BAR,
			xAxisField = "Month",
			yAxisFields = {"Profit"},
			startRow = 0,
			startColumn = 8,
			endRow = 20,
			endColumn = 18,
			xAxisTitle = "Month",
			yAxisTitle = "Profit (USD)",
			showLegend = true,
			legendPosition = ExcelChart.LegendPosition.RIGHT
		)
	)
	public List<ChartDataDTO> exportBarChart(@RequestParam(defaultValue = "12") int months) {
		log.info("Exporting bar chart with {} months of data", months);
		return testDataService.generateChartData(Math.min(months, 12));
	}

	/**
	 * 4. 饼图导出
	 * <p>
	 * 用于展示各部分占总体的比例
	 * 适合：市场份额、销售占比、预算分配等
	 * </p>
	 */
	@GetMapping("/pie")
	@ExportExcel(
		name = "饼图-销售分布",
		sheets = @Sheet(sheetName = "Sales Distribution"),
		chart = @ExcelChart(
			title = "Sales Distribution by Month",
			enabled = true,
			type = ExcelChart.ChartType.PIE,
			xAxisField = "Month",
			yAxisFields = {"Sales"},
			startRow = 0,
			startColumn = 8,
			endRow = 20,
			endColumn = 18,
			showLegend = true,
			legendPosition = ExcelChart.LegendPosition.RIGHT
		)
	)
	public List<ChartDataDTO> exportPieChart(@RequestParam(defaultValue = "12") int months) {
		log.info("Exporting pie chart with {} months of data", months);
		return testDataService.generateChartData(Math.min(months, 12));
	}

	/**
	 * 5. 面积图导出
	 * <p>
	 * 用于展示数量随时间的累积变化
	 * 适合：累计销售额、库存变化、用户增长等
	 * </p>
	 */
	@GetMapping("/area")
	@ExportExcel(
		name = "面积图-累计销售",
		sheets = @Sheet(sheetName = "Cumulative Sales"),
		chart = @ExcelChart(
			title = "Cumulative Sales and Cost",
			enabled = true,
			type = ExcelChart.ChartType.AREA,
			xAxisField = "Month",
			yAxisFields = {"Sales", "Cost"},
			startRow = 0,
			startColumn = 8,
			endRow = 20,
			endColumn = 18,
			xAxisTitle = "Month",
			yAxisTitle = "Amount (USD)",
			showLegend = true,
			legendPosition = ExcelChart.LegendPosition.BOTTOM
		)
	)
	public List<ChartDataDTO> exportAreaChart(@RequestParam(defaultValue = "12") int months) {
		log.info("Exporting area chart with {} months of data", months);
		return testDataService.generateChartData(Math.min(months, 12));
	}

	/**
	 * 6. 散点图导出
	 * <p>
	 * 用于展示两个变量之间的相关性
	 * 适合：数据分布、相关性分析、异常检测等
	 * </p>
	 */
	@GetMapping("/scatter")
	@ExportExcel(
		name = "散点图-销售与成本关系",
		sheets = @Sheet(sheetName = "Sales vs Cost"),
		chart = @ExcelChart(
			title = "Sales and Cost Correlation",
			enabled = true,
			type = ExcelChart.ChartType.SCATTER,
			xAxisField = "Month",
			yAxisFields = {"Sales", "Cost"},
			startRow = 0,
			startColumn = 8,
			endRow = 20,
			endColumn = 18,
			xAxisTitle = "Month",
			yAxisTitle = "Amount (USD)",
			showLegend = true,
			legendPosition = ExcelChart.LegendPosition.TOP_RIGHT
		)
	)
	public List<ChartDataDTO> exportScatterChart(@RequestParam(defaultValue = "12") int months) {
		log.info("Exporting scatter chart with {} months of data", months);
		return testDataService.generateChartData(Math.min(months, 12));
	}

	/**
	 * 7. 多系列折线图
	 * <p>
	 * 演示在一个图表中显示多个数据系列
	 * </p>
	 */
	@GetMapping("/multi-series")
	@ExportExcel(
		name = "多系列图表-完整财务数据",
		sheets = @Sheet(sheetName = "Financial Data"),
		chart = @ExcelChart(
			title = "Complete Financial Overview",
			enabled = true,
			type = ExcelChart.ChartType.LINE,
			xAxisField = "Month",
			yAxisFields = {"Sales", "Cost", "Profit"},
			startRow = 0,
			startColumn = 8,
			endRow = 22,
			endColumn = 20,
			xAxisTitle = "Month",
			yAxisTitle = "Amount (USD)",
			showLegend = true,
			legendPosition = ExcelChart.LegendPosition.BOTTOM
		)
	)
	public List<ChartDataDTO> exportMultiSeriesChart(@RequestParam(defaultValue = "12") int months) {
		log.info("Exporting multi-series chart with {} months of data", months);
		return testDataService.generateChartData(Math.min(months, 12));
	}

	/**
	 * 8. 自定义位置和尺寸
	 * <p>
	 * 演示如何自定义图表的位置和大小
	 * </p>
	 */
	@GetMapping("/custom-position")
	@ExportExcel(
		name = "自定义图表-大尺寸",
		sheets = @Sheet(sheetName = "Sales Data"),
		chart = @ExcelChart(
			title = "Sales Trend (Large Chart)",
			enabled = true,
			type = ExcelChart.ChartType.COLUMN,
			xAxisField = "Month",
			yAxisFields = {"Sales", "Profit"},
			startRow = 0,
			startColumn = 10,
			endRow = 30,
			endColumn = 25,
			xAxisTitle = "Month",
			yAxisTitle = "Amount (USD)",
			showLegend = true,
			legendPosition = ExcelChart.LegendPosition.TOP
		)
	)
	public List<ChartDataDTO> exportCustomPositionChart(@RequestParam(defaultValue = "12") int months) {
		log.info("Exporting custom position chart with {} months of data", months);
		return testDataService.generateChartData(Math.min(months, 12));
	}

	/**
	 * 9. 多图表组合 - 综合分析
	 * <p>
	 * 在一个 Sheet 中同时展示多种类型的图表
	 * 适合：综合数据仪表板、多维度分析报告
	 * </p>
	 */
	@GetMapping("/multiple-charts")
	@ExportExcel(
		name = "多图表组合-综合分析",
		sheets = @Sheet(sheetName = "Comprehensive Analysis"),
		charts = {
			@ExcelChart(
				title = "Sales Trend (Line)",
				enabled = true,
				type = ExcelChart.ChartType.LINE,
				xAxisField = "Month",
				yAxisFields = {"Sales", "Cost", "Profit"},
				startRow = 0,
				startColumn = 8,
				endRow = 15,
				endColumn = 17,
				xAxisTitle = "Month",
				yAxisTitle = "Amount (USD)",
				showLegend = true,
				legendPosition = ExcelChart.LegendPosition.BOTTOM
			),
			@ExcelChart(
				title = "Sales vs Cost (Column)",
				enabled = true,
				type = ExcelChart.ChartType.COLUMN,
				xAxisField = "Month",
				yAxisFields = {"Sales", "Cost"},
				startRow = 17,
				startColumn = 8,
				endRow = 32,
				endColumn = 17,
				xAxisTitle = "Month",
				yAxisTitle = "Amount (USD)",
				showLegend = true,
				legendPosition = ExcelChart.LegendPosition.TOP
			),
			@ExcelChart(
				title = "Sales Distribution (Pie)",
				enabled = true,
				type = ExcelChart.ChartType.PIE,
				xAxisField = "Month",
				yAxisFields = {"Sales"},
				startRow = 0,
				startColumn = 19,
				endRow = 15,
				endColumn = 27,
				showLegend = true,
				legendPosition = ExcelChart.LegendPosition.RIGHT
			)
		}
	)
	public List<ChartDataDTO> exportMultipleCharts(@RequestParam(defaultValue = "12") int months) {
		log.info("Exporting multiple charts with {} months of data", months);
		return testDataService.generateChartData(Math.min(months, 12));
	}

	/**
	 * 10. 多图表 - 分层数据分析
	 * <p>
	 * 使用不同的数据系列创建多个独立的图表
	 * </p>
	 */
	@GetMapping("/layered-analysis")
	@ExportExcel(
		name = "分层分析-多图表",
		sheets = @Sheet(sheetName = "Layered Analysis"),
		charts = {
			@ExcelChart(
				title = "Revenue Analysis",
				enabled = true,
				type = ExcelChart.ChartType.LINE,
				xAxisField = "Month",
				yAxisFields = {"Sales"},
				startRow = 0,
				startColumn = 8,
				endRow = 15,
				endColumn = 17,
				xAxisTitle = "Month",
				yAxisTitle = "Sales (USD)",
				showLegend = true,
				legendPosition = ExcelChart.LegendPosition.BOTTOM
			),
			@ExcelChart(
				title = "Cost Analysis",
				enabled = true,
				type = ExcelChart.ChartType.AREA,
				xAxisField = "Month",
				yAxisFields = {"Cost"},
				startRow = 17,
				startColumn = 8,
				endRow = 32,
				endColumn = 17,
				xAxisTitle = "Month",
				yAxisTitle = "Cost (USD)",
				showLegend = true,
				legendPosition = ExcelChart.LegendPosition.TOP
			),
			@ExcelChart(
				title = "Profit Analysis",
				enabled = true,
				type = ExcelChart.ChartType.COLUMN,
				xAxisField = "Month",
				yAxisFields = {"Profit"},
				startRow = 0,
				startColumn = 19,
				endRow = 15,
				endColumn = 27,
				xAxisTitle = "Month",
				yAxisTitle = "Profit (USD)",
				showLegend = true,
				legendPosition = ExcelChart.LegendPosition.RIGHT
			)
		}
	)
	public List<ChartDataDTO> exportLayeredAnalysis(@RequestParam(defaultValue = "12") int months) {
		log.info("Exporting layered analysis with {} months of data", months);
		return testDataService.generateChartData(Math.min(months, 12));
	}
}
