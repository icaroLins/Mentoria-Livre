package com.mentorialivre.mentoria_livre.service.mentoria;

import com.mentorialivre.mentoria_livre.dto.GenerateMentoriaRequest;
import com.mentorialivre.mentoria_livre.dto.MentoriaResponse;
import com.mentorialivre.mentoria_livre.dto.UpdateMentoriaRequest;
import com.mentorialivre.mentoria_livre.exception.ResourceNotFoundException;
import com.mentorialivre.mentoria_livre.model.mentoria.Mentoria;
import com.mentorialivre.mentoria_livre.model.mentoria.StatusMentoria;
import com.mentorialivre.mentoria_livre.model.user.Mentor;
import com.mentorialivre.mentoria_livre.model.user.Mentorado;
import com.mentorialivre.mentoria_livre.model.user.Role;
import com.mentorialivre.mentoria_livre.model.user.User;
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
public class MentoriaService {
    @Autowired
    private MentoriaRepository mentoriaRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public MentoriaResponse editarMentoria(Long id, UpdateMentoriaRequest request, User user) {
        if (user == null || user.getRole() != Role.MENTOR) {
            throw new AccessDeniedException("Apenas mentores podem editar mentorias");
        }
        Mentoria mentoria = mentoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentoria nao encontrada"));
        if (!mentoria.getMentor().getId().equals(user.getId())) {
            throw new AccessDeniedException("Apenas o mentor responsavel pode editar esta mentoria");
        }
        if (request.titulo() != null) mentoria.setTitulo(request.titulo());
        if (request.statusMentoria() != null) mentoria.setStatus(request.statusMentoria());
        if (request.quantidade() != null) mentoria.setQuantidade(request.quantidade());
        if (request.data() != null) mentoria.setData(request.data());
        if (request.descricao() != null) mentoria.setDescricao(request.descricao());
        return MentoriaResponse.from(mentoriaRepository.save(mentoria));
    }

    @Transactional
    public MentoriaResponse criarMentoria(GenerateMentoriaRequest generateMentoriaRequest, User user){
        User usuario = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (usuario.getRole() != Role.MENTOR) {
            throw new AccessDeniedException("Apenas mentores podem criar mentorias!");
        }

        Mentor mentor = (Mentor) usuario;

        Mentoria mentoria = toEntity(generateMentoriaRequest);

        mentoria.setMentor(mentor);
        mentoria.setCreatedAt(LocalDateTime.now());

        return MentoriaResponse.from(mentoriaRepository.save(mentoria));
    }

    public List<MentoriaResponse> listarMentoriasDoMentor(UUID mentor_id){
        return mentoriaRepository.findByMentor_Id(mentor_id)
                .stream()
                .map(MentoriaResponse::from)
                .toList();
    }

    public List<MentoriaResponse> listarMentoriarMentorados(){
        return mentoriaRepository.findByStatus(StatusMentoria.ABERTA)
                .stream()
                .map(MentoriaResponse::from)
                .toList();
    }

    private Mentoria toEntity(GenerateMentoriaRequest generateMentoriaRequest){
        Mentoria mentoria = new Mentoria();
        mentoria.setData(generateMentoriaRequest.data());
        mentoria.setDescricao(generateMentoriaRequest.descricao());
        mentoria.setQuantidade(generateMentoriaRequest.quantidade());
        mentoria.setStatus(generateMentoriaRequest.statusMentoria());
        mentoria.setTitulo(generateMentoriaRequest.titulo());
        return mentoria;
    }
}
