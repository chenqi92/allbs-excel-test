package cn.allbs.excel.test.listener;

import cn.allbs.excel.listener.ExportProgressListener;
import cn.allbs.excel.test.service.ProgressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * SSE 进度监听器 - 将导出进度实时推送到前端
 */
@Component
public class SseProgressListener implements ExportProgressListener {

    private static final Logger log = LoggerFactory.getLogger(SseProgressListener.class);

    @Autowired
    private ProgressService progressService;

    @Override
    public void onStart(int totalRows, String sheetName) {
        log.info("========== 开始导出 ==========");
        log.info("Sheet名称: {}", sheetName);
        log.info("总行数: {}", totalRows);

        String sessionId = getSessionId();
        if (sessionId != null) {
            progressService.sendProgress(sessionId,
                ProgressService.ProgressMessage.start(totalRows, sheetName));
        }
    }

    @Override
    public void onProgress(int currentRow, int totalRows, double percentage, String sheetName) {
        log.info("导出进度: {}/{} ({:.2f}%) - {}",
            currentRow, totalRows, percentage, sheetName);

        String sessionId = getSessionId();
        if (sessionId != null) {
            progressService.sendProgress(sessionId,
                ProgressService.ProgressMessage.progress(currentRow, totalRows, percentage, sheetName));
        }
    }

    @Override
    public void onComplete(int totalRows, String sheetName) {
        log.info("========== 导出完成 ==========");
        log.info("Sheet名称: {}", sheetName);
        log.info("总行数: {}", totalRows);

        String sessionId = getSessionId();
        if (sessionId != null) {
            progressService.sendProgress(sessionId,
                ProgressService.ProgressMessage.complete(totalRows, sheetName));
            // 延迟关闭连接，确保前端收到完成消息
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    progressService.closeEmitter(sessionId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }

    @Override
    public void onError(Exception exception, String sheetName) {
        log.error("========== 导出失败 ==========");
        log.error("Sheet名称: {}", sheetName);
        log.error("错误信息: {}", exception.getMessage(), exception);

        String sessionId = getSessionId();
        if (sessionId != null) {
            progressService.sendProgress(sessionId,
                ProgressService.ProgressMessage.error(sheetName, exception.getMessage()));
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
