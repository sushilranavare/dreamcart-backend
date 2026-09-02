/*
 * Unit tests for UserService: self-service profile updates and
 * password changes.
 */
package com.dreamcart.backend.service;

import com.dreamcart.backend.dto.request.ChangePasswordRequest;
import com.dreamcart.backend.dto.request.UpdateProfileRequest;
import com.dreamcart.backend.entity.User;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User existingUser;

    @BeforeEach
    void setUp() {

        existingUser = User.builder()
                .id(1L)
                .email("jane@example.com")
                .firstName("Jane")
                .lastName("Doe")
                .phoneNumber("1234567890")
                .password("encoded-old-password")
                .build();
    }

    @Test
    void updateProfile_updatesNameAndPhoneButNotEmail() {

        when(userRepository.findByEmail("jane@example.com"))
                .thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFirstName("Janet");
        request.setLastName("Smith");
        request.setPhoneNumber("9999999999");

        User updated = userService.updateProfile("jane@example.com", request);

        assertEquals("Janet", updated.getFirstName());
        assertEquals("Smith", updated.getLastName());
        assertEquals("9999999999", updated.getPhoneNumber());
        assertEquals("jane@example.com", updated.getEmail());
    }

    @Test
    void updateProfile_throwsWhenUserNotFound() {

        when(userRepository.findByEmail("ghost@example.com"))
                .thenReturn(Optional.empty());

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFirstName("Janet");
        request.setLastName("Smith");
        request.setPhoneNumber("9999999999");

        assertThrows(RuntimeException.class,
                () -> userService.updateProfile("ghost@example.com", request));
    }

    @Test
    void changePassword_encodesAndSavesNewPasswordWhenCurrentIsCorrect() {

        when(userRepository.findByEmail("jane@example.com"))
                .thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("old-password", existingUser.getPassword()))
                .thenReturn(true);
        when(passwordEncoder.encode("new-password"))
                .thenReturn("encoded-new-password");

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("old-password");
        request.setNewPassword("new-password");

        userService.changePassword("jane@example.com", request);

        assertEquals("encoded-new-password", existingUser.getPassword());
        verify(userRepository).save(existingUser);
    }

    @Test
    void changePassword_throwsWhenCurrentPasswordIsWrong() {

        when(userRepository.findByEmail("jane@example.com"))
                .thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("wrong-password", existingUser.getPassword()))
                .thenReturn(false);

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("wrong-password");
        request.setNewPassword("new-password");

        assertThrows(RuntimeException.class,
                () -> userService.changePassword("jane@example.com", request));

        verify(userRepository, never()).save(any(User.class));
    }
}