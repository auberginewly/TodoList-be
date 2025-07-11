package site.auberginewly.todolist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import site.auberginewly.todolist.dto.ChangePasswordRequest;
import site.auberginewly.todolist.dto.LoginRequest;
import site.auberginewly.todolist.dto.RegisterRequest;
import site.auberginewly.todolist.entity.User;
import site.auberginewly.todolist.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * AuthService 单元测试
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Principal principal;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private ChangePasswordRequest changePasswordRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setOldPassword("oldPassword");
        changePasswordRequest.setNewPassword("newPassword123");
    }

    @Test
    void register_ValidRequest_ShouldCreateUser() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = authService.register(registerRequest);

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).existsByUsername("newuser");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_UsernameExists_ShouldThrowException() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            authService.register(registerRequest);
        });
        verify(userRepository).existsByUsername("newuser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_EmptyUsername_ShouldThrowException() {
        // Given
        registerRequest.setUsername("");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            authService.register(registerRequest);
        });
        verify(userRepository, never()).existsByUsername(anyString());
    }

    @Test
    void register_EmptyPassword_ShouldThrowException() {
        // Given
        registerRequest.setPassword("");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            authService.register(registerRequest);
        });
        verify(userRepository, never()).existsByUsername(anyString());
    }

    @Test
    void register_ShortPassword_ShouldThrowException() {
        // Given
        registerRequest.setPassword("123");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            authService.register(registerRequest);
        });
        verify(userRepository, never()).existsByUsername(anyString());
    }

    @Test
    void login_ValidCredentials_ShouldReturnUser() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // When
        User result = authService.login(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).matches("password123", "encodedPassword");
    }

    @Test
    void login_UserNotFound_ShouldThrowException() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(org.springframework.security.authentication.BadCredentialsException.class, () -> {
            authService.login(loginRequest);
        });
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void login_WrongPassword_ShouldThrowException() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // When & Then
        assertThrows(org.springframework.security.authentication.BadCredentialsException.class, () -> {
            authService.login(loginRequest);
        });
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).matches("password123", "encodedPassword");
    }

    @Test
    void changePassword_ValidRequest_ShouldUpdatePassword() {
        // Given
        when(principal.getName()).thenReturn("testuser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        authService.changePassword(changePasswordRequest, principal);

        // Then
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).matches("oldPassword", "encodedPassword");
        verify(passwordEncoder).encode("newPassword123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void changePassword_WrongOldPassword_ShouldThrowException() {
        // Given
        when(principal.getName()).thenReturn("testuser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // When & Then
        assertThrows(org.springframework.security.authentication.BadCredentialsException.class, () -> {
            authService.changePassword(changePasswordRequest, principal);
        });
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).matches("oldPassword", "encodedPassword");
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void getCurrentUser_ValidPrincipal_ShouldReturnUser() {
        // Given
        when(principal.getName()).thenReturn("testuser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));

        // When
        User result = authService.getCurrentUser(principal);

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void getCurrentUser_UserNotFound_ShouldThrowException() {
        // Given
        when(principal.getName()).thenReturn("testuser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            authService.getCurrentUser(principal);
        });
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void getUserByUsername_UserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = authService.getUserByUsername("testuser");

        // Then
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void getUserByUsername_UserNotExists_ShouldReturnEmpty() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When
        Optional<User> result = authService.getUserByUsername("nonexistent");

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findByUsername("nonexistent");
    }

    @Test
    void isUsernameExists_UserExists_ShouldReturnTrue() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When
        boolean result = authService.isUsernameExists("testuser");

        // Then
        assertTrue(result);
        verify(userRepository).existsByUsername("testuser");
    }

    @Test
    void isUsernameExists_UserNotExists_ShouldReturnFalse() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        // When
        boolean result = authService.isUsernameExists("nonexistent");

        // Then
        assertFalse(result);
        verify(userRepository).existsByUsername("nonexistent");
    }
} 