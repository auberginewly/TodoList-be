package site.auberginewly.todolist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.auberginewly.todolist.entity.User;
import site.auberginewly.todolist.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 认证服务类
 * 负责用户注册、登录、密码加密等认证相关业务逻辑
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户注册
     * 
     * @param username 用户名
     * @param password 原始密码
     * @return 注册成功的用户信息
     * @throws IllegalArgumentException 如果用户名已存在或参数无效
     */
    public User registerUser(String username, String password) {
        // 参数验证
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("密码长度不能少于6位");
        }

        // 检查用户名是否已存在
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("用户名已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(passwordEncoder.encode(password)); // 加密密码
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // 保存用户
        return userRepository.save(user);
    }

    /**
     * 用户登录
     * 
     * @param username 用户名
     * @param password 原始密码
     * @return 登录成功的用户信息
     * @throws BadCredentialsException 如果用户名或密码错误
     */
    public User loginUser(String username, String password) {
        // 参数验证
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }

        // 查找用户
        Optional<User> userOptional = userRepository.findByUsername(username.trim());
        if (userOptional.isEmpty()) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        User user = userOptional.get();

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        return user;
    }

    /**
     * 修改密码
     * 
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 更新后的用户信息
     * @throws BadCredentialsException 如果旧密码错误
     * @throws IllegalArgumentException 如果新密码无效
     */
    public User changePassword(Long userId, String oldPassword, String newPassword) {
        // 参数验证
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("新密码不能为空");
        }
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("新密码长度不能少于6位");
        }

        // 查找用户
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("用户不存在");
        }

        User user = userOptional.get();

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadCredentialsException("旧密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    /**
     * 根据用户ID获取用户信息
     * 
     * @param userId 用户ID
     * @return 用户信息
     * @throws IllegalArgumentException 如果用户不存在
     */
    public User getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("用户不存在");
        }
        return userOptional.get();
    }

    /**
     * 根据用户名获取用户信息
     * 
     * @param username 用户名
     * @return 用户信息，如果不存在则返回空
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 检查用户名是否已存在
     * 
     * @param username 用户名
     * @return true 如果用户名存在，false 如果不存在
     */
    public boolean isUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }
} 