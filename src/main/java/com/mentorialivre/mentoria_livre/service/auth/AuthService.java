package com.mentorialivre.mentoria_livre.service.auth;

import com.mentorialivre.mentoria_livre.dto.AuthResponse;
import com.mentorialivre.mentoria_livre.dto.LoginRequest;
import com.mentorialivre.mentoria_livre.dto.RegisterRequest;
import com.mentorialivre.mentoria_livre.dto.UserResponse;
import com.mentorialivre.mentoria_livre.exception.EmailAlreadyExistsException;
import com.mentorialivre.mentoria_livre.model.user.Mentor;
import com.mentorialivre.mentoria_livre.model.user.Mentorado;
import com.mentorialivre.mentoria_livre.model.user.User;
import com.mentorialivre.mentoria_livre.repository.user.UserRepository;
import com.mentorialivre.mentoria_livre.service.token.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
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

    public AuthResponse login(LoginRequest request) {
        // Lança BadCredentialsException se email/senha estiverem errados
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email().trim().toLowerCase(),
                        request.senha()
                )
        );

        User user = (User) authentication.getPrincipal();
        String token = tokenService.generateToken(user);

        return new AuthResponse(token, "Bearer", tokenService.getExpirationMs(), UserResponse.from(user));
    }
}

