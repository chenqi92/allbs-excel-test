package cn.allbs.excel.test.service;

import cn.allbs.excel.vo.FieldError;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 异步导入服务
 * 支持大文件异步导入、进度查询、任务管理
 */
@Slf4j
@Service
public class AsyncImportService {

    @Autowired
    private ProgressService progressService;

    /**
     * 任务缓存
     */
    private final Map<String, ImportTask> tasks = new ConcurrentHashMap<>();

    /**
     * 任务过期时间（1小时）
     */
    private static final long TASK_EXPIRE_MS = 60 * 60 * 1000;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 提交异步导入任务
     */
    public String submitTask(MultipartFile file, Class<?> clazz, boolean skipErrors) throws Exception {
        String taskId = UUID.randomUUID().toString();

        // 读取文件到内存（避免异步时文件被清理）
        byte[] fileBytes = file.getBytes();
        String fileName = file.getOriginalFilename();

        // 创建任务
        ImportTask task = new ImportTask();
        task.setTaskId(taskId);
        task.setFileName(fileName);
        task.setFileBytes(fileBytes);
        task.setClazz(clazz);
        task.setSkipErrors(skipErrors);
        task.setStatus(TaskStatus.PENDING);
        task.setProgress(0);
        task.setTotalRows(0);
        task.setProcessedRows(0);
        task.setSuccessCount(0);
        task.setErrorCount(0);
        task.setCreatedAt(LocalDateTime.now());

        tasks.put(taskId, task);

        // 启动异步处理
        processTaskAsync(taskId);

        log.info("提交异步导入任务: taskId={}, fileName={}", taskId, fileName);

        return taskId;
    }

    /**
     * 异步处理任务
     */
    @Async
    public void processTaskAsync(String taskId) {
        ImportTask task = tasks.get(taskId);
        if (task == null) {
            log.warn("任务不存在: {}", taskId);
            return;
        }

        try {
            task.setStatus(TaskStatus.PROCESSING);
            task.setStartedAt(LocalDateTime.now());

            // 发送开始消息
            progressService.sendProgress(taskId, ProgressService.ProgressMessage.start(0, "异步导入"));

            // 解析Excel
            List<Object> successData = new ArrayList<>();
            List<TaskError> errorList = new ArrayList<>();

            EasyExcel.read(new ByteArrayInputStream(task.getFileBytesInternal()), task.getClazz(),
                    new AnalysisEventListener<Object>() {
                        private int rowIndex = 1;
                        private int totalRows = 0;

                        @Override
                        public void invokeHead(Map<Integer, com.alibaba.excel.metadata.data.ReadCellData<?>> headMap,
                                               AnalysisContext context) {
                            // 尝试获取总行数
                            try {
                                if (context.readSheetHolder() != null &&
                                        context.readSheetHolder().getApproximateTotalRowNumber() != null) {
                                    totalRows = context.readSheetHolder().getApproximateTotalRowNumber() - 1;
                                    task.setTotalRows(totalRows);
                                }
                            } catch (Exception e) {
                                log.debug("无法获取总行数", e);
                            }
                        }

                        @Override
                        public void invoke(Object data, AnalysisContext context) {
                            rowIndex++;
                            task.setProcessedRows(rowIndex - 1);

                            // 校验数据
                            List<FieldError> fieldErrors = validateData(data, rowIndex);

                            if (!fieldErrors.isEmpty()) {
                                TaskError error = new TaskError();
                                error.setRowIndex(rowIndex);
                                error.setData(data);
                                error.setFieldErrors(fieldErrors);
                                errorList.add(error);
                                task.setErrorCount(errorList.size());

                                if (!task.isSkipErrors()) {
                                    // 严格模式，记录但不添加到成功列表
                                }
                            } else {
                                successData.add(data);
                                task.setSuccessCount(successData.size());
                            }

                            // 更新进度
                            if (totalRows > 0) {
                                double progress = (rowIndex - 1) * 100.0 / totalRows;
                                task.setProgress(progress);

                                // 每1%更新一次
                                if ((rowIndex - 1) % Math.max(1, totalRows / 100) == 0) {
                                    progressService.sendProgress(taskId,
                                            ProgressService.ProgressMessage.progress(rowIndex - 1, totalRows, progress, "异步导入"));
                                }
                            }

                            // 模拟处理延迟（可删除）
                            // try { Thread.sleep(1); } catch (InterruptedException ignored) {}
                        }

                        @Override
                        public void doAfterAllAnalysed(AnalysisContext context) {
                            task.setTotalRows(rowIndex - 1);
                            task.setProcessedRows(rowIndex - 1);
                            task.setProgress(100);
                        }
                    }).sheet().doRead();

            // 处理结果
            if (!task.isSkipErrors() && !errorList.isEmpty()) {
                // 严格模式下有错误
                task.setStatus(TaskStatus.FAILED);
                task.setErrorMessage(String.format("发现 %d 行数据有错误", errorList.size()));
            } else {
                // 模拟入库
                log.info("异步导入完成：成功 {} 条，跳过 {} 条错误", successData.size(), errorList.size());
                task.setStatus(TaskStatus.COMPLETED);
                task.setSuccessCount(successData.size());
            }

            task.setErrorList(errorList);
            task.setSuccessData(successData);
            task.setCompletedAt(LocalDateTime.now());

            // 清理文件字节释放内存
            task.clearFileBytes();

            // 发送完成消息
            progressService.sendProgress(taskId,
                    ProgressService.ProgressMessage.complete(task.getTotalRows(), "异步导入"));

        } catch (Exception e) {
            log.error("异步导入任务失败: {}", taskId, e);
            task.setStatus(TaskStatus.FAILED);
            task.setErrorMessage("导入失败: " + e.getMessage());
            task.setCompletedAt(LocalDateTime.now());

            // 清理文件字节释放内存
            task.clearFileBytes();

            progressService.sendProgress(taskId,
                    ProgressService.ProgressMessage.error("异步导入", e.getMessage()));
        }
    }

