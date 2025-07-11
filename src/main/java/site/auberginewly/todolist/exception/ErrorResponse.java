package site.auberginewly.todolist.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 错误响应 DTO
 * 统一的错误响应格式
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    /**
     * 时间戳
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    /**
     * HTTP 状态码
     */
    private int status;
    
    /**
     * 错误类型
     */
    private String error;
    
    /**
     * 错误消息
     */
    private String message;
    
    /**
     * 请求路径
     */
    private String path;
    
    /**
     * 构造函数
     */
    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
} 