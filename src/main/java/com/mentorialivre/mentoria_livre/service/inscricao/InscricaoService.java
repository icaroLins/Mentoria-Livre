package com.mentorialivre.mentoria_livre.service.inscricao;

import com.mentorialivre.mentoria_livre.dto.InscricaoResponse;
import com.mentorialivre.mentoria_livre.model.inscricao.Inscricao;
import com.mentorialivre.mentoria_livre.model.inscricao.StatusInscricao;
import com.mentorialivre.mentoria_livre.model.mentoria.Mentoria;
import com.mentorialivre.mentoria_livre.model.user.Mentorado;
import com.mentorialivre.mentoria_livre.model.user.Role;
import com.mentorialivre.mentoria_livre.repository.inscricao.InscricaoRepository;
import com.mentorialivre.mentoria_livre.repository.mentoria.MentoriaRepository;
import com.mentorialivre.mentoria_livre.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class InscricaoService {
    @Autowired
    private InscricaoRepository inscricaoRepository;
    @Autowired
    private MentoriaRepository mentoriaRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public InscricaoResponse realizarInscricao(UUID mentoradoId, Long mentoriaId){
       Mentorado mentorado = (Mentorado) userRepository.findById(mentoradoId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

       Mentoria mentoria = mentoriaRepository.findById(mentoriaId)
                .orElseThrow(() -> new RuntimeException("Mentoria Not Found"));

        if(inscricaoRepository.existsByMentoradoAndMentoria(mentorado, mentoria)){
            throw  new RuntimeException("Inscrição já realizada");
        }

        if(mentorado.getRole() != Role.MENTORADO){
            throw new AccessDeniedException("Apenas mentorados podem se inscrever nas mentorias!");
        }

        long totalInscritos = inscricaoRepository
                .countByMentoria_IdAndStatusInscricao(
                        mentoria.getId(),
                        StatusInscricao.ATIVA
                );

        if (totalInscritos >= mentoria.getQuantidade()) {
            throw new RuntimeException("Mentoria sem vagas disponíveis!");
        }

        Inscricao inscricao = new Inscricao();

        inscricao.setStatusInscricao(StatusInscricao.ATIVA);
        inscricao.setMentorado(mentorado);
        inscricao.setMentoria(mentoria);
        inscricao.setCreatedAt(LocalDateTime.now());

        return InscricaoResponse.from(inscricaoRepository.save(inscricao));
    }

    public List<InscricaoResponse> listInscricaoMentorado(UUID mentoradoId){
        return inscricaoRepository.findByMentorado_Id(mentoradoId)
                .stream()
                .map(InscricaoResponse::from)
                .toList();
    }

    public List<InscricaoResponse> listInscricaoMentor(Long mentoriaId){
        return inscricaoRepository.findByMentoria_Id(mentoriaId)
                .stream()
                .map(InscricaoResponse::from)
                .toList();
    }
}
