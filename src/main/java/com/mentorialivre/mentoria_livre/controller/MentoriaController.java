package com.mentorialivre.mentoria_livre.controller;

import com.mentorialivre.mentoria_livre.dto.GenerateMentoriaRequest;
import com.mentorialivre.mentoria_livre.dto.MentoriaResponse;
import com.mentorialivre.mentoria_livre.dto.UpdateMentoriaRequest;
import com.mentorialivre.mentoria_livre.model.user.Mentor;
import com.mentorialivre.mentoria_livre.model.user.User;
import com.mentorialivre.mentoria_livre.service.mentoria.MentoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mentoria")
public class MentoriaController {
    @Autowired
    private MentoriaService mentoriaService;

    @PatchMapping("/{id}")
    public ResponseEntity<MentoriaResponse> editar(@PathVariable Long id,
            @Valid @RequestBody UpdateMentoriaRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(mentoriaService.editarMentoria(id, request, user));
    }

    @PostMapping("/criar")
    public ResponseEntity<?> create(@Valid @RequestBody GenerateMentoriaRequest request,
                                    @AuthenticationPrincipal User user) {
        MentoriaResponse mentoriaResponse = mentoriaService.criarMentoria(request, user);
        return new ResponseEntity<>(mentoriaResponse, HttpStatus.OK);
    }

    @GetMapping("/lista/mentor")
    public ResponseEntity<?> listarMentoriaParaMentor(@AuthenticationPrincipal Mentor mentor){
        List<MentoriaResponse> mentoriaResponse = mentoriaService.listarMentoriasDoMentor(mentor.getId());
        return new ResponseEntity<>(mentoriaResponse, HttpStatus.OK);
    }

    @GetMapping("/listar/mentorado")
    public ResponseEntity<?> listarMentoriaParaMentorado(){
        List<MentoriaResponse> mentoriaResponse = mentoriaService.listarMentoriarMentorados();
        return new ResponseEntity<>(mentoriaResponse, HttpStatus.OK);
    }
}
