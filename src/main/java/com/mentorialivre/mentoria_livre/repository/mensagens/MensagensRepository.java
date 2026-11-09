package com.mentorialivre.mentoria_livre.repository.mensagens;

import com.mentorialivre.mentoria_livre.model.mensagens.Mensagens;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Repository
public interface MensagensRepository extends JpaRepository<Mensagens, UUID> {
    Page<Mensagens> findByMentoria_IdOrderByDataEnvioDesc(
            Long mentoriaId,
            Pageable pageable
    );
}
