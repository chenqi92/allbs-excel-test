package cn.allbs.excel.test.controller;

import cn.allbs.excel.annotation.ExportExcel;
import cn.allbs.excel.annotation.ExportProgress;
import cn.allbs.excel.annotation.Sheet;
import cn.allbs.excel.handle.ExcelValidationWriteHandler;
import cn.allbs.excel.test.entity.DataValidationDTO;
import cn.allbs.excel.test.entity.EmployeeDTO;
import cn.allbs.excel.test.entity.MultiSheetOrderDTO;
import cn.allbs.excel.test.entity.SensitiveUserDTO;
import cn.allbs.excel.test.listener.ConsoleProgressListener;
import cn.allbs.excel.test.service.TestDataService;
import cn.allbs.excel.util.MultiSheetRelationProcessor;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
        interval = 10  // 每 10 行触发一次进度回调
    )
    public List<SensitiveUserDTO> exportWithProgress(@RequestParam(defaultValue = "100") int count) {
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
        interval = 100
    )
    public List<SensitiveUserDTO> largeDataExport(@RequestParam(defaultValue = "1000") int count) {
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
        interval = 5
    )
    public List<EmployeeDTO> mergeWithProgressExport(@RequestParam(defaultValue = "50") int count) {
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
}