    /**
     * 获取任务状态
     */
    public ImportTask getTask(String taskId) {
        return tasks.get(taskId);
    }

    /**
     * 获取任务列表
     */
    public List<ImportTask> getTaskList() {
        return new ArrayList<>(tasks.values());
    }

    /**
     * 取消任务
     */
    public boolean cancelTask(String taskId) {
        ImportTask task = tasks.get(taskId);
        if (task != null && task.getStatus() == TaskStatus.PENDING) {
            task.setStatus(TaskStatus.CANCELLED);
            task.setCompletedAt(LocalDateTime.now());
            return true;
        }
        return false;
    }

    /**
     * 删除任务
     */
    public boolean deleteTask(String taskId) {
        return tasks.remove(taskId) != null;
    }

    /**
     * 校验数据
     */
    private List<FieldError> validateData(Object data, int rowIndex) {
        List<FieldError> errors = new ArrayList<>();

        @SuppressWarnings("unchecked")
        Set<ConstraintViolation<Object>> violations = validator.validate(data);

        for (ConstraintViolation<Object> violation : violations) {
            FieldError fieldError = new FieldError();
            fieldError.setFieldName(violation.getPropertyPath().toString());
            fieldError.setPropertyName(violation.getPropertyPath().toString());
            fieldError.setMessage(violation.getMessage());
            fieldError.setFieldValue(violation.getInvalidValue() != null ? violation.getInvalidValue().toString() : null);

            String annotationType = violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
            if ("NotNull".equals(annotationType) || "NotBlank".equals(annotationType) || "NotEmpty".equals(annotationType)) {
                fieldError.setErrorType("REQUIRED");
            } else if ("Email".equals(annotationType) || "Pattern".equals(annotationType)) {
                fieldError.setErrorType("FORMAT");
            } else if ("Min".equals(annotationType) || "Max".equals(annotationType) ||
                    "Size".equals(annotationType) || "Range".equals(annotationType)) {
                fieldError.setErrorType("RANGE");
            } else {
                fieldError.setErrorType("VALIDATION");
            }

            errors.add(fieldError);
        }

        return errors;
    }

    /**
     * 定时清理过期任务
     */
    @Scheduled(fixedRate = 300000)
    public void cleanExpiredTasks() {
        long now = System.currentTimeMillis();
        tasks.entrySet().removeIf(entry -> {
            ImportTask task = entry.getValue();
            if (task.getCompletedAt() != null) {
                long completedTime = java.sql.Timestamp.valueOf(task.getCompletedAt()).getTime();
                boolean expired = (now - completedTime) > TASK_EXPIRE_MS;
                if (expired) {
                    log.info("清理过期任务: {}", entry.getKey());
                }
                return expired;
            }
            return false;
        });
    }

    /**
     * 任务状态枚举
     */
    public enum TaskStatus {
        PENDING,        // 等待处理
        PROCESSING,     // 处理中
        COMPLETED,      // 完成
        FAILED,         // 失败
        CANCELLED       // 已取消
    }

    /**
     * 导入任务
     */
    @Data
    public static class ImportTask {
        private String taskId;
        private String fileName;
        private byte[] fileBytes;
        private Class<?> clazz;
        private boolean skipErrors;
        private TaskStatus status;
        private double progress;
        private int totalRows;
        private int processedRows;
        private int successCount;
        private int errorCount;
        private String errorMessage;
        private List<TaskError> errorList;
        private List<Object> successData;
        private LocalDateTime createdAt;
        private LocalDateTime startedAt;
        private LocalDateTime completedAt;

        /**
         * 内部方法获取文件字节（用于处理）
         */
        public byte[] getFileBytesInternal() {
            return fileBytes;
        }

        /**
         * 防止序列化文件内容（JSON返回时不包含文件）
         */
        public byte[] getFileBytes() {
            return null;
        }

        public List<Object> getSuccessData() {
            return successData != null && successData.size() > 20
                    ? successData.subList(0, 20)
                    : successData;
        }

        public List<TaskError> getErrorList() {
            return errorList != null && errorList.size() > 50
                    ? errorList.subList(0, 50)
                    : errorList;
        }

        /**
         * 清理文件字节释放内存
         */
        public void clearFileBytes() {
            this.fileBytes = null;
        }
    }

    /**
     * 任务错误
     */
    @Data
    public static class TaskError {
        private int rowIndex;
        private Object data;
        private List<FieldError> fieldErrors;
    }
}
