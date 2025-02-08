package com.example.taskmanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.taskmanager.models.User;
import com.example.taskmanager.services.JwtService;
import com.example.taskmanager.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000") 
@Tag(name = "Auth", description = "Endpoints de autenticación")

public class AuthController {

    @Autowired
    private JwtService jwtService;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Registrar un nuevo usuario", description = "Registra un usuario si el nombre de usuario no existe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "El usuario ya existe")
    })

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        Optional<User> existingUser = userService.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El usuario ya existe");
        }
        
        userService.saveUser(user);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    @Operation(summary = "Iniciar sesión", description = "Valida el usuario y devuelve un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Usuario no encontrado o contraseña incorrecta")
    })

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        Optional<User> optionalUser = userService.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
        }

        String token = jwtService.generateToken(user.getUsername());


        // Respuesta sin contraseña por seguridad
        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("message", "Inicio de sesión exitoso");
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}