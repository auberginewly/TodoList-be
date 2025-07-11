package site.auberginewly.todolist.constant;

/**
 * 应用常量类
 * 统一管理应用中的常量值
 */
public class AppConstants {
    
    /**
     * 认证相关常量
     */
    public static class Auth {
        public static final String TOKEN_PREFIX = "Bearer ";
        public static final String TOKEN_HEADER = "Authorization";
        public static final int MIN_PASSWORD_LENGTH = 6;
        public static final int MAX_USERNAME_LENGTH = 50;
    }
    
    /**
     * 分页相关常量
     */
    public static class Pagination {
        public static final int DEFAULT_PAGE_SIZE = 20;
        public static final int MAX_PAGE_SIZE = 100;
    }
    
    /**
     * 待办事项相关常量
     */
    public static class Todo {
        public static final int MAX_TITLE_LENGTH = 255;
        public static final String DEFAULT_PRIORITY = "MEDIUM";
    }
    
    /**
     * 状态常量
     */
    public static class Status {
        public static final String ALL = "all";
        public static final String COMPLETED = "completed";
        public static final String INCOMPLETE = "incomplete";
    }
    
    /**
     * 错误消息常量
     */
    public static class ErrorMessage {
        public static final String USER_NOT_FOUND = "用户不存在";
        public static final String TODO_NOT_FOUND = "待办事项不存在";
        public static final String UNAUTHORIZED_ACCESS = "无权访问此资源";
        public static final String INVALID_CREDENTIALS = "用户名或密码错误";
        public static final String USERNAME_EXISTS = "用户名已存在";
        public static final String OLD_PASSWORD_INCORRECT = "旧密码错误";
    }
} 