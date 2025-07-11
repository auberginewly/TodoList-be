package site.auberginewly.todolist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.auberginewly.todolist.dto.TodoRequest;
import site.auberginewly.todolist.entity.Todo;
import site.auberginewly.todolist.repository.TodoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * TodoService 单元测试
 */
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    private Todo testTodo;
    private TodoRequest todoRequest;
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        testTodo = new Todo();
        testTodo.setId(1L);
        testTodo.setTitle("测试待办事项");
        testTodo.setDescription("这是一个测试待办事项");
        testTodo.setCompleted(false);
        testTodo.setPriority(Todo.Priority.MEDIUM);
        testTodo.setDueDate(LocalDate.now().plusDays(7));
        testTodo.setUserId(userId);
        testTodo.setCreatedAt(LocalDateTime.now());
        testTodo.setUpdatedAt(LocalDateTime.now());

        todoRequest = new TodoRequest();
        todoRequest.setTitle("新待办事项");
        todoRequest.setDescription("新的待办事项描述");
        todoRequest.setPriority("HIGH");
        todoRequest.setDueDate(LocalDate.now().plusDays(5));
    }

    @Test
    void createTodo_ValidRequest_ShouldCreateTodo() {
        // Given
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

        // When
        Todo result = todoService.createTodo(todoRequest, userId);

        // Then
        assertNotNull(result);
        assertEquals("测试待办事项", result.getTitle());
        assertEquals(userId, result.getUserId());
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void createTodo_NullRequest_ShouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.createTodo(null, userId);
        });
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    void createTodo_EmptyTitle_ShouldThrowException() {
        // Given
        todoRequest.setTitle("");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.createTodo(todoRequest, userId);
        });
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    void createTodo_NullUserId_ShouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.createTodo(todoRequest, null);
        });
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    void getTodos_ValidUserId_ShouldReturnTodos() {
        // Given
        List<Todo> todos = Arrays.asList(testTodo);
        when(todoRepository.findByUserId(anyLong())).thenReturn(todos);

        // When
        List<Todo> result = todoService.getTodos(userId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试待办事项", result.get(0).getTitle());
        verify(todoRepository).findByUserId(userId);
    }

    @Test
    void getTodos_NullUserId_ShouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.getTodos(null);
        });
        verify(todoRepository, never()).findByUserId(anyLong());
    }

    @Test
    void getTodo_ValidIdAndUserId_ShouldReturnTodo() {
        // Given
        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(testTodo));

        // When
        Todo result = todoService.getTodo(1L, userId);

        // Then
        assertNotNull(result);
        assertEquals("测试待办事项", result.getTitle());
        assertEquals(userId, result.getUserId());
        verify(todoRepository).findById(1L);
    }

    @Test
    void getTodo_TodoNotFound_ShouldThrowException() {
        // Given
        when(todoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.getTodo(1L, userId);
        });
        verify(todoRepository).findById(1L);
    }

    @Test
    void getTodo_WrongUserId_ShouldThrowException() {
        // Given
        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(testTodo));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.getTodo(1L, 999L); // 不同的用户ID
        });
        verify(todoRepository).findById(1L);
    }

    @Test
    void updateTodo_ValidRequest_ShouldUpdateTodo() {
        // Given
        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(testTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

        // When
        Todo result = todoService.updateTodo(1L, todoRequest, userId);

        // Then
        assertNotNull(result);
        verify(todoRepository).findById(1L);
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void updateTodo_NullRequest_ShouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.updateTodo(1L, null, userId);
        });
        verify(todoRepository, never()).findById(anyLong());
    }

    @Test
    void deleteTodo_ValidIdAndUserId_ShouldDeleteTodo() {
        // Given
        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(testTodo));
        doNothing().when(todoRepository).deleteById(anyLong());

        // When
        todoService.deleteTodo(1L, userId);

        // Then
        verify(todoRepository).findById(1L);
        verify(todoRepository).deleteById(1L);
    }

    @Test
    void deleteTodo_TodoNotFound_ShouldThrowException() {
        // Given
        when(todoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.deleteTodo(1L, userId);
        });
        verify(todoRepository).findById(1L);
        verify(todoRepository, never()).deleteById(anyLong());
    }

    @Test
    void toggleTodo_ValidIdAndUserId_ShouldToggleCompleted() {
        // Given
        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(testTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

        // When
        Todo result = todoService.toggleTodo(1L, userId);

        // Then
        assertNotNull(result);
        verify(todoRepository).findById(1L);
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void getOverdueTodos_ValidUserId_ShouldReturnOverdueTodos() {
        // Given
        List<Todo> overdueTodos = Arrays.asList(testTodo);
        when(todoRepository.findOverdueTodos(anyLong(), any(LocalDate.class)))
                .thenReturn(overdueTodos);

        // When
        List<Todo> result = todoService.getOverdueTodos(userId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(todoRepository).findOverdueTodos(anyLong(), any(LocalDate.class));
    }

    @Test
    void getOverdueTodos_NullUserId_ShouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.getOverdueTodos(null);
        });
        verify(todoRepository, never()).findOverdueTodos(anyLong(), any(LocalDate.class));
    }

    @Test
    void countTodosByUserId_ValidUserId_ShouldReturnCount() {
        // Given
        when(todoRepository.countByUserId(anyLong())).thenReturn(5L);

        // When
        long result = todoService.countTodosByUserId(userId);

        // Then
        assertEquals(5L, result);
        verify(todoRepository).countByUserId(userId);
    }

    @Test
    void countCompletedTodosByUserId_ValidUserId_ShouldReturnCount() {
        // Given
        when(todoRepository.countByUserIdAndCompletedTrue(anyLong())).thenReturn(3L);

        // When
        long result = todoService.countCompletedTodosByUserId(userId);

        // Then
        assertEquals(3L, result);
        verify(todoRepository).countByUserIdAndCompletedTrue(userId);
    }

    @Test
    void countIncompleteTodosByUserId_ValidUserId_ShouldReturnCount() {
        // Given
        when(todoRepository.countByUserIdAndCompletedFalse(anyLong())).thenReturn(2L);

        // When
        long result = todoService.countIncompleteTodosByUserId(userId);

        // Then
        assertEquals(2L, result);
        verify(todoRepository).countByUserIdAndCompletedFalse(userId);
    }
} 