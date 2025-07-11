package site.auberginewly.todolist.exception;

/**
 * 错误请求异常
 * 当请求参数无效或业务逻辑错误时抛出此异常
 */
public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String message) {
        super(message);
    }
    
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
} 