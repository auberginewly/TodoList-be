package site.auberginewly.todolist.exception;

/**
 * 未授权异常
 * 当用户没有权限访问资源时抛出此异常
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
} 