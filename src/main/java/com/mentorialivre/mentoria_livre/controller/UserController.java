package com.mentorialivre.mentoria_livre.controller;

import com.mentorialivre.mentoria_livre.dto.UserResponse;
import com.mentorialivre.mentoria_livre.model.user.User;
import com.mentorialivre.mentoria_livre.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "Perfil do usuário")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Retorna o perfil do usuário autenticado")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.findByEmail(user.getEmail()));
    }

    @Operation(summary = "Busca o perfil de um usuário pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }
}
