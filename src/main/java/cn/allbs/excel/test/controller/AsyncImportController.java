package cn.allbs.excel.test.controller;

import cn.allbs.excel.test.entity.UserDTO;
import cn.allbs.excel.test.service.AsyncImportService;
import cn.allbs.excel.test.service.AsyncImportService.ImportTask;
import cn.allbs.excel.test.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 异步导入控制器
 * 提供异步导入任务管理、进度查询、SSE推送
 */
@RestController
@RequestMapping("/api/async-import")
@CrossOrigin(origins = "*")
public class AsyncImportController {

    @Autowired
    private AsyncImportService asyncImportService;

    @Autowired
    private ProgressService progressService;

    /**
     * 1. 提交异步导入任务
     */
    @PostMapping("/submit")
    public ResponseEntity<?> submitTask(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "skipErrors", defaultValue = "false") boolean skipErrors
    ) {
        try {
            String taskId = asyncImportService.submitTask(file, UserDTO.class, skipErrors);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("taskId", taskId);
            response.put("message", "任务已提交，请通过taskId查询进度");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "提交任务失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 2. 获取任务状态
     */
    @GetMapping("/task/{taskId}")
    public ResponseEntity<?> getTaskStatus(@PathVariable String taskId) {
        ImportTask task = asyncImportService.getTask(taskId);

        if (task == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "任务不存在");
            return ResponseEntity.badRequest().body(response);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("taskId", task.getTaskId());
        response.put("fileName", task.getFileName());
        response.put("status", task.getStatus().name());
        response.put("progress", task.getProgress());
        response.put("totalRows", task.getTotalRows());
        response.put("processedRows", task.getProcessedRows());
        response.put("successCount", task.getSuccessCount());
        response.put("errorCount", task.getErrorCount());
        response.put("errorMessage", task.getErrorMessage());
        response.put("createdAt", task.getCreatedAt());
        response.put("startedAt", task.getStartedAt());
        response.put("completedAt", task.getCompletedAt());

        // 如果有错误，返回部分错误详情
        if (task.getErrorList() != null && !task.getErrorList().isEmpty()) {
            response.put("errors", task.getErrorList().stream()
                    .limit(20)
                    .map(error -> {
                        Map<String, Object> errorMap = new HashMap<>();
                        errorMap.put("rowIndex", error.getRowIndex());
                        errorMap.put("data", error.getData());
                        errorMap.put("fieldErrors", error.getFieldErrors());
                        return errorMap;
                    })
                    .collect(Collectors.toList()));
            response.put("hasMoreErrors", task.getErrorList().size() > 20);
        }

        // 如果完成，返回部分成功数据
        if (task.getSuccessData() != null && !task.getSuccessData().isEmpty()) {
            response.put("previewData", task.getSuccessData());
            response.put("hasMoreData", task.getSuccessCount() > 20);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 3. 获取任务列表
     */
    @GetMapping("/tasks")
    public ResponseEntity<?> getTaskList() {
        List<ImportTask> tasks = asyncImportService.getTaskList();

        List<Map<String, Object>> taskList = tasks.stream()
                .map(task -> {
                    Map<String, Object> taskMap = new HashMap<>();
                    taskMap.put("taskId", task.getTaskId());
                    taskMap.put("fileName", task.getFileName());
                    taskMap.put("status", task.getStatus().name());
                    taskMap.put("progress", task.getProgress());
                    taskMap.put("totalRows", task.getTotalRows());
                    taskMap.put("processedRows", task.getProcessedRows());
                    taskMap.put("successCount", task.getSuccessCount());
                    taskMap.put("errorCount", task.getErrorCount());
                    taskMap.put("createdAt", task.getCreatedAt());
                    taskMap.put("completedAt", task.getCompletedAt());
                    return taskMap;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("tasks", taskList);
        response.put("total", taskList.size());

        return ResponseEntity.ok(response);
    }

    /**
     * 4. 订阅任务进度（SSE）
     */
    @GetMapping("/subscribe/{taskId}")
    public SseEmitter subscribeProgress(@PathVariable String taskId) {
        return progressService.createEmitter(taskId);
    }

    /**
     * 5. 取消任务
     */
    @PostMapping("/task/{taskId}/cancel")
    public ResponseEntity<?> cancelTask(@PathVariable String taskId) {
        boolean cancelled = asyncImportService.cancelTask(taskId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", cancelled);
        response.put("message", cancelled ? "任务已取消" : "无法取消任务（可能已在处理中）");

        return ResponseEntity.ok(response);
    }

    /**
     * 6. 删除任务
     */
    @DeleteMapping("/task/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable String taskId) {
        boolean deleted = asyncImportService.deleteTask(taskId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", deleted);
        response.put("message", deleted ? "任务已删除" : "任务不存在");

        return ResponseEntity.ok(response);
    }

    /**
     * 7. 清空所有已完成的任务
     */
    @DeleteMapping("/tasks/completed")
    public ResponseEntity<?> clearCompletedTasks() {
        List<ImportTask> tasks = asyncImportService.getTaskList();

        int cleared = 0;
        for (ImportTask task : tasks) {
            if (task.getStatus() == AsyncImportService.TaskStatus.COMPLETED ||
                    task.getStatus() == AsyncImportService.TaskStatus.FAILED ||
                    task.getStatus() == AsyncImportService.TaskStatus.CANCELLED) {
                asyncImportService.deleteTask(task.getTaskId());
                cleared++;
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", String.format("已清除 %d 个已完成的任务", cleared));
        response.put("clearedCount", cleared);

        return ResponseEntity.ok(response);
    }
}
