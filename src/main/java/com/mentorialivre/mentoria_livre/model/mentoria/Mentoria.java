package com.mentorialivre.mentoria_livre.model.mentoria;

import com.mentorialivre.mentoria_livre.model.user.Mentor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mentoria_tb")
@NoArgsConstructor
@Setter @Getter
public class Mentoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusMentoria status;

    @Min(1)
    @Max(15)
    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "data_encerramento", nullable = false)
    private LocalDate data;

    @Column(name = "descricao", nullable = false)
    @Size(max = 1500)
    private String descricao;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
