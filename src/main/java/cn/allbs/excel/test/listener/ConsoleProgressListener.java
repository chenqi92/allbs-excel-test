package cn.allbs.excel.test.listener;

import cn.allbs.excel.listener.ExportProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 控制台进度监听器 - 用于测试导出进度回调功能
 */
@Component
public class ConsoleProgressListener implements ExportProgressListener {

    private static final Logger log = LoggerFactory.getLogger(ConsoleProgressListener.class);

    @Override
    public void onStart(int totalRows, String sheetName) {
        log.info("========== 开始导出 ==========");
        log.info("Sheet名称: {}", sheetName);
        log.info("总行数: {}", totalRows);
    }

    @Override
    public void onProgress(int currentRow, int totalRows, double percentage, String sheetName) {
        log.info("导出进度: {}/{} ({:.2f}%) - {}",
            currentRow, totalRows, percentage, sheetName);
    }

    @Override
    public void onComplete(int totalRows, String sheetName) {
        log.info("========== 导出完成 ==========");
        log.info("Sheet名称: {}", sheetName);
        log.info("总行数: {}", totalRows);
    }

    @Override
    public void onError(Exception exception, String sheetName) {
        log.error("========== 导出失败 ==========");
        log.error("Sheet名称: {}", sheetName);
        log.error("错误信息: {}", exception.getMessage(), exception);
    }
}
