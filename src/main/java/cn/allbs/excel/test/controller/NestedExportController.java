package cn.allbs.excel.test.controller;

import cn.allbs.excel.annotation.ExportExcel;
import cn.allbs.excel.annotation.Sheet;
import cn.allbs.excel.test.entity.FlattenListOrderDTO;
import cn.allbs.excel.test.entity.FlattenListStudentDTO;
import cn.allbs.excel.test.entity.FlattenPropertyExampleDTO;
import cn.allbs.excel.test.entity.NestedPropertyExampleDTO;
import cn.allbs.excel.test.service.TestDataService;
import cn.allbs.excel.util.ListEntityExpander;
import com.alibaba.excel.EasyExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     */
    @GetMapping("/flatten-property")
    @ExportExcel(
        name = "对象展开示例",
        sheets = @Sheet(sheetName = "员工信息")
    )
    public List<FlattenPropertyExampleDTO> flattenPropertyExport(@RequestParam(defaultValue = "10") int count) {
        return testDataService.generateFlattenPropertyExamples(count);
    }

    /**
     * 3. @FlattenList 示例 - 订单明细展开
     * 注意：@FlattenList 需要特殊处理，不能使用 @ExportExcel 注解
     */
    @GetMapping("/flatten-list-order")
    public void flattenListOrderExport(
        @RequestParam(defaultValue = "5") int count,
        HttpServletResponse response
    ) throws IOException {
        // 1. 获取原始数据
        List<FlattenListOrderDTO> orders = testDataService.generateFlattenListOrders(count);

        // 2. 展开 List
        List<Map<String, Object>> expandedData = ListEntityExpander.expandData(orders);

        // 3. 生成元数据
        ListEntityExpander.ListExpandMetadata metadata =
            ListEntityExpander.analyzeClass(FlattenListOrderDTO.class);

        // 4. 生成合并区域
        List<ListEntityExpander.MergeRegion> mergeRegions =
            ListEntityExpander.generateMergeRegions(expandedData, metadata);

        // 5. 生成表头
        List<String> headers = ListEntityExpander.generateHeaders(metadata);
        List<List<String>> head = headers.stream()
            .map(Collections::singletonList)
            .collect(Collectors.toList());

        // 6. 设置响应
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("订单明细列表", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        // 7. 导出
        EasyExcel.write(response.getOutputStream())
            .head(head)
            .registerWriteHandler(new cn.allbs.excel.handle.ListMergeCellWriteHandler(mergeRegions))
            .sheet("订单明细")
            .doWrite(expandedData);
    }

    /**
     * 4. @FlattenList 示例 - 学生多 List 展开
     * 演示多个 List 字段的展开效果
     */
    @GetMapping("/flatten-list-student")
    public void flattenListStudentExport(
        @RequestParam(defaultValue = "5") int count,
        HttpServletResponse response
    ) throws IOException {
        // 1. 获取原始数据
        List<FlattenListStudentDTO> students = testDataService.generateFlattenListStudents(count);

        // 2. 展开 List
        List<Map<String, Object>> expandedData = ListEntityExpander.expandData(students);

        // 3. 生成元数据
        ListEntityExpander.ListExpandMetadata metadata =
            ListEntityExpander.analyzeClass(FlattenListStudentDTO.class);

        // 4. 生成合并区域
        List<ListEntityExpander.MergeRegion> mergeRegions =
            ListEntityExpander.generateMergeRegions(expandedData, metadata);

        // 5. 生成表头
        List<String> headers = ListEntityExpander.generateHeaders(metadata);
        List<List<String>> head = headers.stream()
            .map(Collections::singletonList)
            .collect(Collectors.toList());

        // 6. 设置响应
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("学生课程奖项列表", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        // 7. 导出
        EasyExcel.write(response.getOutputStream())
            .head(head)
            .registerWriteHandler(new cn.allbs.excel.handle.ListMergeCellWriteHandler(mergeRegions))
            .sheet("学生信息")
            .doWrite(expandedData);
    }
}
