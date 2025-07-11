package site.auberginewly.todolist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.auberginewly.todolist.entity.Todo;
import site.auberginewly.todolist.repository.TodoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 待办事项服务类
 * 负责待办事项的业务逻辑，确保用户只能操作自己的待办事项
 */
@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    /**
     * 创建待办事项
     * 
     * @param todo 待办事项信息
     * @param userId 用户ID
     * @return 创建成功的待办事项
     * @throws IllegalArgumentException 如果参数无效
     */
    public Todo createTodo(Todo todo, Long userId) {
        // 参数验证
        if (todo == null) {
            throw new IllegalArgumentException("待办事项不能为空");
        }
        if (todo.getTitle() == null || todo.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        // 设置用户ID，确保用户只能创建自己的待办事项
        todo.setUserId(userId);
        todo.setCompleted(false); // 新创建的待办事项默认为未完成

        return todoRepository.save(todo);
    }

    /**
     * 获取用户的所有待办事项（支持分页、筛选、搜索）
     * 
     * @param userId 用户ID
     * @param status 状态筛选（"all", "completed", "incomplete"）
     * @param searchTerm 搜索关键词
     * @param pageable 分页参数
     * @return 分页的待办事项列表
     */
    public Page<Todo> getTodosByUserId(Long userId, String status, String searchTerm, Pageable pageable) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        // 如果有搜索关键词，优先使用搜索功能
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            return todoRepository.findByUserIdAndTitleContainingIgnoreCase(userId, searchTerm.trim(), pageable);
        }

        // 根据状态筛选
        if ("completed".equals(status)) {
            return todoRepository.findByUserIdAndCompleted(userId, true, pageable);
        } else if ("incomplete".equals(status)) {
            return todoRepository.findByUserIdAndCompleted(userId, false, pageable);
        } else {
            // 默认返回所有待办事项
            return todoRepository.findByUserId(userId, pageable);
        }
    }

    /**
     * 获取用户的所有待办事项（不分页）
     * 
     * @param userId 用户ID
     * @return 待办事项列表
     */
    public List<Todo> getAllTodosByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return todoRepository.findByUserId(userId);
    }

    /**
     * 根据ID和用户ID获取单个待办事项
     * 
     * @param todoId 待办事项ID
     * @param userId 用户ID
     * @return 待办事项信息
     * @throws IllegalArgumentException 如果待办事项不存在或不属于该用户
     */
    public Todo getTodoByIdAndUserId(Long todoId, Long userId) {
        if (todoId == null) {
            throw new IllegalArgumentException("待办事项ID不能为空");
        }
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        Optional<Todo> todoOptional = todoRepository.findById(todoId);
        if (todoOptional.isEmpty()) {
            throw new IllegalArgumentException("待办事项不存在");
        }

        Todo todo = todoOptional.get();
        if (!todo.getUserId().equals(userId)) {
            throw new IllegalArgumentException("无权访问此待办事项");
        }

        return todo;
    }

    /**
     * 更新待办事项
     * 
     * @param todoId 待办事项ID
     * @param todoDetails 更新的待办事项信息
     * @param userId 用户ID
     * @return 更新后的待办事项
     * @throws IllegalArgumentException 如果待办事项不存在或不属于该用户
     */
    public Todo updateTodo(Long todoId, Todo todoDetails, Long userId) {
        if (todoId == null) {
            throw new IllegalArgumentException("待办事项ID不能为空");
        }
        if (todoDetails == null) {
            throw new IllegalArgumentException("更新信息不能为空");
        }
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        // 获取现有的待办事项
        Todo existingTodo = getTodoByIdAndUserId(todoId, userId);

        // 更新字段（只更新非空字段）
        if (todoDetails.getTitle() != null && !todoDetails.getTitle().trim().isEmpty()) {
            existingTodo.setTitle(todoDetails.getTitle().trim());
        }
        if (todoDetails.getDescription() != null) {
            existingTodo.setDescription(todoDetails.getDescription().trim());
        }
        if (todoDetails.getCompleted() != null) {
            existingTodo.setCompleted(todoDetails.getCompleted());
        }
        if (todoDetails.getPriority() != null) {
            existingTodo.setPriority(todoDetails.getPriority());
        }
        if (todoDetails.getDueDate() != null) {
            existingTodo.setDueDate(todoDetails.getDueDate());
        }

        return todoRepository.save(existingTodo);
    }

    /**
     * 删除待办事项
     * 
     * @param todoId 待办事项ID
     * @param userId 用户ID
     * @throws IllegalArgumentException 如果待办事项不存在或不属于该用户
     */
    public void deleteTodo(Long todoId, Long userId) {
        if (todoId == null) {
            throw new IllegalArgumentException("待办事项ID不能为空");
        }
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        // 验证待办事项是否存在且属于该用户
        getTodoByIdAndUserId(todoId, userId);

        todoRepository.deleteById(todoId);
    }

    /**
     * 切换待办事项的完成状态
     * 
     * @param todoId 待办事项ID
     * @param userId 用户ID
     * @return 更新后的待办事项
     */
    public Todo toggleTodoStatus(Long todoId, Long userId) {
        Todo todo = getTodoByIdAndUserId(todoId, userId);
        todo.setCompleted(!todo.getCompleted());
        return todoRepository.save(todo);
    }

    /**
     * 获取用户的已过期待办事项
     * 
     * @param userId 用户ID
     * @return 已过期的待办事项列表
     */
    public List<Todo> getOverdueTodos(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return todoRepository.findOverdueTodos(userId, LocalDate.now());
    }

    /**
     * 根据优先级获取待办事项
     * 
     * @param userId 用户ID
     * @param priority 优先级
     * @return 符合条件的待办事项列表
     */
    public List<Todo> getTodosByPriority(Long userId, Todo.Priority priority) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (priority == null) {
            throw new IllegalArgumentException("优先级不能为空");
        }
        return todoRepository.findByUserIdAndPriority(userId, priority);
    }

    /**
     * 根据截止日期获取待办事项
     * 
     * @param userId 用户ID
     * @param dueDate 截止日期
     * @return 符合条件的待办事项列表
     */
    public List<Todo> getTodosByDueDate(Long userId, LocalDate dueDate) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (dueDate == null) {
            throw new IllegalArgumentException("截止日期不能为空");
        }
        return todoRepository.findByUserIdAndDueDate(userId, dueDate);
    }

    /**
     * 统计用户的待办事项数量
     * 
     * @param userId 用户ID
     * @return 待办事项总数
     */
    public long countTodosByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return todoRepository.countByUserId(userId);
    }

    /**
     * 统计用户已完成的待办事项数量
     * 
     * @param userId 用户ID
     * @return 已完成的待办事项数量
     */
    public long countCompletedTodosByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return todoRepository.countByUserIdAndCompletedTrue(userId);
    }

    /**
     * 统计用户未完成的待办事项数量
     * 
     * @param userId 用户ID
     * @return 未完成的待办事项数量
     */
    public long countIncompleteTodosByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return todoRepository.countByUserIdAndCompletedFalse(userId);
    }
} 