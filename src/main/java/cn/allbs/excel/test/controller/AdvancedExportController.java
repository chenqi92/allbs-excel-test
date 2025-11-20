package cn.allbs.excel.test.controller;

import cn.allbs.excel.annotation.ExcelEncryption;
import cn.allbs.excel.annotation.ExcelWatermark;
import cn.allbs.excel.annotation.ExcelChart;
import cn.allbs.excel.annotation.ExportExcel;
import cn.allbs.excel.annotation.ExportProgress;
import cn.allbs.excel.annotation.Sheet;
import cn.allbs.excel.handle.ConditionalFormatWriteHandler;
import cn.allbs.excel.handle.ExcelChartWriteHandler;
import cn.allbs.excel.handle.ExcelCommentWriteHandler;
import cn.allbs.excel.handle.ExcelFormulaWriteHandler;
import cn.allbs.excel.handle.ExcelSheetStyleWriteHandler;
import cn.allbs.excel.handle.ExcelValidationWriteHandler;
import cn.allbs.excel.handle.ExcelWatermarkWriteHandler;
import cn.allbs.excel.template.ExcelTemplateGenerator;
import cn.allbs.excel.test.entity.ChartDataDTO;
import cn.allbs.excel.test.entity.CommentDataDTO;
import cn.allbs.excel.test.entity.DataValidationDTO;
import cn.allbs.excel.test.entity.EmployeeDTO;
import cn.allbs.excel.test.entity.FormulaDataDTO;
import cn.allbs.excel.test.entity.MultiSheetOrderDTO;
import cn.allbs.excel.test.entity.PerformanceDataDTO;
import cn.allbs.excel.test.entity.SensitiveUserDTO;
import cn.allbs.excel.test.entity.WatermarkDataDTO;
import cn.allbs.excel.test.listener.ConsoleProgressListener;
import cn.allbs.excel.test.service.TestDataService;
import cn.allbs.excel.util.ExcelEncryptionUtil;
import cn.allbs.excel.util.MultiSheetRelationProcessor;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 高级导出功能测试控制器
 */
@RestController
@RequestMapping("/api/export/advanced")
@CrossOrigin(origins = "*")
public class AdvancedExportController {

    @Autowired
    private TestDataService testDataService;

    /**
     * 1. 合并单元格导出
     */
    @GetMapping("/merge")
    @ExportExcel(
        name = "部门员工列表",
        sheets = @Sheet(
            sheetName = "员工信息",
            autoMerge = true  // 启用自动合并
        )
    )
    public List<EmployeeDTO> mergeExport(@RequestParam(defaultValue = "20") int count) {
        return testDataService.generateEmployees(count);
    }

    /**
     * 2. 数据脱敏导出（包含字典转换）
     */
    @GetMapping("/desensitize")
    @ExportExcel(
        name = "敏感信息用户列表",
        sheets = @Sheet(sheetName = "用户信息")
    )
    public List<SensitiveUserDTO> desensitizeExport(@RequestParam(defaultValue = "10") int count) {
        return testDataService.generateSensitiveUsers(count);
    }

    /**
     * 3. 导出进度回调
     */
    @GetMapping("/with-progress")
    @ExportExcel(
        name = "用户列表-带进度",
        sheets = @Sheet(sheetName = "用户信息")
    )
    @ExportProgress(
        listener = ConsoleProgressListener.class,
        interval = 100  // 每 100 行触发一次进度回调
    )
    public List<SensitiveUserDTO> exportWithProgress(@RequestParam(defaultValue = "5000") int count) {
        return testDataService.generateSensitiveUsers(count);
    }

    /**
     * 4. 大数据量导出（带进度）
     */
    @GetMapping("/large-data")
    @ExportExcel(
        name = "大数据导出测试",
        sheets = @Sheet(sheetName = "数据")
    )
    @ExportProgress(
        listener = ConsoleProgressListener.class,
        interval = 500  // 每 500 行触发一次进度回调
    )
    public List<SensitiveUserDTO> largeDataExport(@RequestParam(defaultValue = "20000") int count) {
        return testDataService.generateSensitiveUsers(count);
    }

    /**
     * 5. 合并单元格 + 导出进度
     */
    @GetMapping("/merge-with-progress")
    @ExportExcel(
        name = "员工列表-合并带进度",
        sheets = @Sheet(
            sheetName = "员工信息",
            autoMerge = true
        )
    )
    @ExportProgress(
        listener = ConsoleProgressListener.class,
        interval = 100  // 每 100 行触发一次进度回调
    )
    public List<EmployeeDTO> mergeWithProgressExport(@RequestParam(defaultValue = "3000") int count) {
        return testDataService.generateEmployees(count);
    }

