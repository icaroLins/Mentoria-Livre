package com.mentorialivre.mentoria_livre.model.inscricao;

import com.mentorialivre.mentoria_livre.model.mentoria.Mentoria;
import com.mentorialivre.mentoria_livre.model.user.Mentorado;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "inscricao_tb")
@NoArgsConstructor
@Setter @Getter
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentorado_id")
    private Mentorado mentorado;

    @ManyToOne
    @JoinColumn(name = "mentoria_id")
    private Mentoria mentoria;

    @Column(name = "create_at")
    private LocalDateTime createdAt;
}
