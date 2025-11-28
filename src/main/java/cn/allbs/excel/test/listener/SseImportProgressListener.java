package cn.allbs.excel.test.listener;

import cn.allbs.excel.listener.ImportProgressListener;
import cn.allbs.excel.test.service.ProgressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * SSE 导入进度监听器 - 将导入进度实时推送到前端
 *
 * @author ChenQi
 * @since 2025-11-28
 */
@Component
public class SseImportProgressListener implements ImportProgressListener {

    private static final Logger log = LoggerFactory.getLogger(SseImportProgressListener.class);

    @Autowired
    private ProgressService progressService;

    @Override
    public void onStart(int totalRows, String sheetName) {
        log.info("========== 开始导入 ==========");
        log.info("Sheet名称: {}", sheetName);
        log.info("总行数(预估): {}", totalRows);

        String sessionId = getSessionId();
        if (sessionId != null) {
            ProgressService.ProgressMessage message = new ProgressService.ProgressMessage(
                    "start", 0, totalRows, 0.0, sheetName, "开始导入"
            );
            progressService.sendProgress(sessionId, message);
        }
    }

    @Override
    public void onProgress(int currentRow, int totalRows, double percentage, String sheetName) {
        log.info("导入进度: {}/{} ({:.2f}%) - {}",
                currentRow, totalRows, percentage, sheetName);

        String sessionId = getSessionId();
        if (sessionId != null) {
            ProgressService.ProgressMessage message = new ProgressService.ProgressMessage(
                    "progress", currentRow, totalRows, percentage, sheetName,
                    String.format("导入进度: %d/%d (%.2f%%)", currentRow, totalRows, percentage)
            );
            progressService.sendProgress(sessionId, message);
        }
    }

    @Override
    public void onComplete(int totalRows, String sheetName) {
        log.info("========== 导入完成 ==========");
        log.info("Sheet名称: {}", sheetName);
        log.info("总行数: {}", totalRows);

        String sessionId = getSessionId();
        if (sessionId != null) {
            ProgressService.ProgressMessage message = new ProgressService.ProgressMessage(
                    "complete", totalRows, totalRows, 100.0, sheetName, "导入完成"
            );
            progressService.sendProgress(sessionId, message);

            // 延迟关闭连接，给前端足够时间主动关闭（避免触发前端 onerror）
            new Thread(() -> {
                try {
                    Thread.sleep(5000);  // 延长到 5 秒，让前端有足够时间主动关闭
                    progressService.closeEmitter(sessionId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }

    @Override
    public void onError(Exception exception, String sheetName) {
        log.error("========== 导入失败 ==========");
        log.error("Sheet名称: {}", sheetName);
        log.error("错误信息: {}", exception.getMessage(), exception);

        String sessionId = getSessionId();
        if (sessionId != null) {
            ProgressService.ProgressMessage message = new ProgressService.ProgressMessage(
                    "error", 0, 0, 0.0, sheetName, exception.getMessage()
            );
            progressService.sendProgress(sessionId, message);
            progressService.closeEmitter(sessionId);
        }
    }

    /**
     * 从请求参数中获取 sessionId
     */
    private String getSessionId() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                return attributes.getRequest().getParameter("sessionId");
            }
        } catch (Exception e) {
            log.warn("无法获取 sessionId: {}", e.getMessage());
        }
        return null;
    }
}
