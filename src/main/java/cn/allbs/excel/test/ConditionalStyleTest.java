package cn.allbs.excel.test;

import cn.allbs.excel.handle.ConditionalStyleWriteHandler;
import cn.allbs.excel.test.entity.ConditionalStyleDTO;
import com.alibaba.excel.EasyExcel;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 条件样式功能测试
 */
public class ConditionalStyleTest {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("条件样式功能测试");
        System.out.println("========================================");

        // 创建测试数据
        List<ConditionalStyleDTO> dataList = new ArrayList<>();

        String[] statuses = {"已完成", "进行中", "已取消"};
        String[] grades = {"A优秀", "B良好", "C及格", "D不及格"};

        for (int i = 1; i <= 20; i++) {
            ConditionalStyleDTO dto = new ConditionalStyleDTO();
            dto.setStudentName("学生" + i);
            dto.setScore(50 + (i * 5) % 50); // 分数在 50-100 之间
            dto.setStatus(statuses[i % 3]);
            dto.setSalesAmount(new BigDecimal(3000 + i * 500)); // 销售额 3000-13000
            dto.setGrade(grades[i % 4]);

            dataList.add(dto);
        }

        // 导出到文件
        String fileName = "conditional_style_test.xlsx";
        File file = new File(fileName);

        try {
            System.out.println("开始导出到文件: " + file.getAbsolutePath());
            System.out.println();

            EasyExcel.write(file, ConditionalStyleDTO.class)
                    .registerWriteHandler(new ConditionalStyleWriteHandler(ConditionalStyleDTO.class))
                    .sheet("条件样式示例")
                    .doWrite(dataList);

            System.out.println("✓ 导出成功！文件大小: " + file.length() + " 字节");

            if (file.length() == 0) {
                System.err.println("✗ 错误：文件大小为 0！");
            } else {
                System.out.println("✓ 文件内容正常");
                System.out.println();
                System.out.println("请打开文件检查样式效果：" + file.getAbsolutePath());
                System.out.println();
                System.out.println("预期样式效果：");
                System.out.println("----------------------------------------");
                System.out.println("【考试分数列】");
                System.out.println("  - 分数 >=90 : 绿色背景 + 加粗字体");
                System.out.println("  - 分数 >=60 : 黄色背景");
                System.out.println("  - 分数 <60  : 红色背景 + 白色字体");
                System.out.println();
                System.out.println("【任务状态列】");
                System.out.println("  - 状态=已完成 : 绿色背景 + 白色字体");
                System.out.println("  - 状态=进行中 : 黄色背景");
                System.out.println("  - 状态=已取消 : 灰色背景 + 白色字体");
                System.out.println();
                System.out.println("【销售额列】");
                System.out.println("  - 销售额 >=10000 : 金色背景 + 加粗字体");
                System.out.println("  - 销售额 >=5000  : 天蓝色背景");
                System.out.println();
                System.out.println("【等级列】");
                System.out.println("  - 等级以A开头 : 绿色背景 + 加粗字体");
                System.out.println("  - 等级以B开头 : 黄色背景");
                System.out.println("  - 等级以C开头 : 橙色背景");
                System.out.println("----------------------------------------");
            }

        } catch (Exception e) {
            System.err.println("✗ 导出失败！");
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("========================================");
        System.out.println("测试完成！");
        System.out.println("========================================");
    }
}
