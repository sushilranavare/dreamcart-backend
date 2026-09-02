/*
 * Unit tests for AuthService: registration and login business logic.
 */
package com.dreamcart.backend.service;

import com.dreamcart.backend.dto.request.LoginRequest;
import com.dreamcart.backend.dto.request.RegisterRequest;
import com.dreamcart.backend.dto.response.AuthResponse;
import com.dreamcart.backend.entity.Role;
import com.dreamcart.backend.entity.User;
import com.dreamcart.backend.repository.RoleRepository;
import com.dreamcart.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private Role userRole;

    @BeforeEach
    void setUp() {

        registerRequest = new RegisterRequest();
        registerRequest.setFirstName("Jane");
        registerRequest.setLastName("Doe");
        registerRequest.setEmail("jane@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setPhoneNumber("1234567890");

        userRole = Role.builder().id(1L).name("USER").build();
    }

    @Test
    void register_savesNewUserAndReturnsToken() {

        when(userRepository.findByEmail(registerRequest.getEmail()))
                .thenReturn(Optional.empty());
        when(roleRepository.findByName("USER"))
                .thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(registerRequest.getPassword()))
                .thenReturn("encoded-password");
        when(jwtService.generateToken(registerRequest.getEmail()))
                .thenReturn("fake-jwt-token");

        AuthResponse response = authService.register(registerRequest);

        assertEquals("fake-jwt-token", response.getToken());
        assertEquals("USER", response.getRole());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_throwsWhenEmailAlreadyExists() {

        when(userRepository.findByEmail(registerRequest.getEmail()))
                .thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class,
                () -> authService.register(registerRequest));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_returnsTokenForCorrectCredentials() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("jane@example.com");
        loginRequest.setPassword("password123");

        User user = User.builder()
                .id(1L)
                .email("jane@example.com")
                .password("encoded-password")
                .role(userRole)
                .build();

        when(userRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
                .thenReturn(true);
        when(jwtService.generateToken(user.getEmail()))
                .thenReturn("fake-jwt-token");

        AuthResponse response = authService.login(loginRequest);

        assertEquals("fake-jwt-token", response.getToken());
        assertEquals("USER", response.getRole());
    }

    @Test
    void login_throwsWhenPasswordIsWrong() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("jane@example.com");
        loginRequest.setPassword("wrong-password");

        User user = User.builder()
                .email("jane@example.com")
                .password("encoded-password")
                .role(userRole)
                .build();

        when(userRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
                .thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> authService.login(loginRequest));
    }

    @Test
    void login_throwsWhenUserNotFound() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("missing@example.com");
        loginRequest.setPassword("password123");

        when(userRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> authService.login(loginRequest));
    }
}