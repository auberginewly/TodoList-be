package site.auberginewly.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.auberginewly.todolist.entity.Todo;

import java.time.LocalDate;

/**
 * 待办事项请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoRequest {
    private String title;
    private String description;
    private String priority = Todo.Priority.MEDIUM.name();
    private LocalDate dueDate;
} 