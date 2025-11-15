package cn.allbs.excel.test.controller;

import cn.allbs.excel.annotation.ExportExcel;
import cn.allbs.excel.annotation.Sheet;
import cn.allbs.excel.test.entity.OrderDTO;
import cn.allbs.excel.test.entity.ProductDTO;
import cn.allbs.excel.test.entity.UserDTO;
import cn.allbs.excel.test.service.TestDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 基本导出功能测试控制器
 */
@RestController
@RequestMapping("/api/export/basic")
@CrossOrigin(origins = "*")
public class BasicExportController {

    @Autowired
    private TestDataService testDataService;

    /**
     * 1. 基本导出
     */
    @GetMapping("/simple")
    @ExportExcel(
        name = "用户列表",
        sheets = @Sheet(sheetName = "用户信息")
    )
    public List<UserDTO> simpleExport(@RequestParam(defaultValue = "10") int count) {
        return testDataService.generateUsers(count);
    }

    /**
     * 2. 空数据导出（带表头）
     */
    @GetMapping("/empty")
    @ExportExcel(
        name = "空用户列表",
        sheets = @Sheet(
            sheetName = "用户信息",
            clazz = UserDTO.class  // 指定数据类型用于生成表头
        )
    )
    public List<UserDTO> emptyExport() {
        return Collections.emptyList();
    }

    /**
     * 3. 只导出有注解的字段
     */
    @GetMapping("/only-annotated")
    @ExportExcel(
        name = "用户列表-仅注解字段",
        sheets = @Sheet(sheetName = "用户信息"),
        onlyExcelProperty = true  // 只导出有 @ExcelProperty 注解的字段
    )
    public List<UserDTO> onlyAnnotatedExport(@RequestParam(defaultValue = "10") int count) {
        return testDataService.generateUsers(count);
    }

    /**
     * 4. 多 Sheet 导出
     */
    @GetMapping("/multi-sheet")
    @ExportExcel(
        name = "综合报表",
        sheets = {
            @Sheet(sheetName = "用户信息", clazz = UserDTO.class),
            @Sheet(sheetName = "订单信息", clazz = OrderDTO.class)
        }
    )
    public List<List<?>> multiSheetExport(
        @RequestParam(defaultValue = "10") int userCount,
        @RequestParam(defaultValue = "20") int orderCount
    ) {
        List<UserDTO> users = testDataService.generateUsers(userCount);
        List<OrderDTO> orders = testDataService.generateOrders(orderCount);
        return Arrays.asList(users, orders);
    }

    /**
     * 5. 多 Sheet 空数据导出
     */
    @GetMapping("/multi-sheet-empty")
    @ExportExcel(
        name = "综合报表-空数据",
        sheets = {
            @Sheet(sheetName = "用户信息", clazz = UserDTO.class),
            @Sheet(sheetName = "订单信息", clazz = OrderDTO.class)
        }
    )
    public List<List<?>> multiSheetEmptyExport() {
        return Arrays.asList(
            Collections.emptyList(),
            Collections.emptyList()
        );
    }

    /**
     * 6. 列宽和行高设置
     */
    @GetMapping("/styled")
    @ExportExcel(
        name = "产品列表",
        sheets = @Sheet(sheetName = "产品信息")
    )
    public List<ProductDTO> styledExport(@RequestParam(defaultValue = "15") int count) {
        return testDataService.generateProducts(count);
    }

    /**
     * 7. 动态文件名
     */
    @GetMapping("/dynamic-name")
    @ExportExcel(
        name = "用户列表-#{#date}",  // 支持 SpEL 表达式
        sheets = @Sheet(sheetName = "用户信息")
    )
    public List<UserDTO> dynamicNameExport(
        @RequestParam String date,
        @RequestParam(defaultValue = "10") int count
    ) {
        return testDataService.generateUsers(count);
    }
}
