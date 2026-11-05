package com.mentorialivre.mentoria_livre.service.auth;

import com.mentorialivre.mentoria_livre.dto.*;
import com.mentorialivre.mentoria_livre.exception.EmailAlreadyExistsException;
import com.mentorialivre.mentoria_livre.model.user.Mentor;
import com.mentorialivre.mentoria_livre.model.user.Mentorado;
import com.mentorialivre.mentoria_livre.model.user.User;
import com.mentorialivre.mentoria_livre.repository.user.UserRepository;
import com.mentorialivre.mentoria_livre.service.token.RefreshTokenService;
import com.mentorialivre.mentoria_livre.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final TokenService tokenService;
    @Autowired
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       TokenService tokenService,
                       RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        User user = switch (request.role()) {
            case MENTOR -> {
                Mentor mentor = new Mentor();
                mentor.setEspecialidade(request.especialidade());
                yield mentor;
            }
            case MENTORADO -> {
                Mentorado mentorado = new Mentorado();
                mentorado.setObjetivo(request.objetivo());
                yield mentorado;
            }
        };

        user.setNome(request.nome().trim());
        user.setEmail(email);
        user.setSenha(passwordEncoder.encode(request.senha())); // BCrypt
        user.setDataNascimento(request.dataNascimento());

        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email().trim().toLowerCase(),
                                request.senha()
                        )
                );

        User user = (User) authentication.getPrincipal();

        // Gerar Access Token.
        assert user != null;
        String accessToken = tokenService.generateToken(user);

        // Gerar Refresh Token.
        GeneratedRefreshToken generated =
                refreshTokenService.create(user);

        return new AuthResponse(
                accessToken,
                generated.rawToken(),
                "Bearer",
                tokenService.getExpirationMs(),
                UserResponse.from(user)
        );
    }

    @Transactional
    public AuthResponse refresh(String rawRefreshToken) {

        // 1. Validar e rotacionar o Refresh Token.
        GeneratedRefreshToken generated =
                refreshTokenService.rotate(rawRefreshToken);

        if (generated == null) {
            throw new RuntimeException("Invalid Refresh Token");
        }

        // 2. Recuperar o usuário associado.
        User user = generated.entity().getUser();

        // 3. Gerar novo Access Token.
        String accessToken = tokenService.generateToken(user);

        // 4. Retornar os dois novos tokens.
        return new AuthResponse(
                accessToken,
                generated.rawToken(),
                "Bearer",
                tokenService.getExpirationMs(),
                UserResponse.from(user)
        );
    }
}

