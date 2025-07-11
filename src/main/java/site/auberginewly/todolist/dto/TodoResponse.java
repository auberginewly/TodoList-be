package site.auberginewly.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.auberginewly.todolist.entity.Todo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 待办事项响应DTO
 * 用于返回待办事项信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoResponse {
    private Long id;
    private String title;
    private String description;
    private Boolean completed;
    private String priority;
    private LocalDate dueDate;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 从Todo实体转换为TodoResponse
     */
    public static TodoResponse fromTodo(Todo todo) {
        return new TodoResponse(
            todo.getId(),
            todo.getTitle(),
            todo.getDescription(),
            todo.getCompleted(),
            todo.getPriority().name(),
            todo.getDueDate(),
            todo.getUserId(),
            todo.getCreatedAt(),
            todo.getUpdatedAt()
        );
    }
} 