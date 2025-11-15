package cn.allbs.excel.test;

import cn.allbs.excel.ExportExcelAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Excel 测试应用启动类
 */
@SpringBootApplication
@Import(ExportExcelAutoConfiguration.class)
public class ExcelTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExcelTestApplication.class, args);
        System.out.println("========================================");
        System.out.println("   allbs-excel 测试应用启动成功！");
        System.out.println("   访问地址: http://localhost:8080");
        System.out.println("========================================");
    }
}
