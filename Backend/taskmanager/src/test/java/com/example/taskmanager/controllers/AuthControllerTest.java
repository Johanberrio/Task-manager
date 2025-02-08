package com.example.taskmanager.controllers;

import com.example.taskmanager.models.User;
import com.example.taskmanager.services.JwtService;
import com.example.taskmanager.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("password123");
    }

    @Test
    void testRegisterUser_Success() {
        when(userService.findByUsername("testUser")).thenReturn(Optional.empty());

        ResponseEntity<?> response = authController.register(testUser);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Usuario registrado exitosamente", response.getBody());
    }

    @Test
    void testRegisterUser_Failure_UserAlreadyExists() {
        when(userService.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        ResponseEntity<?> response = authController.register(testUser);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("El usuario ya existe", response.getBody());
    }

    @Test
    void testLogin_Success() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "testUser");
        loginRequest.put("password", "password123");

        when(userService.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", testUser.getPassword())).thenReturn(true);
        when(jwtService.generateToken("testUser")).thenReturn("mocked-jwt-token");

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(200, response.getStatusCodeValue());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("testUser", responseBody.get("username"));
        assertEquals("Inicio de sesión exitoso", responseBody.get("message"));
        assertEquals("mocked-jwt-token", responseBody.get("token"));
    }

    @Test
    void testLogin_Failure_UserNotFound() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "unknownUser");
        loginRequest.put("password", "password123");

        when(userService.findByUsername("unknownUser")).thenReturn(Optional.empty());

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Usuario no encontrado", response.getBody());
    }

    @Test
    void testLogin_Failure_WrongPassword() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "testUser");
        loginRequest.put("password", "wrongPassword");

        when(userService.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", testUser.getPassword())).thenReturn(false);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Contraseña incorrecta", response.getBody());
    }
}
