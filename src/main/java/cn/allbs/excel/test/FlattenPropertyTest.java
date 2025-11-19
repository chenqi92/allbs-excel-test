package cn.allbs.excel.test;

import cn.allbs.excel.head.FlattenHeadGenerator;
import cn.allbs.excel.test.entity.FlattenPropertyExampleDTO;
import cn.allbs.excel.test.entity.nested.Department;
import cn.allbs.excel.test.entity.nested.Leader;
import cn.allbs.excel.util.FlattenFieldProcessor;
import com.alibaba.excel.EasyExcel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FlattenProperty 功能测试
 */
public class FlattenPropertyTest {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("FlattenProperty 功能测试");
        System.out.println("========================================");

        // 创建测试数据
        List<FlattenPropertyExampleDTO> dataList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            FlattenPropertyExampleDTO dto = new FlattenPropertyExampleDTO();
            dto.setId((long) i);
            dto.setName("员工" + i);
            dto.setAge(25 + i);

            // 部门
            Department dept = new Department();
            dept.setCode("DEPT-" + (i % 3));
            dept.setName(i % 3 == 0 ? "技术部" : (i % 3 == 1 ? "市场部" : "人力资源部"));
            dept.setType(i % 2 == 0 ? "研发" : "运营");
            dto.setDepartment(dept);

            // 上级部门
            Department parentDept = new Department();
            parentDept.setCode("PARENT-" + (i % 2));
            parentDept.setName("总部");
            parentDept.setType("管理");
            dto.setParentDept(parentDept);

            // 主管部门（带领导，用于测试递归）
            Department managerDept = new Department();
            managerDept.setCode("MGR-" + i);
            managerDept.setName("主管部门" + i);
            managerDept.setType("管理");

            Leader leader = new Leader();
            leader.setName("领导" + i);
            leader.setPosition("总监");
            leader.setPhone("138" + String.format("%08d", 10000000 + i));
            managerDept.setLeader(leader);
            dto.setManagerDept(managerDept);

            dataList.add(dto);
        }

        // 分析字段信息
        System.out.println("分析 FlattenProperty 字段...");
        List<FlattenFieldProcessor.FlattenFieldInfo> fieldInfos =
            FlattenFieldProcessor.processFlattenFields(FlattenPropertyExampleDTO.class);

        System.out.println("找到 " + fieldInfos.size() + " 个字段：");
        for (FlattenFieldProcessor.FlattenFieldInfo info : fieldInfos) {
            System.out.println("  - " + info.getHeadName() + " (fieldPath: " + info.getFieldPath() +
                ", flatten: " + info.isFlatten() + ")");
        }
        System.out.println();

        // 导出方式1：使用 FlattenHeadGenerator（当前的方式，可能有问题）
        testWithHeadGenerator(dataList);

        // 导出方式2：手动转换为 Map（推荐方式）
        testWithMapConversion(dataList);

        System.out.println("========================================");
        System.out.println("测试完成！");
        System.out.println("========================================");
    }

    /**
     * 测试方式1：使用 FlattenHeadGenerator
     */
    private static void testWithHeadGenerator(List<FlattenPropertyExampleDTO> dataList) {
        System.out.println("【方式1】使用 FlattenHeadGenerator");
        System.out.println("----------------------------------------");

        String fileName = "flatten_property_headgen.xlsx";
        File file = new File(fileName);

        try {
            System.out.println("开始导出: " + file.getAbsolutePath());

            // 注意：这种方式可能不work，因为 EasyExcel 不认识 @FlattenProperty
            EasyExcel.write(file, FlattenPropertyExampleDTO.class)
                    .sheet("员工信息")
                    .doWrite(dataList);

            System.out.println("✓ 导出成功！文件大小: " + file.length() + " 字节");

            if (file.length() == 0) {
                System.err.println("✗ 错误：文件大小为 0！");
                System.err.println("  原因：EasyExcel 不认识 @FlattenProperty 注解");
            }

        } catch (Exception e) {
            System.err.println("✗ 导出失败！");
            System.err.println("  错误：" + e.getMessage());
        }

        System.out.println();
    }

    /**
     * 测试方式2：手动转换为 Map
     */
    private static void testWithMapConversion(List<FlattenPropertyExampleDTO> dataList) {
        System.out.println("【方式2】手动转换为 Map（推荐）");
        System.out.println("----------------------------------------");

        String fileName = "flatten_property_map.xlsx";
        File file = new File(fileName);

        try {
            System.out.println("开始导出: " + file.getAbsolutePath());

            // 1. 获取字段信息
            List<FlattenFieldProcessor.FlattenFieldInfo> fieldInfos =
                FlattenFieldProcessor.processFlattenFields(FlattenPropertyExampleDTO.class);

            // 2. 生成表头
            List<List<String>> head = new ArrayList<>();
            for (FlattenFieldProcessor.FlattenFieldInfo info : fieldInfos) {
                List<String> columnHead = new ArrayList<>();
                columnHead.add(info.getHeadName());
                head.add(columnHead);
            }

            // 3. 转换数据
            List<List<Object>> rows = new ArrayList<>();
            for (FlattenPropertyExampleDTO dto : dataList) {
                List<Object> row = new ArrayList<>();
                for (FlattenFieldProcessor.FlattenFieldInfo info : fieldInfos) {
                    Object value = FlattenFieldProcessor.extractValue(dto, info);
                    row.add(value != null ? value : "");
                }
                rows.add(row);
            }

            // 4. 导出
            EasyExcel.write(file)
                    .head(head)
                    .sheet("员工信息")
                    .doWrite(rows);

            System.out.println("✓ 导出成功！文件大小: " + file.length() + " 字节");

            if (file.length() == 0) {
                System.err.println("✗ 错误：文件大小为 0！");
            } else {
                System.out.println("✓ 文件内容正常");
                System.out.println();
                System.out.println("预期结果：");
                System.out.println("  - 包含基础字段：员工ID、员工姓名、年龄");
                System.out.println("  - 包含部门展开字段：部门-部门编码、部门-部门名称、部门-部门类型");
                System.out.println("  - 包含上级部门展开字段：上级部门-部门编码、上级部门-部门名称、上级部门-部门类型");
                System.out.println("  - 包含主管部门展开字段（递归）：主管-...");
            }

        } catch (Exception e) {
            System.err.println("✗ 导出失败！");
            e.printStackTrace();
        }

        System.out.println();
    }
}
