package site.auberginewly.todolist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.auberginewly.todolist.entity.Todo;

import java.time.LocalDate;
import java.util.List;

/**
 * Todo 数据访问层接口
 * 负责待办事项相关的数据库操作
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    /**
     * 根据用户ID查找该用户的所有待办事项
     */
    List<Todo> findByUserId(Long userId);

    /**
     * 根据用户ID和完成状态查找待办事项
     */
    List<Todo> findByUserIdAndCompleted(Long userId, Boolean completed);

    /**
     * 根据用户ID和完成状态查找待办事项（分页）
     */
    Page<Todo> findByUserIdAndCompleted(Long userId, Boolean completed, Pageable pageable);

    /**
     * 根据用户ID查找待办事项（分页）
     */
    Page<Todo> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据用户ID和标题模糊搜索待办事项（忽略大小写）
     */
    List<Todo> findByUserIdAndTitleContainingIgnoreCase(Long userId, String title);

    /**
     * 根据用户ID和标题模糊搜索待办事项（分页，忽略大小写）
     */
    Page<Todo> findByUserIdAndTitleContainingIgnoreCase(Long userId, String title, Pageable pageable);

    /**
     * 根据用户ID和优先级查找待办事项
     */
    List<Todo> findByUserIdAndPriority(Long userId, Todo.Priority priority);

    /**
     * 根据用户ID和截止日期查找待办事项
     */
    List<Todo> findByUserIdAndDueDate(Long userId, LocalDate dueDate);

    /**
     * 查找用户的所有已过期待办事项（截止日期早于今天且未完成）
     */
    @Query("SELECT t FROM Todo t WHERE t.userId = :userId AND t.completed = false AND t.dueDate < :today")
    List<Todo> findOverdueTodos(@Param("userId") Long userId, @Param("today") LocalDate today);

    /**
     * 统计用户的所有待办事项数量
     */
    long countByUserId(Long userId);

    /**
     * 统计用户已完成的待办事项数量
     */
    long countByUserIdAndCompletedTrue(Long userId);

    /**
     * 统计用户未完成的待办事项数量
     */
    long countByUserIdAndCompletedFalse(Long userId);
} 