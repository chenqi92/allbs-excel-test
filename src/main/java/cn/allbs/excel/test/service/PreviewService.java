package cn.allbs.excel.test.service;

import cn.allbs.excel.vo.ErrorMessage;
import cn.allbs.excel.vo.FieldError;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 数据预览服务
 * 支持上传预览、分页查看、校验错误展示、确认导入
 */
@Slf4j
@Service
public class PreviewService {

    /**
     * 预览会话缓存，key 为 sessionId
     */
    private final Map<String, PreviewSession> sessions = new ConcurrentHashMap<>();

    /**
     * 会话过期时间（30分钟）
     */
    private static final long SESSION_EXPIRE_MS = 30 * 60 * 1000;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 上传文件并预览
     */
    public <T> PreviewResult<T> uploadAndPreview(MultipartFile file, Class<T> clazz, int pageSize) throws IOException {
        String sessionId = UUID.randomUUID().toString();

        // 读取文件到内存
        byte[] fileBytes = file.getBytes();

        // 解析Excel并进行校验
        List<T> allData = new ArrayList<>();
        List<RowError> allErrors = new ArrayList<>();

        EasyExcel.read(new ByteArrayInputStream(fileBytes), clazz, new AnalysisEventListener<T>() {
            private int rowIndex = 1; // 从1开始（表头是0）

            @Override
            public void invoke(T data, AnalysisContext context) {
                rowIndex++;
                // 校验数据
                List<FieldError> fieldErrors = validateData(data, rowIndex);

                if (!fieldErrors.isEmpty()) {
                    RowError rowError = new RowError();
                    rowError.setRowIndex(rowIndex);
                    rowError.setData(data);
                    rowError.setFieldErrors(fieldErrors);
                    allErrors.add(rowError);
                }

                allData.add(data);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                log.info("Excel解析完成，共 {} 行数据，{} 行有错误", allData.size(), allErrors.size());
            }
        }).sheet().doRead();

        // 创建会话
        PreviewSession session = new PreviewSession();
        session.setSessionId(sessionId);
        session.setFileBytes(fileBytes);
        session.setAllData(allData);
        session.setAllErrors(allErrors);
        session.setTotalRows(allData.size());
        session.setErrorCount(allErrors.size());
        session.setValidCount(allData.size() - allErrors.size());
        session.setCreatedAt(LocalDateTime.now());

        // 构建错误行索引（快速查找）
        Set<Integer> errorRowIndexes = allErrors.stream()
                .map(RowError::getRowIndex)
                .collect(Collectors.toSet());
        session.setErrorRowIndexes(errorRowIndexes);

        sessions.put(sessionId, session);

        // 返回第一页数据
        return buildPreviewResult(session, 1, pageSize, clazz);
    }

    /**
     * 获取指定页数据
     */
    public <T> PreviewResult<T> getPage(String sessionId, int page, int pageSize, Class<T> clazz) {
        PreviewSession session = sessions.get(sessionId);
        if (session == null) {
            return null;
        }

        return buildPreviewResult(session, page, pageSize, clazz);
    }

    /**
     * 获取错误数据（分页）
     */
    public Map<String, Object> getErrors(String sessionId, int page, int pageSize) {
        PreviewSession session = sessions.get(sessionId);
        if (session == null) {
            return null;
        }

        List<RowError> allErrors = session.getAllErrors();
        int totalErrors = allErrors.size();
        int totalPages = (int) Math.ceil((double) totalErrors / pageSize);

        // 分页
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalErrors);

        List<RowError> pageErrors = fromIndex < totalErrors
                ? allErrors.subList(fromIndex, toIndex)
                : Collections.emptyList();

        // 构建返回结果
        List<Map<String, Object>> errorList = new ArrayList<>();
        for (RowError error : pageErrors) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("rowIndex", error.getRowIndex());
            errorMap.put("data", error.getData());
            errorMap.put("fieldErrors", error.getFieldErrors().stream()
                    .map(fe -> {
                        Map<String, Object> feMap = new HashMap<>();
                        feMap.put("fieldName", fe.getFieldName());
                        feMap.put("propertyName", fe.getPropertyName());
                        feMap.put("message", fe.getMessage());
                        feMap.put("errorType", fe.getErrorType());
                        feMap.put("fieldValue", fe.getFieldValue());
                        return feMap;
                    })
                    .collect(Collectors.toList()));
            errorList.add(errorMap);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("currentPage", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", totalPages);
        result.put("totalErrors", totalErrors);
        result.put("errors", errorList);

        return result;
    }

    /**
     * 确认导入
     */
    public <T> Map<String, Object> confirmImport(String sessionId, boolean skipErrors, Class<T> clazz) {
        PreviewSession session = sessions.get(sessionId);
        if (session == null) {
            return null;
        }

        List<?> allData = session.getAllData();
        Set<Integer> errorRowIndexes = session.getErrorRowIndexes();

        List<Object> importedData = new ArrayList<>();
        List<Object> skippedData = new ArrayList<>();

        // 处理数据
        for (int i = 0; i < allData.size(); i++) {
            int rowIndex = i + 2; // Excel行号（从2开始，1是表头）
            Object data = allData.get(i);

            if (errorRowIndexes.contains(rowIndex)) {
                if (skipErrors) {
                    skippedData.add(data);
                } else {
                    // 严格模式，有错误则拒绝导入
                    Map<String, Object> result = new HashMap<>();
                    result.put("success", false);
                    result.put("message", String.format("发现 %d 行数据有错误，请修正后重新导入或选择跳过错误", errorRowIndexes.size()));
                    result.put("errorCount", errorRowIndexes.size());
                    return result;
                }
            } else {
                importedData.add(data);
            }
        }

        // 模拟入库操作
        log.info("确认导入：成功导入 {} 条数据，跳过 {} 条错误数据", importedData.size(), skippedData.size());

        // 清除会话
        sessions.remove(sessionId);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", skipErrors && !skippedData.isEmpty()
                ? String.format("导入完成，成功 %d 条，跳过 %d 条错误数据", importedData.size(), skippedData.size())
                : String.format("导入成功，共 %d 条数据", importedData.size()));
        result.put("importedCount", importedData.size());
        result.put("skippedCount", skippedData.size());
        result.put("importedData", importedData.size() > 20 ? importedData.subList(0, 20) : importedData);
        result.put("hasMoreData", importedData.size() > 20);

