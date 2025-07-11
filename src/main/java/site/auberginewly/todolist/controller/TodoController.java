package site.auberginewly.todolist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.auberginewly.todolist.entity.Todo;
import site.auberginewly.todolist.service.TodoService;
import site.auberginewly.todolist.exception.ApiResponse;
import site.auberginewly.todolist.dto.TodoRequest;

import java.security.Principal;
import java.util.List;

/**
 * 待办事项控制器
 * 处理 /api/todos 相关的 CRUD 请求，所有方法都需要认证
 */
@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;
    private final site.auberginewly.todolist.service.AuthService authService;

    /**
     * 创建待办事项
     */
    @PostMapping
    public ApiResponse<Todo> createTodo(@RequestBody TodoRequest request, Principal principal) {
        Todo todo = todoService.createTodo(request, getUserIdFromPrincipal(principal));
        return new ApiResponse<>(200, "创建成功", todo);
    }

    /**
     * 获取当前用户所有待办事项（支持分页、筛选、搜索）
     */
    @GetMapping
    public ApiResponse<List<Todo>> getTodos(Principal principal) {
        List<Todo> todos = todoService.getTodos(getUserIdFromPrincipal(principal));
        return new ApiResponse<>(200, "获取成功", todos);
    }

    /**
     * 获取单个待办事项
     */
    @GetMapping("/{id}")
    public ApiResponse<Todo> getTodo(@PathVariable Long id, Principal principal) {
        Todo todo = todoService.getTodo(id, getUserIdFromPrincipal(principal));
        return new ApiResponse<>(200, "获取成功", todo);
    }

    /**
     * 更新待办事项
     */
    @PutMapping("/{id}")
    public ApiResponse<Todo> updateTodo(@PathVariable Long id, @RequestBody TodoRequest request, Principal principal) {
        Todo todo = todoService.updateTodo(id, request, getUserIdFromPrincipal(principal));
        return new ApiResponse<>(200, "更新成功", todo);
    }

    /**
     * 删除待办事项
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTodo(@PathVariable Long id, Principal principal) {
        todoService.deleteTodo(id, getUserIdFromPrincipal(principal));
        return new ApiResponse<>(200, "删除成功", null);
    }

    /**
     * 切换待办事项完成状态
     */
    @PostMapping("/{id}/toggle")
    public ApiResponse<Todo> toggleTodo(@PathVariable Long id, Principal principal) {
        Todo todo = todoService.toggleTodo(id, getUserIdFromPrincipal(principal));
        return new ApiResponse<>(200, "切换完成状态成功", todo);
    }

    /**
     * 获取已过期待办事项
     */
    @GetMapping("/overdue")
    public ApiResponse<List<Todo>> getOverdueTodos(Principal principal) {
        List<Todo> todos = todoService.getOverdueTodos(getUserIdFromPrincipal(principal));
        return new ApiResponse<>(200, "获取成功", todos);
    }

    /**
     * 从 Principal 中获取用户ID
     * 通过用户名查找用户来获取用户ID
     */
    private Long getUserIdFromPrincipal(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new IllegalArgumentException("用户未登录");
        }
        
        // 通过用户名查找用户，获取用户ID
        return authService.getUserByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"))
                .getId();
    }
} 