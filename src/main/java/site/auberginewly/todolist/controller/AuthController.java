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
     * @param body 包含 username 和 password
     * @return 注册成功的用户信息
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        User user = authService.registerUser(username, password);
        return ResponseEntity.ok(user);
    }

    /**
     * 用户登录
     * @param body 包含 username 和 password
     * @return 登录成功的 JWT Token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        
        try {
            // 使用 Spring Security 进行认证
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            
            // 生成 JWT Token
            String token = tokenProvider.generateToken(authentication);
            
            // 返回 Token
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("type", "Bearer");
            response.put("username", username);
            
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("用户名或密码错误");
        }
    }

    /**
     * 修改密码
     * @param body 包含 oldPassword 和 newPassword
     * @param principal 当前登录用户
     * @return 修改后的用户信息
     */
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body, Principal principal) {
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");
        // principal.getName() 通常为用户名，实际项目应通过 SecurityContext 获取用户ID
        User user = authService.getUserByUsername(principal.getName()).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        User updated = authService.changePassword(user.getId(), oldPassword, newPassword);
        return ResponseEntity.ok(updated);
    }

    /**
     * 获取当前登录用户信息
     * @param principal 当前登录用户
     * @return 用户信息
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        User user = authService.getUserByUsername(principal.getName()).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        return ResponseEntity.ok(user);
    }
} 