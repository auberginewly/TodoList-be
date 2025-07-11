package site.auberginewly.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.auberginewly.todolist.entity.User;

import java.time.LocalDateTime;

/**
 * 用户响应DTO
 * 用于返回用户信息，不包含敏感数据如密码
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 从User实体转换为UserResponse
     */
    public static UserResponse fromUser(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
} 