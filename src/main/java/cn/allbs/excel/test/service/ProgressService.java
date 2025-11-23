package cn.allbs.excel.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 进度推送服务 - 管理 SSE 连接
 */
@Service
public class ProgressService {

    private static final Logger log = LoggerFactory.getLogger(ProgressService.class);

    // 存储所有活跃的 SSE 连接，key 为 sessionId
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    /**
     * 创建新的 SSE 连接
     */
    public SseEmitter createEmitter(String sessionId) {
        // 设置超时时间为 10 分钟
        SseEmitter emitter = new SseEmitter(600000L);

        emitter.onCompletion(() -> {
            log.info("SSE 连接完成: {}", sessionId);
            emitters.remove(sessionId);
        });

        emitter.onTimeout(() -> {
            log.warn("SSE 连接超时: {}", sessionId);
            emitters.remove(sessionId);
        });

        emitter.onError((ex) -> {
            log.error("SSE 连接错误: {}", sessionId, ex);
            emitters.remove(sessionId);
        });

        emitters.put(sessionId, emitter);
        log.info("创建新的 SSE 连接: {}", sessionId);

        return emitter;
    }

    /**
     * 发送进度消息
     */
    public void sendProgress(String sessionId, ProgressMessage message) {
        SseEmitter emitter = emitters.get(sessionId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                    .name("progress")
                    .data(message));
            } catch (IOException e) {
                log.error("发送进度消息失败: {}", sessionId, e);
                emitters.remove(sessionId);
            }
        }
    }

    /**
     * 关闭 SSE 连接
     */
    public void closeEmitter(String sessionId) {
        SseEmitter emitter = emitters.remove(sessionId);
        if (emitter != null) {
            emitter.complete();
            log.info("关闭 SSE 连接: {}", sessionId);
        }
    }

    /**
     * 进度消息类
     */
    public static class ProgressMessage {
        private String type; // start, progress, complete, error
        private int currentRow;
        private int totalRows;
        private double percentage;
        private String sheetName;
        private String message;

        public ProgressMessage() {
        }

        public ProgressMessage(String type, int currentRow, int totalRows, double percentage, String sheetName, String message) {
            this.type = type;
            this.currentRow = currentRow;
            this.totalRows = totalRows;
            this.percentage = percentage;
            this.sheetName = sheetName;
            this.message = message;
        }

        public static ProgressMessage start(int totalRows, String sheetName) {
            return new ProgressMessage("start", 0, totalRows, 0.0, sheetName, "开始导出");
        }

        public static ProgressMessage progress(int currentRow, int totalRows, double percentage, String sheetName) {
            return new ProgressMessage("progress", currentRow, totalRows, percentage, sheetName,
                String.format("导出进度: %d/%d (%.2f%%)", currentRow, totalRows, percentage));
        }

        public static ProgressMessage complete(int totalRows, String sheetName) {
            return new ProgressMessage("complete", totalRows, totalRows, 100.0, sheetName, "导出完成");
        }

        public static ProgressMessage error(String sheetName, String errorMessage) {
            return new ProgressMessage("error", 0, 0, 0.0, sheetName, errorMessage);
        }

        // Getters and Setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getCurrentRow() {
            return currentRow;
        }

        public void setCurrentRow(int currentRow) {
            this.currentRow = currentRow;
        }

        public int getTotalRows() {
            return totalRows;
        }

        public void setTotalRows(int totalRows) {
            this.totalRows = totalRows;
        }

        public double getPercentage() {
            return percentage;
        }

        public void setPercentage(double percentage) {
            this.percentage = percentage;
        }

        public String getSheetName() {
            return sheetName;
        }

        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}