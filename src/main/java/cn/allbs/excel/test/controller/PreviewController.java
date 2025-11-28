package cn.allbs.excel.test.controller;

import cn.allbs.excel.test.entity.UserDTO;
import cn.allbs.excel.test.service.PreviewService;
import cn.allbs.excel.test.service.PreviewService.PreviewResult;
import cn.allbs.excel.test.service.PreviewService.PreviewSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据预览和确认导入控制器
 * 提供上传预览、分页查看、错误展示、确认导入功能
 */
@RestController
@RequestMapping("/api/preview")
@CrossOrigin(origins = "*")
public class PreviewController {

    @Autowired
    private PreviewService previewService;

    /**
     * 1. 上传Excel并预览（不入库）
     * 返回预览会话ID和第一页数据
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadAndPreview(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        try {
            PreviewResult<UserDTO> result = previewService.uploadAndPreview(file, UserDTO.class, pageSize);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("sessionId", result.getSessionId());
            response.put("totalRows", result.getTotalRows());
            response.put("totalPages", result.getTotalPages());
            response.put("currentPage", result.getCurrentPage());
            response.put("pageSize", result.getPageSize());
            response.put("data", result.getData());
            response.put("hasErrors", result.isHasErrors());
            response.put("errorCount", result.getErrorCount());
            response.put("validCount", result.getValidCount());
            response.put("errors", result.getErrors());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "上传失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 2. 获取指定页的数据
     */
    @GetMapping("/{sessionId}/page/{page}")
    public ResponseEntity<?> getPage(
            @PathVariable String sessionId,
            @PathVariable int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        try {
            PreviewResult<UserDTO> result = previewService.getPage(sessionId, page, pageSize, UserDTO.class);

            if (result == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "会话已过期，请重新上传文件");
                return ResponseEntity.badRequest().body(response);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("sessionId", result.getSessionId());
            response.put("totalRows", result.getTotalRows());
            response.put("totalPages", result.getTotalPages());
            response.put("currentPage", result.getCurrentPage());
            response.put("pageSize", result.getPageSize());
            response.put("data", result.getData());
            response.put("hasErrors", result.isHasErrors());
            response.put("errorCount", result.getErrorCount());
            response.put("validCount", result.getValidCount());
            response.put("errors", result.getErrors());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取数据失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 3. 获取所有错误数据（分页）
     */
    @GetMapping("/{sessionId}/errors")
    public ResponseEntity<?> getErrors(
            @PathVariable String sessionId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize
    ) {
        try {
            Map<String, Object> result = previewService.getErrors(sessionId, page, pageSize);

            if (result == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "会话已过期，请重新上传文件");
                return ResponseEntity.badRequest().body(response);
            }

            result.put("success", true);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取错误数据失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 4. 确认导入（用户确认后才真正入库）
     */
    @PostMapping("/{sessionId}/confirm")
    public ResponseEntity<?> confirmImport(
            @PathVariable String sessionId,
            @RequestParam(value = "skipErrors", defaultValue = "false") boolean skipErrors
    ) {
        try {
            Map<String, Object> result = previewService.confirmImport(sessionId, skipErrors, UserDTO.class);

            if (result == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "会话已过期，请重新上传文件");
                return ResponseEntity.badRequest().body(response);
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "确认导入失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 5. 取消预览（清除缓存）
     */
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<?> cancelPreview(@PathVariable String sessionId) {
        previewService.cancelPreview(sessionId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "预览已取消");
        return ResponseEntity.ok(response);
    }

    /**
     * 6. 获取预览会话状态
     */
    @GetMapping("/{sessionId}/status")
    public ResponseEntity<?> getSessionStatus(@PathVariable String sessionId) {
        PreviewSession session = previewService.getSession(sessionId);

        Map<String, Object> response = new HashMap<>();
        if (session == null) {
            response.put("success", false);
            response.put("exists", false);
            response.put("message", "会话不存在或已过期");
        } else {
            response.put("success", true);
            response.put("exists", true);
            response.put("totalRows", session.getTotalRows());
            response.put("errorCount", session.getErrorCount());
            response.put("validCount", session.getValidCount());
            response.put("createdAt", session.getCreatedAt());
        }

        return ResponseEntity.ok(response);
    }
}
