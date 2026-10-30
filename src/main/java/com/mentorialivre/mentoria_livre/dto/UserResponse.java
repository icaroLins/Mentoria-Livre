package com.mentorialivre.mentoria_livre.dto;

import com.mentorialivre.mentoria_livre.model.user.Mentor;
import com.mentorialivre.mentoria_livre.model.user.Mentorado;
import com.mentorialivre.mentoria_livre.model.user.Role;
import com.mentorialivre.mentoria_livre.model.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String nome,
        String email,
        LocalDate dataNascimento,
        Role role,
        String especialidade,
        String objetivo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserResponse from(User user) {
        String especialidade = user instanceof Mentor m ? m.getEspecialidade() : null;
        String objetivo = user instanceof Mentorado md ? md.getObjetivo() : null;

        return new UserResponse(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getDataNascimento(),
                user.getRole(),
                especialidade,
                objetivo,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
