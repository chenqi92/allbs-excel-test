package cn.allbs.excel.test.controller;

import cn.allbs.excel.annotation.ExportExcel;
import cn.allbs.excel.annotation.ImportExcel;
import cn.allbs.excel.annotation.Sheet;
import cn.allbs.excel.test.entity.UserDTO;
import cn.allbs.excel.vo.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
     * 2. 带验证的导入（默认格式）
     */
    @PostMapping("/validate")
    public ResponseEntity<?> importWithValidation(
            @ImportExcel List<UserDTO> users,
            @RequestAttribute(name = "excelErrors", required = false) List<ErrorMessage> excelErrors
    ) {
        Map<String, Object> result = new HashMap<>();

        if (!CollectionUtils.isEmpty(excelErrors)) {
            // 使用默认格式化的错误消息
            List<String> errors = excelErrors.stream()
                    .flatMap(em -> em.getErrorMessages().stream()
                            .map(msg -> "行号 " + em.getLineNum() + "：" + msg))
                    .collect(Collectors.toList());

            result.put("success", false);
            result.put("message", "数据验证失败");
            result.put("errors", errors);
            result.put("validCount", users.size());
            result.put("errorCount", excelErrors.size());
            return ResponseEntity.badRequest().body(result);
        }

        result.put("success", true);
        result.put("message", "导入成功");
        result.put("count", users.size());
        result.put("data", users);
        return ResponseEntity.ok(result);
    }

    /**
     * 2-1. 带验证的导入（结构化错误信息）
     */
    @PostMapping("/validate-structured")
    public ResponseEntity<?> importWithValidationStructured(
            @ImportExcel List<UserDTO> users,
            @RequestAttribute(name = "excelErrors", required = false) List<ErrorMessage> excelErrors
    ) {
        Map<String, Object> result = new HashMap<>();

        if (!CollectionUtils.isEmpty(excelErrors)) {
            // 返回结构化的错误信息
            List<Map<String, Object>> structuredErrors = new java.util.ArrayList<>();

            excelErrors.forEach(em -> {
                em.getFieldErrors().forEach(fe -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("lineNum", em.getLineNum());
                    error.put("fieldName", fe.getFieldName());
                    error.put("propertyName", fe.getPropertyName());
                    error.put("errorType", fe.getErrorType());
                    error.put("message", fe.getMessage());
                    error.put("fullMessage", fe.getFullMessage());
                    error.put("fieldValue", fe.getFieldValue());
                    structuredErrors.add(error);
                });
            });

            result.put("success", false);
            result.put("message", "数据验证失败");
            result.put("errors", structuredErrors);
            result.put("validCount", users.size());
            result.put("errorCount", excelErrors.stream().mapToInt(ErrorMessage::getErrorCount).sum());
            return ResponseEntity.badRequest().body(result);
        }

        result.put("success", true);
        result.put("message", "导入成功");
        result.put("count", users.size());
        result.put("data", users);
        return ResponseEntity.ok(result);
    }

    /**
     * 2-2. 带验证的导入（按错误类型分组）
     */
    @PostMapping("/validate-grouped")
    public ResponseEntity<?> importWithValidationGrouped(
            @ImportExcel List<UserDTO> users,
            @RequestAttribute(name = "excelErrors", required = false) List<ErrorMessage> excelErrors
    ) {
        Map<String, Object> result = new HashMap<>();

        if (!CollectionUtils.isEmpty(excelErrors)) {
            // 按错误类型分组
            Map<String, List<String>> groupedErrors = new HashMap<>();
            groupedErrors.put("必填错误", new java.util.ArrayList<>());
            groupedErrors.put("格式错误", new java.util.ArrayList<>());
            groupedErrors.put("范围错误", new java.util.ArrayList<>());
            groupedErrors.put("其他错误", new java.util.ArrayList<>());

            excelErrors.forEach(em -> {
                // 必填错误
                em.getRequiredErrors().forEach(fe ->
                    groupedErrors.get("必填错误").add(
                        "行号 " + em.getLineNum() + "：" + fe.getFullMessage()
                    )
                );

                // 格式错误
                em.getFormatErrors().forEach(fe ->
                    groupedErrors.get("格式错误").add(
                        "行号 " + em.getLineNum() + "：" + fe.getFullMessage()
                    )
                );

                // 范围错误
                em.getRangeErrors().forEach(fe ->
                    groupedErrors.get("范围错误").add(
                        "行号 " + em.getLineNum() + "：" + fe.getFullMessage()
                    )
                );

                // 其他错误
                em.getFieldErrors().stream()
                    .filter(fe -> !fe.isRequiredError() && !fe.isFormatError() && !fe.isRangeError())
                    .forEach(fe ->
                        groupedErrors.get("其他错误").add(
                            "行号 " + em.getLineNum() + "：" + fe.getFullMessage()
                        )
                    );
            });

            // 移除空的分组
            groupedErrors.entrySet().removeIf(entry -> entry.getValue().isEmpty());

            result.put("success", false);
            result.put("message", "数据验证失败");
            result.put("groupedErrors", groupedErrors);
            result.put("validCount", users.size());
            result.put("errorCount", excelErrors.stream().mapToInt(ErrorMessage::getErrorCount).sum());
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
