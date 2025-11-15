package cn.allbs.excel.test.controller;

import cn.allbs.excel.annotation.ExportExcel;
import cn.allbs.excel.annotation.ImportExcel;
import cn.allbs.excel.annotation.Sheet;
import cn.allbs.excel.test.entity.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 导入功能测试控制器
 */
@RestController
@RequestMapping("/api/import")
@CrossOrigin(origins = "*")
public class ImportController {

    /**
     * 0. 下载导入模板（先导出一个示例文件用于导入测试）
     */
    @GetMapping("/template")
    @ExportExcel(
        name = "导入模板",
        sheets = @Sheet(sheetName = "用户信息", clazz = UserDTO.class)
    )
    public List<UserDTO> downloadTemplate() {
        // 返回一个示例数据，方便用户了解格式
        UserDTO example = new UserDTO();
        example.setId(1L);
        example.setUsername("张三");
        example.setEmail("zhangsan@example.com");
        example.setCreateTime(java.time.LocalDateTime.now());
        example.setAge(25);
        example.setStatus("正常");
        return java.util.Collections.singletonList(example);
    }

    /**
     * 1. 基本导入
     */
    @PostMapping("/simple")
    public ResponseEntity<?> simpleImport(@ImportExcel List<UserDTO> users) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "导入成功");
        result.put("count", users.size());
        result.put("data", users);
        return ResponseEntity.ok(result);
    }

    /**
     * 2. 带验证的导入
     */
    @PostMapping("/validate")
    public ResponseEntity<?> importWithValidation(
        @ImportExcel List<UserDTO> users,
        BindingResult bindingResult
    ) {
        Map<String, Object> result = new HashMap<>();

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());

            result.put("success", false);
            result.put("message", "数据验证失败");
            result.put("errors", errors);
            return ResponseEntity.badRequest().body(result);
        }

        result.put("success", true);
        result.put("message", "导入成功");
        result.put("count", users.size());
        result.put("data", users);
        return ResponseEntity.ok(result);
    }

    /**
     * 3. 跳过空行导入
     */
    @PostMapping("/skip-empty")
    public ResponseEntity<?> importSkipEmpty(
        @ImportExcel(ignoreEmptyRow = true) List<UserDTO> users
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "导入成功（已跳过空行）");
        result.put("count", users.size());
        result.put("data", users);
        return ResponseEntity.ok(result);
    }

    /**
     * 4. 自定义字段名导入
     */
    @PostMapping("/custom-field")
    public ResponseEntity<?> importCustomField(
        @ImportExcel(fileName = "excelFile") List<UserDTO> users
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "导入成功（自定义字段名）");
        result.put("count", users.size());
        result.put("data", users);
        return ResponseEntity.ok(result);
    }
}
