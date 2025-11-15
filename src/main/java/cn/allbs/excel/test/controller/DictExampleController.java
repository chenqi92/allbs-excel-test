package cn.allbs.excel.test.controller;

import cn.allbs.excel.annotation.ExportExcel;
import cn.allbs.excel.annotation.ImportExcel;
import cn.allbs.excel.annotation.Sheet;
import cn.allbs.excel.test.entity.DictExampleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 字典转换功能示例控制器
 * 演示如何使用 @ExcelDict 和 DictConverter 进行字典值转换
 */
@RestController
@RequestMapping("/api/dict")
@CrossOrigin(origins = "*")
public class DictExampleController {

    /**
     * 1. 导出字典转换示例
     * 数据库中存储的是字典值(0/1/2)，导出到Excel时自动转换为字典标签(女/男/未知、正常/禁用/锁定)
     */
    @GetMapping("/export")
    @ExportExcel(
        name = "字典转换示例",
        sheets = @Sheet(sheetName = "用户信息")
    )
    public List<DictExampleDTO> exportDictExample(@RequestParam(defaultValue = "10") int count) {
        List<DictExampleDTO> list = new ArrayList<>();
        Random random = new Random();

        String[] departments = {"技术部", "市场部", "人事部", "财务部", "运营部"};
        String[] usernames = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十"};

        for (int i = 1; i <= count; i++) {
            DictExampleDTO dto = new DictExampleDTO();
            dto.setId((long) i);
            dto.setUsername(usernames[random.nextInt(usernames.length)] + i);

            // 性别: 数据库存储 0/1/2，导出时会转换为 女/男/未知
            dto.setSex(String.valueOf(random.nextInt(3))); // 0, 1, 2

            // 状态: 数据库存储 0/1/2，导出时会转换为 正常/禁用/锁定
            dto.setStatus(String.valueOf(random.nextInt(3))); // 0, 1, 2

            dto.setDepartment(departments[random.nextInt(departments.length)]);
            dto.setCreateTime(LocalDateTime.now().minusDays(random.nextInt(365)));
            dto.setRemark("这是测试数据 " + i);

            list.add(dto);
        }

        return list;
    }

    /**
     * 2. 导出字典转换模板（用于导入测试）
     * 导出包含示例数据的模板，展示导出时的字典转换效果
     */
    @GetMapping("/template")
    @ExportExcel(
        name = "字典转换导入模板",
        sheets = @Sheet(sheetName = "用户信息", clazz = DictExampleDTO.class)
    )
    public List<DictExampleDTO> downloadDictTemplate() {
        List<DictExampleDTO> list = new ArrayList<>();

        // 示例1: 女性，正常状态
        DictExampleDTO example1 = new DictExampleDTO();
        example1.setId(1L);
        example1.setUsername("张小花");
        example1.setSex("0");  // 导出时显示: 女
        example1.setStatus("0");  // 导出时显示: 正常
        example1.setDepartment("技术部");
        example1.setCreateTime(LocalDateTime.now());
        example1.setRemark("女性用户示例");
        list.add(example1);

        // 示例2: 男性，禁用状态
        DictExampleDTO example2 = new DictExampleDTO();
        example2.setId(2L);
        example2.setUsername("李强");
        example2.setSex("1");  // 导出时显示: 男
        example2.setStatus("1");  // 导出时显示: 禁用
        example2.setDepartment("市场部");
        example2.setCreateTime(LocalDateTime.now());
        example2.setRemark("男性用户示例");
        list.add(example2);

        // 示例3: 未知性别，锁定状态
        DictExampleDTO example3 = new DictExampleDTO();
        example3.setId(3L);
        example3.setUsername("王系统");
        example3.setSex("2");  // 导出时显示: 未知
        example3.setStatus("2");  // 导出时显示: 锁定
        example3.setDepartment("运营部");
        example3.setCreateTime(LocalDateTime.now());
        example3.setRemark("系统用户示例");
        list.add(example3);

        return list;
    }

    /**
     * 3. 导入字典转换示例
     * Excel中填写的是字典标签(女/男/未知、正常/禁用/锁定)，导入时自动转换为字典值(0/1/2)
     */
    @PostMapping("/import")
    public ResponseEntity<?> importDictExample(@ImportExcel List<DictExampleDTO> users) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "字典转换导入成功");
        result.put("count", users.size());

        // 展示转换后的数据
        List<Map<String, Object>> convertedData = new ArrayList<>();
        for (DictExampleDTO user : users) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", user.getId());
            item.put("username", user.getUsername());
            item.put("sexValue", user.getSex());  // 转换后的值: 0/1/2
            item.put("statusValue", user.getStatus());  // 转换后的值: 0/1/2
            item.put("department", user.getDepartment());
            item.put("createTime", user.getCreateTime());
            item.put("remark", user.getRemark());
            convertedData.add(item);
        }

        result.put("data", convertedData);
        result.put("explanation", "Excel中的'女/男/未知'已转换为数据库值'0/1/2'，'正常/禁用/锁定'已转换为'0/1/2'");

        return ResponseEntity.ok(result);
    }

    /**
     * 4. 查看字典配置
     * 展示当前系统中配置的所有字典
     */
    @GetMapping("/config")
    public ResponseEntity<?> getDictConfig() {
        Map<String, Object> result = new HashMap<>();

        // 性别字典
        Map<String, String> sexDict = new LinkedHashMap<>();
        sexDict.put("0", "女");
        sexDict.put("1", "男");
        sexDict.put("2", "未知");

        // 状态字典
        Map<String, String> statusDict = new LinkedHashMap<>();
        statusDict.put("0", "正常");
        statusDict.put("1", "禁用");
        statusDict.put("2", "锁定");

        Map<String, Map<String, String>> allDict = new LinkedHashMap<>();
        allDict.put("sys_user_sex", sexDict);
        allDict.put("sys_user_status", statusDict);

        result.put("dictionaries", allDict);
        result.put("message", "字典配置信息");
        result.put("explanation", "这些字典用于Excel导入导出时的值转换");

        return ResponseEntity.ok(result);
    }
}
