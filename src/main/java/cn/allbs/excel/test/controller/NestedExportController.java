package cn.allbs.excel.test.controller;

import cn.allbs.excel.annotation.ExportExcel;
import cn.allbs.excel.annotation.Sheet;
import cn.allbs.excel.handle.ConditionalStyleWriteHandler;
import cn.allbs.excel.test.entity.ConditionalStyleDTO;
import cn.allbs.excel.test.entity.DynamicHeaderDTO;
import cn.allbs.excel.test.entity.FlattenListOrderDTO;
import cn.allbs.excel.test.entity.FlattenListStudentDTO;
import cn.allbs.excel.test.entity.FlattenPropertyExampleDTO;
import cn.allbs.excel.test.entity.NestedPropertyExampleDTO;
import cn.allbs.excel.test.service.TestDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 嵌套对象导出功能测试控制器
 * 演示 @NestedProperty, @FlattenProperty, @FlattenList 的使用
 */
@RestController
@RequestMapping("/api/export/nested")
@CrossOrigin(origins = "*")
public class NestedExportController {

    @Autowired
    private TestDataService testDataService;

    /**
     * 1. @NestedProperty 示例 - 嵌套对象字段提取
     */
    @GetMapping("/nested-property")
    @ExportExcel(
        name = "嵌套对象示例",
        sheets = @Sheet(sheetName = "员工信息")
    )
    public List<NestedPropertyExampleDTO> nestedPropertyExport(@RequestParam(defaultValue = "10") int count) {
        return testDataService.generateNestedPropertyExamples(count);
    }

    /**
     * 2. @FlattenProperty 示例 - 嵌套对象自动展开
     * 使用 @ExportExcel 注解，FlattenPropertyWriteHandler 自动处理嵌套对象展开
     */
    @GetMapping("/flatten-property")
    @ExportExcel(
        name = "对象展开示例",
        sheets = @Sheet(sheetName = "员工信息")
    )
    public List<FlattenPropertyExampleDTO> flattenPropertyExport(@RequestParam(defaultValue = "10") int count) {
        // FlattenPropertyWriteHandler 会自动检测 @FlattenProperty 注解并处理嵌套对象展开
        return testDataService.generateFlattenPropertyExamples(count);
    }

    /**
     * 3. @FlattenList 示例 - 订单明细展开
     * 使用 @ExportExcel 注解，FlattenListWriteHandler 自动处理 List 展开和单元格合并
     */
    @GetMapping("/flatten-list-order")
    @ExportExcel(
        name = "订单明细列表",
        sheets = @Sheet(sheetName = "订单明细")
    )
    public List<FlattenListOrderDTO> flattenListOrderExport(@RequestParam(defaultValue = "5") int count) {
        // FlattenListWriteHandler 会自动检测 @FlattenList 注解并处理 List 展开和单元格合并
        return testDataService.generateFlattenListOrders(count);
    }

    /**
     * 4. @FlattenList 示例 - 学生多 List 展开
     * 使用 @ExportExcel 注解，FlattenListWriteHandler 自动处理多个 List 字段的展开效果
     */
    @GetMapping("/flatten-list-student")
    @ExportExcel(
        name = "学生课程奖项列表",
        sheets = @Sheet(sheetName = "学生信息")
    )
    public List<FlattenListStudentDTO> flattenListStudentExport(@RequestParam(defaultValue = "5") int count) {
        // FlattenListWriteHandler 会自动检测 @FlattenList 注解并处理多个 List 字段的展开和单元格合并
        return testDataService.generateFlattenListStudents(count);
    }

    /**
     * 5. @ConditionalStyle 示例 - 条件样式
     * 演示根据单元格值自动应用不同样式
     * 注意：使用注解方式,需要通过 writeHandler 属性指定 ConditionalStyleWriteHandler
     */
    @GetMapping("/conditional-style")
    @ExportExcel(
        name = "条件样式示例",
        sheets = @Sheet(sheetName = "条件样式示例"),
        writeHandler = {ConditionalStyleWriteHandler.class}
    )
    public List<ConditionalStyleDTO> conditionalStyleExport(
        @RequestParam(defaultValue = "20") int count
    ) {
        return testDataService.generateConditionalStyleData(count);
    }

    /**
     * 6. @DynamicHeaders 示例 - 动态表头
     * 使用 @ExportExcel 注解，DynamicHeaderWriteHandler 自动处理动态表头生成
     */
    @GetMapping("/dynamic-header")
    @ExportExcel(
        name = "动态表头示例",
        sheets = @Sheet(sheetName = "产品列表")
    )
    public List<DynamicHeaderDTO> dynamicHeaderExport(@RequestParam(defaultValue = "15") int count) {
        // DynamicHeaderWriteHandler 会自动检测 @DynamicHeaders 注解并处理动态表头生成
        return testDataService.generateDynamicHeaderData(count);
    }
}
