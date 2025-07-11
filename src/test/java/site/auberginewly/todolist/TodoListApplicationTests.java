package site.auberginewly.todolist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * 主测试类
 * 测试Spring Boot应用上下文是否能正常启动
 */
@SpringBootTest
@ActiveProfiles("test")
class TodoListApplicationTests {

    @Test
    void contextLoads() {
        // 测试Spring Boot应用上下文是否能正常启动
    }
}
