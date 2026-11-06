package com.mentorialivre.mentoria_livre.controller;

import com.mentorialivre.mentoria_livre.dto.InscricaoResponse;
import com.mentorialivre.mentoria_livre.model.user.Mentorado;
import com.mentorialivre.mentoria_livre.repository.inscricao.InscricaoRepository;
import com.mentorialivre.mentoria_livre.service.inscricao.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inscricao")
public class InscricaoController {

    @Autowired
    private InscricaoService inscricaoService;

    @PostMapping("/realizar/{mentoria_id}")
    public ResponseEntity<?> realizarInscricao(@AuthenticationPrincipal Mentorado mentorado,
                                               @PathVariable Long mentoria_id){
        InscricaoResponse inscricaoResponse = inscricaoService.realizarInscricao(mentorado.getId(),mentoria_id);
        return new ResponseEntity<>(inscricaoResponse, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<?> listIscricaoMentorado(@AuthenticationPrincipal Mentorado mentorado){
        List<InscricaoResponse> inscricaoResponses = inscricaoService.listInscricaoMentorado(mentorado.getId());
        return new ResponseEntity<>(inscricaoResponses, HttpStatus.OK);
    }

    @GetMapping("/mentoria/{mentoria_id}")
    public ResponseEntity<?> listInscricaoPorMentoria(@AuthenticationPrincipal Mentorado mentorado,
                                                       @PathVariable Long mentoria_id){
        List<InscricaoResponse> inscricaoResponses = inscricaoService.listInscricaoMentor(mentoria_id);
        return new ResponseEntity<>(inscricaoResponses, HttpStatus.OK);
    }
}