        return result;
    }

    /**
     * 取消预览
     */
    public void cancelPreview(String sessionId) {
        sessions.remove(sessionId);
        log.info("取消预览会话: {}", sessionId);
    }

    /**
     * 获取会话信息
     */
    public PreviewSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    /**
     * 构建预览结果
     */
    @SuppressWarnings("unchecked")
    private <T> PreviewResult<T> buildPreviewResult(PreviewSession session, int page, int pageSize, Class<T> clazz) {
        List<?> allData = session.getAllData();
        int totalRows = allData.size();
        int totalPages = (int) Math.ceil((double) totalRows / pageSize);

        // 分页
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalRows);

        List<T> pageData = fromIndex < totalRows
                ? (List<T>) allData.subList(fromIndex, toIndex)
                : Collections.emptyList();

        // 获取当前页的错误信息
        Set<Integer> errorRowIndexes = session.getErrorRowIndexes();
        List<Map<String, Object>> pageErrors = new ArrayList<>();

        for (int i = fromIndex; i < toIndex && i < totalRows; i++) {
            int rowIndex = i + 2; // Excel行号
            if (errorRowIndexes.contains(rowIndex)) {
                // 找到对应的错误
                RowError rowError = session.getAllErrors().stream()
                        .filter(e -> e.getRowIndex() == rowIndex)
                        .findFirst()
                        .orElse(null);

                if (rowError != null) {
                    Map<String, Object> errorMap = new HashMap<>();
                    errorMap.put("rowIndex", rowIndex);
                    errorMap.put("dataIndex", i - fromIndex); // 在当前页的索引
                    errorMap.put("fieldErrors", rowError.getFieldErrors().stream()
                            .map(fe -> {
                                Map<String, Object> feMap = new HashMap<>();
                                feMap.put("fieldName", fe.getFieldName());
                                feMap.put("propertyName", fe.getPropertyName());
                                feMap.put("message", fe.getMessage());
                                feMap.put("errorType", fe.getErrorType());
                                feMap.put("fieldValue", fe.getFieldValue());
                                return feMap;
                            })
                            .collect(Collectors.toList()));
                    pageErrors.add(errorMap);
                }
            }
        }

        PreviewResult<T> result = new PreviewResult<>();
        result.setSessionId(session.getSessionId());
        result.setTotalRows(totalRows);
        result.setTotalPages(totalPages);
        result.setCurrentPage(page);
        result.setPageSize(pageSize);
        result.setData(pageData);
        result.setHasErrors(!session.getAllErrors().isEmpty());
        result.setErrorCount(session.getErrorCount());
        result.setValidCount(session.getValidCount());
        result.setErrors(pageErrors);

        return result;
    }

    /**
     * 校验数据
     */
    private <T> List<FieldError> validateData(T data, int rowIndex) {
        List<FieldError> errors = new ArrayList<>();

        Set<ConstraintViolation<T>> violations = validator.validate(data);
        for (ConstraintViolation<T> violation : violations) {
            FieldError fieldError = new FieldError();
            fieldError.setFieldName(violation.getPropertyPath().toString());
            fieldError.setPropertyName(violation.getPropertyPath().toString());
            fieldError.setMessage(violation.getMessage());
            fieldError.setFieldValue(violation.getInvalidValue() != null ? violation.getInvalidValue().toString() : null);

            // 根据注解类型判断错误类型
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
     * 定时清理过期会话
     */
    @Scheduled(fixedRate = 60000)
    public void cleanExpiredSessions() {
        long now = System.currentTimeMillis();
        sessions.entrySet().removeIf(entry -> {
            PreviewSession session = entry.getValue();
            long createdTime = java.sql.Timestamp.valueOf(session.getCreatedAt()).getTime();
            boolean expired = (now - createdTime) > SESSION_EXPIRE_MS;
            if (expired) {
                log.info("清理过期会话: {}", entry.getKey());
            }
            return expired;
        });
    }

    /**
     * 预览会话
     */
    @Data
    public static class PreviewSession {
        private String sessionId;
        private byte[] fileBytes;
        private List<?> allData;
        private List<RowError> allErrors;
        private Set<Integer> errorRowIndexes;
        private int totalRows;
        private int errorCount;
        private int validCount;
        private LocalDateTime createdAt;
    }

    /**
     * 行错误
     */
    @Data
    public static class RowError {
        private int rowIndex;
        private Object data;
        private List<FieldError> fieldErrors;
    }

    /**
     * 预览结果
     */
    @Data
    public static class PreviewResult<T> {
        private String sessionId;
        private int totalRows;
        private int totalPages;
        private int currentPage;
        private int pageSize;
        private List<T> data;
        private boolean hasErrors;
        private int errorCount;
        private int validCount;
        private List<Map<String, Object>> errors;
    }
}
