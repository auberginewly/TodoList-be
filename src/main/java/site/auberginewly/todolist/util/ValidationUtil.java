package site.auberginewly.todolist.util;

import site.auberginewly.todolist.constant.AppConstants;

/**
 * 验证工具类
 * 提供常用的参数验证方法
 */
public class ValidationUtil {

    /**
     * 验证用户名
     */
    public static void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (username.length() > AppConstants.Auth.MAX_USERNAME_LENGTH) {
            throw new IllegalArgumentException("用户名长度不能超过" + AppConstants.Auth.MAX_USERNAME_LENGTH + "个字符");
        }
        // 可以添加更多用户名格式验证
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("用户名只能包含字母、数字和下划线");
        }
    }

    /**
     * 验证密码
     */
    public static void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        if (password.length() < AppConstants.Auth.MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("密码长度不能少于" + AppConstants.Auth.MIN_PASSWORD_LENGTH + "位");
        }
    }

    /**
     * 验证待办事项标题
     */
    public static void validateTodoTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("待办事项标题不能为空");
        }
        if (title.length() > AppConstants.Todo.MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("标题长度不能超过" + AppConstants.Todo.MAX_TITLE_LENGTH + "个字符");
        }
    }

    /**
     * 验证ID
     */
    public static void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID不能为空且必须大于0");
        }
    }

    /**
     * 验证字符串不为空
     */
    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + "不能为空");
        }
    }
} 