package site.auberginewly.todolist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Todo 待办事项实体类
 * 用于存储用户的待办事项信息
 */
@Entity
@Table(name = "todos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Todo {

    /**
     * 主键ID，自动生成
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 待办事项标题，不能为空
     */
    @Column(nullable = false, length = 255)
    private String title;

    /**
     * 待办事项描述，可以为空
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 完成状态，默认为 false（未完成）
     */
    @Column(nullable = false)
    private Boolean completed = false;

    /**
     * 优先级枚举：HIGH（高）、MEDIUM（中）、LOW（低）
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;

    /**
     * 截止日期，可以为空
     */
    @Column
    private LocalDate dueDate;

    /**
     * 关联的用户ID，外键
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 创建时间，自动生成
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间，自动更新
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 优先级枚举
     */
    public enum Priority {
        HIGH,   // 高优先级
        MEDIUM, // 中优先级
        LOW     // 低优先级
    }

    /**
     * 预持久化回调，设置创建时间和更新时间
     */
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    /**
     * 预更新回调，设置更新时间
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 