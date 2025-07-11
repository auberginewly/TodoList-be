package site.auberginewly.todolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * JPA 审计配置类
 * 提供 AuditorAware 实现，保证 @CreatedDate/@LastModifiedDate 能自动赋值
 */
@Configuration
public class AuditorAwareConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        // 这里简单返回一个固定用户名，实际项目可集成 Spring Security 获取当前登录用户
        return () -> Optional.of("system");
    }
} 