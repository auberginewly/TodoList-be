package site.auberginewly.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.auberginewly.todolist.entity.User;

import java.util.Optional;

/**
 * User 数据访问层接口
 * 负责用户相关的数据库操作
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户
     * 用于用户登录和注册时的用户名验证
     * 
     * @param username 用户名
     * @return 用户信息，如果不存在则返回空
     */
    Optional<User> findByUsername(String username);

    /**
     * 检查用户名是否存在
     * 用于注册时验证用户名是否已被使用
     * 
     * @param username 用户名
     * @return true 如果用户名存在，false 如果不存在
     */
    boolean existsByUsername(String username);
} 