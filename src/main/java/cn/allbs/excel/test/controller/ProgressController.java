package cn.allbs.excel.test.controller;

import cn.allbs.excel.test.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 进度推送控制器 - 提供 SSE 端点
 */
@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = "*")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    /**
     * 创建 SSE 连接以接收实时进度
     * @param sessionId 会话ID，用于标识唯一的导出任务
     */
    @GetMapping("/subscribe/{sessionId}")
    public SseEmitter subscribe(@PathVariable String sessionId) {
        return progressService.createEmitter(sessionId);
    }
}