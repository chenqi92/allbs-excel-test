package cn.allbs.excel.test.controller;

import cn.allbs.excel.annotation.*;
import cn.allbs.excel.handle.*;
import cn.allbs.excel.template.ExcelTemplateGenerator;
import cn.allbs.excel.test.entity.ChartDataDTO;
import cn.allbs.excel.test.entity.CommentDataDTO;
import cn.allbs.excel.test.entity.DataValidationDTO;
import cn.allbs.excel.test.entity.EmployeeDTO;
import cn.allbs.excel.test.entity.FormulaDataDTO;
import cn.allbs.excel.test.entity.MultiSheetOrderDTO;
import cn.allbs.excel.test.entity.PerformanceDataDTO;
import cn.allbs.excel.test.entity.RowNumberDTO;
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
     * 使用注解方式,通过 writeHandler 属性指定 ExcelValidationWriteHandler
     */
    @GetMapping("/validation")
    @ExportExcel(
        name = "员工信息-数据验证",
        sheets = @Sheet(sheetName = "员工信息"),
        writeHandler = {ExcelValidationWriteHandler.class}
    )
    public List<DataValidationDTO> validationExport(@RequestParam(defaultValue = "10") int count) {
        return testDataService.generateDataValidationData(count);
    }

    /**
     * 7. 数据验证导出（自定义验证范围）
     * 使用注解方式，通过 @Sheet 的 validationStartRow 和 validationEndRow 指定验证范围
     */
    @GetMapping("/validation-custom")
    @ExportExcel(
        name = "员工信息-自定义验证",
        sheets = @Sheet(
            sheetName = "员工信息",
            validationStartRow = 1,
            validationEndRow = 1000
        )
    )
    public List<DataValidationDTO> validationCustomExport(@RequestParam(defaultValue = "10") int count) {
        return testDataService.generateDataValidationData(count);
    }

    /**
     * 8. 多 Sheet 关联导出
     * 使用注解方式，自动检测 @RelatedSheet 注解并处理关联关系
     * 实体类中的 @RelatedSheet 字段会自动导出到关联 Sheet，并创建超链接
     */
    @GetMapping("/multi-sheet")
    @ExportExcel(
        name = "订单及明细-多Sheet",
        sheets = @Sheet(sheetName = "订单")
    )
    public List<MultiSheetOrderDTO> multiSheetExport(@RequestParam(defaultValue = "5") int count) {
        return testDataService.generateMultiSheetOrders(count);
    }

    /**
     * 9. Excel 公式导出
     * 使用注解方式,通过 writeHandler 属性指定处理器
     */
    @GetMapping("/formula")
    @ExportExcel(
        name = "销售统计-公式计算",
        sheets = @Sheet(sheetName = "销售统计"),
        writeHandler = {ExcelFormulaWriteHandler.class, ExcelSheetStyleWriteHandler.class}
    )
    public List<FormulaDataDTO> formulaExport(@RequestParam(defaultValue = "10") int count) {
        return testDataService.generateFormulaData(count);
    }

    /**
     * 10. Sheet 样式设置（冻结窗格、自动筛选）
     * 使用注解方式,通过 writeHandler 属性指定处理器
     */
    @GetMapping("/sheet-style")
    @ExportExcel(
        name = "数据列表-冻结筛选",
        sheets = @Sheet(sheetName = "数据列表"),
        writeHandler = {ExcelSheetStyleWriteHandler.class}
    )
    public List<FormulaDataDTO> sheetStyleExport(@RequestParam(defaultValue = "20") int count) {
        return testDataService.generateFormulaData(count);
    }

    /**
     * 11. 条件格式导出（图标集）
     * 使用注解方式,通过 writeHandler 属性指定处理器
     */
    @GetMapping("/conditional-format")
    @ExportExcel(
        name = "员工绩效-条件格式",
        sheets = @Sheet(sheetName = "员工绩效"),
        writeHandler = {ConditionalFormatWriteHandler.class}
    )
    public List<PerformanceDataDTO> conditionalFormatExport(@RequestParam(defaultValue = "15") int count) {
        return testDataService.generatePerformanceData(count);
    }

    /**
     * 12. 单元格批注导出
     * 使用注解方式,通过 writeHandler 属性指定处理器
     */
    @GetMapping("/comment")
    @ExportExcel(
        name = "产品列表-批注说明",
        sheets = @Sheet(sheetName = "产品列表"),
        writeHandler = {ExcelCommentWriteHandler.class}
    )
    public List<CommentDataDTO> commentExport(@RequestParam(defaultValue = "10") int count) {
        return testDataService.generateCommentData(count);
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
     * 使用注解方式，通过 @ExportExcel 的 watermark 属性配置水印
     */
    @GetMapping("/watermark")
    @ExportExcel(
        name = "机密文档-水印",
        sheets = @Sheet(sheetName = "机密文档"),
        watermark = @ExcelWatermark(
            text = "CONFIDENTIAL",
            enabled = true,
            fontName = "Arial",
            fontSize = 48,
            color = "#D3D3D3",
            rotation = -45,
            opacity = 0.3,
            horizontalSpacing = 200,
            verticalSpacing = 150
        )
    )
    public List<WatermarkDataDTO> watermarkExport(@RequestParam(defaultValue = "30") int count) {
        return testDataService.generateWatermarkData(count);
    }

    /**
     * 15. 图表导出
     * 使用注解方式，通过 @ExportExcel 的 chart 属性配置图表
     */
    @GetMapping("/chart")
    @ExportExcel(
        name = "销售趋势图表",
        sheets = @Sheet(sheetName = "Sales Data"),
        chart = @ExcelChart(
            title = "Monthly Sales Trend",
            enabled = true,
            type = ExcelChart.ChartType.LINE,
            xAxisField = "month",
            yAxisFields = {"sales", "cost", "profit"},
            startRow = 0,
            startColumn = 11,
            endRow = 20,
            endColumn = 20,
            xAxisTitle = "Month",
            yAxisTitle = "Amount (USD)",
            showLegend = true,
            legendPosition = ExcelChart.LegendPosition.BOTTOM
        )
    )
    public List<ChartDataDTO> chartExport(@RequestParam(defaultValue = "12") int count) {
        return testDataService.generateChartData(Math.min(count, 12));
    }

    /**
     * 14. 图片导出
     * 使用注解方式,通过 writeHandler 属性指定处理器
     */
    @GetMapping("/image")
    @ExportExcel(
        name = "商品列表-图片导出",
        sheets = @Sheet(sheetName = "商品列表"),
        writeHandler = {cn.allbs.excel.handle.ImageWriteHandler.class}
    )
    public List<cn.allbs.excel.test.entity.ProductWithImageDTO> imageExport(@RequestParam(defaultValue = "10") int count) {
        return testDataService.generateProductWithImageData(count);
    }

    /**
     * 15. 加密导出
     * 使用注解方式，通过 @ExportExcel 的 password 属性配置密码
     * 注意：此方法使用 EasyExcel 原生加密，与 ExcelEncryptionUtil 的高级加密不同
     */
    @GetMapping("/encrypted")
    @ExportExcel(
        name = "加密文件-密码password123",
        sheets = @Sheet(sheetName = "敏感数据"),
        password = "password123"  // ✨ 使用注解方式设置密码
    )
    public List<SensitiveUserDTO> encryptedExport(@RequestParam(defaultValue = "20") int count) {
        return testDataService.generateSensitiveUsers(count);
    }

    /**
     * 15-2. 高级加密导出（使用 AGILE 算法）
     * 使用 ExcelEncryptionUtil 提供的高级加密功能
     * 支持自定义加密算法
     */
    @GetMapping("/encrypted-advanced")
    public void encryptedAdvancedExport(@RequestParam(defaultValue = "20") int count,
                                        @RequestParam(defaultValue = "password123") String password,
                                        HttpServletResponse response) throws IOException {
        List<SensitiveUserDTO> data = testDataService.generateSensitiveUsers(count);

        // 先导出到临时文件
        File tempFile = File.createTempFile("excel_", ".xlsx");
        tempFile.deleteOnExit();

        EasyExcel.write(tempFile, SensitiveUserDTO.class)
            .sheet("敏感数据")
            .doWrite(data);

        // 使用 AGILE 算法加密文件
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
        String fileName = URLEncoder.encode("加密文件-AGILE-密码" + password, "UTF-8").replaceAll("\\+", "%20");
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

    /**
     * 测试 @ExcelLine 注解 - 自动行号功能
     *
     * @ExcelLine 注解会为每一行数据自动填充行号(从1开始)
     */
    @GetMapping("/row-number")
    @ExportExcel(
        name = "产品清单-带行号",
        sheets = @Sheet(sheetName = "产品列表")
    )
    public List<RowNumberDTO> exportWithRowNumber(@RequestParam(defaultValue = "50") int count) {
        List<RowNumberDTO> products = new java.util.ArrayList<>();

        String[] categories = {"电子产品", "家居用品", "食品饮料", "图书文具", "运动户外"};
        for (int i = 1; i <= count; i++) {
            String category = categories[(i - 1) % categories.length];
            products.add(new RowNumberDTO(
                "产品-" + i,
                "P" + String.format("%04d", i),
                Math.round(Math.random() * 1000 * 100.0) / 100.0,
                (int) (Math.random() * 1000),
                category
            ));
        }

        return products;
    }

    /**
     * 测试动态文件名 - 使用SpEL表达式
     *
     * 支持的变量:
     * - #today: 当前日期
     * - #now: 当前日期时间
     * - #timestamp: 时间戳
     * - #uuid: UUID
     * - #formatDate(#today, 'yyyyMMdd'): 格式化日期
     * - #参数名: 方法参数值
     */
    @GetMapping("/dynamic-filename")
    @ExportExcel(
        name = "'UserList-' + #formatDate(#today, 'yyyyMMdd') + '-' + #username",
        sheets = @Sheet(sheetName = "UserData")
    )
    public List<SensitiveUserDTO> exportWithDynamicFilename(
        @RequestParam(defaultValue = "admin") String username,
        @RequestParam(defaultValue = "20") int count
    ) {
        return testDataService.generateSensitiveUsers(count);
    }

    /**
     * 测试动态文件名 - 使用时间戳
     */
    @GetMapping("/dynamic-filename-timestamp")
    @ExportExcel(
        name = "'ExportData-' + #timestamp",
        sheets = @Sheet(sheetName = "Data")
    )
    public List<EmployeeDTO> exportWithTimestamp(@RequestParam(defaultValue = "30") int count) {
        return testDataService.generateEmployees(count);
    }

    /**
     * 测试动态文件名 - 使用方法参数
     */
    @GetMapping("/dynamic-filename-param")
    @ExportExcel(
        name = "#department + '-Employees-' + #formatDateTime(#now, 'yyyyMMdd-HHmmss')",
        sheets = @Sheet(sheetName = "EmployeeData")
    )
    public List<EmployeeDTO> exportWithParamFilename(
        @RequestParam String department,
        @RequestParam(defaultValue = "25") int count
    ) {
        List<EmployeeDTO> employees = testDataService.generateEmployees(count);
        // 设置所有员工为指定部门
        employees.forEach(emp -> emp.setDepartment(department));
        return employees;
    }
}
