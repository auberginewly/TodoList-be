package site.auberginewly.todolist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import site.auberginewly.todolist.exception.ApiResponse;

/**
 * 测试控制器
 * 用于验证应用是否正常运行
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 健康检查接口
     * 
     * @return 应用状态信息
     */
    @GetMapping("/health")
    public ApiResponse<String> health() {
        return new ApiResponse<>(200, "服务正常", "OK");
    }

    /**
     * 简单测试接口
     * 
     * @return 测试信息
     */
    @GetMapping("/hello")
    public Map<String, String> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello from TodoList Backend!");
        response.put("status", "success");
        return response;
    }
} 