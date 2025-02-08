package com.example.taskmanager.services;

import com.example.taskmanager.models.User;
import com.example.taskmanager.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("password123");
    }

    @Test
    void testSaveUser_Success() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals("testUser", savedUser.getUsername());
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSaveUser_UsernameAlreadyExists() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user));

        assertEquals("El usuario ya existe", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testFindByUsername_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername("testUser");

        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void testFindByUsername_NotFound() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findByUsername("unknownUser");

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findByUsername("unknownUser");
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, 
                () -> userService.loadUserByUsername("unknownUser"));

        assertEquals("Usuario no encontrado: unknownUser", exception.getMessage());
    }
}
