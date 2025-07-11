package site.auberginewly.todolist.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import site.auberginewly.todolist.entity.User;
import site.auberginewly.todolist.security.JwtTokenProvider;
import site.auberginewly.todolist.service.AuthService;
import site.auberginewly.todolist.exception.ApiResponse;
import site.auberginewly.todolist.dto.RegisterRequest;
import site.auberginewly.todolist.dto.LoginRequest;
import site.auberginewly.todolist.dto.ChangePasswordRequest;
import site.auberginewly.todolist.dto.UserResponse;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 处理注册、登录、修改密码、获取当前用户信息等接口
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    /**
     * 用户注册
     * @param request 注册请求
     * @return 注册成功的用户信息
     */
    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody RegisterRequest request) {
        User user = authService.register(request);
        return new ApiResponse<>(200, "注册成功", user);
    }

    /**
     * 用户登录
     * @param body 包含 username 和 password
     * @return 登录成功的 JWT Token
     */
    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@RequestBody LoginRequest request) {
        User user = authService.login(request);
        // 创建认证对象
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            user.getUsername(), null, null);
        // 生成JWT token
        String token = tokenProvider.generateToken(authentication);
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        return new ApiResponse<>(200, "登录成功", data);
    }

    /**
     * 修改密码
     * @param body 包含 oldPassword 和 newPassword
     * @param principal 当前登录用户
     * @return 修改后的用户信息
     */
    @PutMapping("/password")
    public ApiResponse<Void> changePassword(@RequestBody ChangePasswordRequest request, Principal principal) {
        authService.changePassword(request, principal);
        return new ApiResponse<>(200, "密码修改成功", null);
    }

    /**
     * 获取当前登录用户信息
     * @param principal 当前登录用户
     * @return 用户信息
     */
    @GetMapping("/me")
    public ApiResponse<User> getCurrentUser(Principal principal) {
        User user = authService.getCurrentUser(principal);
        return new ApiResponse<>(200, "获取成功", user);
    }
} 