package com.mentorialivre.mentoria_livre.repository.mentoria;

import com.mentorialivre.mentoria_livre.model.mentoria.Mentoria;
import com.mentorialivre.mentoria_livre.model.mentoria.StatusMentoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MentoriaRepository extends JpaRepository<Mentoria, Long> {
    List<Mentoria> findByMentor_Id(UUID mentorId);
    List<Mentoria> findByStatus(StatusMentoria statusMentoria);
}
