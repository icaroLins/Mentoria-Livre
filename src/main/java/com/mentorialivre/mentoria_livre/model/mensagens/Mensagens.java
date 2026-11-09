package com.mentorialivre.mentoria_livre.model.mensagens;

import com.mentorialivre.mentoria_livre.model.mentoria.Mentoria;
import com.mentorialivre.mentoria_livre.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "menssagens_tb")
@NoArgsConstructor
@Setter @Getter
public class Mensagens {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "conteudo", columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "data_envio", nullable = false)
    private Instant dataEnvio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentoria_id", nullable = false)
    private Mentoria mentoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