    /**
     * 6. 数据验证导出
     */
    @GetMapping("/validation")
    public void validationExport(@RequestParam(defaultValue = "10") int count, HttpServletResponse response) throws IOException {
        List<DataValidationDTO> data = testDataService.generateDataValidationData(count);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("员工信息-数据验证", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 使用 EasyExcel 导出，并添加数据验证处理器
        EasyExcel.write(response.getOutputStream(), DataValidationDTO.class)
            .sheet("员工信息")
            .registerWriteHandler(new ExcelValidationWriteHandler(DataValidationDTO.class))
            .doWrite(data);
    }

    /**
     * 7. 数据验证导出（自定义验证范围）
     */
    @GetMapping("/validation-custom")
    public void validationCustomExport(@RequestParam(defaultValue = "10") int count, HttpServletResponse response) throws IOException {
        List<DataValidationDTO> data = testDataService.generateDataValidationData(count);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("员工信息-自定义验证", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 使用自定义验证范围（从第 2 行到第 1000 行）
        EasyExcel.write(response.getOutputStream(), DataValidationDTO.class)
            .sheet("员工信息")
            .registerWriteHandler(new ExcelValidationWriteHandler(DataValidationDTO.class, 1, 1000))
            .doWrite(data);
    }

    /**
     * 8. 多 Sheet 关联导出
     */
    @GetMapping("/multi-sheet")
    public void multiSheetExport(@RequestParam(defaultValue = "5") int count, HttpServletResponse response) throws IOException {
        List<MultiSheetOrderDTO> orders = testDataService.generateMultiSheetOrders(count);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("订单及明细-多Sheet", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 使用 MultiSheetRelationProcessor 导出多 Sheet 关联数据
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
        try {
            MultiSheetRelationProcessor.exportWithRelations(excelWriter, orders, "订单", MultiSheetOrderDTO.class);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 9. Excel 公式导出
     */
    @GetMapping("/formula")
    public void formulaExport(@RequestParam(defaultValue = "10") int count, HttpServletResponse response) throws IOException {
        List<FormulaDataDTO> data = testDataService.generateFormulaData(count);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("销售统计-公式计算", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 使用 EasyExcel 导出，并添加公式处理器和样式处理器
        EasyExcel.write(response.getOutputStream(), FormulaDataDTO.class)
            .sheet("销售统计")
            .registerWriteHandler(new ExcelFormulaWriteHandler(FormulaDataDTO.class))
            .registerWriteHandler(new ExcelSheetStyleWriteHandler(FormulaDataDTO.class))
            .doWrite(data);
    }

    /**
     * 10. Sheet 样式设置（冻结窗格、自动筛选）
     */
    @GetMapping("/sheet-style")
    public void sheetStyleExport(@RequestParam(defaultValue = "20") int count, HttpServletResponse response) throws IOException {
        List<FormulaDataDTO> data = testDataService.generateFormulaData(count);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("数据列表-冻结筛选", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 使用 EasyExcel 导出，并添加样式处理器
        EasyExcel.write(response.getOutputStream(), FormulaDataDTO.class)
            .sheet("数据列表")
            .registerWriteHandler(new ExcelSheetStyleWriteHandler(FormulaDataDTO.class))
            .doWrite(data);
    }

    /**
     * 11. 条件格式导出（图标集）
     */
    @GetMapping("/conditional-format")
    public void conditionalFormatExport(@RequestParam(defaultValue = "15") int count, HttpServletResponse response) throws IOException {
        List<PerformanceDataDTO> data = testDataService.generatePerformanceData(count);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("员工绩效-条件格式", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 使用 EasyExcel 导出，并添加条件格式处理器
        EasyExcel.write(response.getOutputStream(), PerformanceDataDTO.class)
            .sheet("员工绩效")
            .registerWriteHandler(new ConditionalFormatWriteHandler(PerformanceDataDTO.class))
            .doWrite(data);
    }

    /**
     * 12. 单元格批注导出
     */
    @GetMapping("/comment")
    public void commentExport(@RequestParam(defaultValue = "10") int count, HttpServletResponse response) throws IOException {
        List<CommentDataDTO> data = testDataService.generateCommentData(count);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("产品列表-批注说明", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 使用 EasyExcel 导出，并添加批注处理器
        EasyExcel.write(response.getOutputStream(), CommentDataDTO.class)
            .sheet("产品列表")
            .registerWriteHandler(new ExcelCommentWriteHandler(CommentDataDTO.class))
            .doWrite(data);
    }

    /**
     * 13. 模板下载（空模板）
     */
    @GetMapping("/template")
    public void downloadTemplate(@RequestParam(defaultValue = "0") int sampleRows, HttpServletResponse response) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("员工导入模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 使用 ExcelTemplateGenerator 生成模板
        ExcelTemplateGenerator.generateTemplate(
            response.getOutputStream(),
            DataValidationDTO.class,
            sampleRows,
            "员工信息"
        );
    }

    /**
     * 14. 水印导出
     */
    @GetMapping("/watermark")
    public void watermarkExport(@RequestParam(defaultValue = "30") int count, HttpServletResponse response) throws IOException {
        List<WatermarkDataDTO> data = testDataService.generateWatermarkData(count);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("机密文档-水印", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 创建水印配置
        ExcelWatermark watermark = createWatermark(
            "CONFIDENTIAL - " + System.getProperty("user.name"),
            true, "Arial", 48, "#D3D3D3", -45, 0.3, 200, 150, 0, 0
        );

        // 使用 EasyExcel 导出，并添加水印处理器
        EasyExcel.write(response.getOutputStream(), WatermarkDataDTO.class)
            .sheet("机密文档")
            .registerWriteHandler(new ExcelWatermarkWriteHandler(watermark))
            .doWrite(data);
    }

    /**
     * 15. 图表导出
     */
    @GetMapping("/chart")
    public void chartExport(@RequestParam(defaultValue = "12") int count, HttpServletResponse response) throws IOException {
        List<ChartDataDTO> data = testDataService.generateChartData(Math.min(count, 12));

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("销售趋势图表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 创建图表配置
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

        // 使用 EasyExcel 导出，并添加图表处理器
        EasyExcel.write(response.getOutputStream(), ChartDataDTO.class)
            .sheet("Sales Data")
            .registerWriteHandler(new ExcelChartWriteHandler(chartConfig, ChartDataDTO.class, 1, data.size()))
            .doWrite(data);
    }

    /**
     * 14. 图片导出
     */
    @GetMapping("/image")
    public void imageExport(@RequestParam(defaultValue = "10") int count, HttpServletResponse response) throws IOException {
        List<cn.allbs.excel.test.entity.ProductWithImageDTO> data = testDataService.generateProductWithImageData(count);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("商品列表-图片导出", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 使用 EasyExcel 导出，并添加图片处理器
        EasyExcel.write(response.getOutputStream(), cn.allbs.excel.test.entity.ProductWithImageDTO.class)
            .sheet("商品列表")
            .registerWriteHandler(new cn.allbs.excel.handle.ImageWriteHandler(cn.allbs.excel.test.entity.ProductWithImageDTO.class))
            .doWrite(data);
    }

    /**
     * 15. 加密导出
     */
    @GetMapping("/encrypted")
    public void encryptedExport(@RequestParam(defaultValue = "20") int count,
                                 @RequestParam(defaultValue = "password123") String password,
                                 HttpServletResponse response) throws IOException {
        List<SensitiveUserDTO> data = testDataService.generateSensitiveUsers(count);

        // 先导出到临时文件
        File tempFile = File.createTempFile("excel_", ".xlsx");
        tempFile.deleteOnExit();

        EasyExcel.write(tempFile, SensitiveUserDTO.class)
            .sheet("敏感数据")
            .doWrite(data);

        // 加密文件
        File encryptedFile = File.createTempFile("encrypted_", ".xlsx");
        encryptedFile.deleteOnExit();

        ExcelEncryptionUtil.encryptFile(
            tempFile,
            encryptedFile,
            password,
            ExcelEncryption.EncryptionAlgorithm.AGILE
        );

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("加密文件-密码" + password, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 输出加密文件
        try (FileInputStream fis = new FileInputStream(encryptedFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, bytesRead);
            }
        }

        // 清理临时文件
        tempFile.delete();
        encryptedFile.delete();
    }

    // Helper methods to create annotations
    private ExcelWatermark createWatermark(
        String text, boolean enabled, String fontName, int fontSize,
        String color, int rotation, double opacity,
        int horizontalSpacing, int verticalSpacing,
        int startRow, int startColumn
    ) {
        return new ExcelWatermark() {
            @Override public String text() { return text; }
            @Override public boolean enabled() { return enabled; }
            @Override public String fontName() { return fontName; }
            @Override public int fontSize() { return fontSize; }
            @Override public String color() { return color; }
            @Override public int rotation() { return rotation; }
            @Override public double opacity() { return opacity; }
            @Override public int horizontalSpacing() { return horizontalSpacing; }
            @Override public int verticalSpacing() { return verticalSpacing; }
            @Override public int startRow() { return startRow; }
            @Override public int startColumn() { return startColumn; }
            @Override public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return ExcelWatermark.class;
            }
        };
    }

    private ExcelChart createChart(
        String title, ExcelChart.ChartType type,
        String xAxisField, String[] yAxisFields,
        int startRow, int startColumn, int endRow, int endColumn,
        String xAxisTitle, String yAxisTitle,
        boolean showLegend, ExcelChart.LegendPosition legendPosition
    ) {
        return new ExcelChart() {
            @Override public String title() { return title; }
            @Override public ChartType type() { return type; }
            @Override public String xAxisField() { return xAxisField; }
            @Override public String[] yAxisFields() { return yAxisFields; }
            @Override public int startRow() { return startRow; }
            @Override public int startColumn() { return startColumn; }
            @Override public int endRow() { return endRow; }
            @Override public int endColumn() { return endColumn; }
            @Override public String xAxisTitle() { return xAxisTitle; }
            @Override public String yAxisTitle() { return yAxisTitle; }
            @Override public boolean showLegend() { return showLegend; }
            @Override public LegendPosition legendPosition() { return legendPosition; }
            @Override public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return ExcelChart.class;
            }
        };
    }
}
