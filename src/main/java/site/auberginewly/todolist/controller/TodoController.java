package site.auberginewly.todolist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.auberginewly.todolist.entity.Todo;
import site.auberginewly.todolist.service.TodoService;

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
    public ResponseEntity<?> createTodo(@RequestBody Todo todo, Principal principal) {
        // 实际项目应通过 SecurityContext 获取用户ID，这里用用户名模拟
        Long userId = getUserIdFromPrincipal(principal);
        Todo created = todoService.createTodo(todo, userId);
        return ResponseEntity.ok(created);
    }

    /**
     * 获取当前用户所有待办事项（支持分页、筛选、搜索）
     */
    @GetMapping
    public ResponseEntity<?> getTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(required = false) String search,
            Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        Pageable pageable = PageRequest.of(page, size);
        Page<Todo> todos = todoService.getTodosByUserId(userId, status, search, pageable);
        return ResponseEntity.ok(todos);
    }

    /**
     * 获取单个待办事项
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTodoById(@PathVariable Long id, Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        Todo todo = todoService.getTodoByIdAndUserId(id, userId);
        return ResponseEntity.ok(todo);
    }

    /**
     * 更新待办事项
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable Long id, @RequestBody Todo todo, Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        Todo updated = todoService.updateTodo(id, todo, userId);
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除待办事项
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long id, Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        todoService.deleteTodo(id, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 切换待办事项完成状态
     */
    @PostMapping("/{id}/toggle")
    public ResponseEntity<?> toggleTodoStatus(@PathVariable Long id, Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        Todo updated = todoService.toggleTodoStatus(id, userId);
        return ResponseEntity.ok(updated);
    }

    /**
     * 获取已过期待办事项
     */
    @GetMapping("/overdue")
    public ResponseEntity<?> getOverdueTodos(Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        List<Todo> overdue = todoService.getOverdueTodos(userId);
        return ResponseEntity.ok(overdue);
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