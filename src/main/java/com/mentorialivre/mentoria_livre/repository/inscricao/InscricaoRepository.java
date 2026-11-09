package com.mentorialivre.mentoria_livre.repository.inscricao;

import com.mentorialivre.mentoria_livre.model.inscricao.Inscricao;
import com.mentorialivre.mentoria_livre.model.inscricao.StatusInscricao;
import com.mentorialivre.mentoria_livre.model.mentoria.Mentoria;
import com.mentorialivre.mentoria_livre.model.user.Mentorado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {
    boolean existsByMentoradoAndMentoria(Mentorado mentorado, Mentoria mentoria);
    List<Inscricao> findByMentorado_Id(UUID mentoradoId);
    List<Inscricao> findByMentoria_Id(Long mentoriaId);
    void deleteByMentorado_Id(UUID mentorado_id);
    long countByMentoria_IdAndStatusInscricao(
            Long mentoriaId,
            StatusInscricao statusInscricao
    );

    boolean existsByMentorado_IdAndMentoria_IdAndStatusInscricao(
            UUID mentoradoId,
            Long mentoriaId,
            StatusInscricao statusInscricao
    );
}