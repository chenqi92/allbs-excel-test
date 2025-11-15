package cn.allbs.excel.test.controller;

import cn.allbs.excel.annotation.ExportExcel;
import cn.allbs.excel.annotation.ExportProgress;
import cn.allbs.excel.annotation.Sheet;
import cn.allbs.excel.test.entity.EmployeeDTO;
import cn.allbs.excel.test.entity.SensitiveUserDTO;
import cn.allbs.excel.test.listener.ConsoleProgressListener;
import cn.allbs.excel.test.service.TestDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
