package site.auberginewly.todolist.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA 配置类
 * 启用 JPA 审计功能，支持 @CreatedDate 和 @LastModifiedDate 注解
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
    // 配置类，启用 JPA 审计功能
} 