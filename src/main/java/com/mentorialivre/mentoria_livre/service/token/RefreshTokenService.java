package com.mentorialivre.mentoria_livre.service.token;

import com.mentorialivre.mentoria_livre.dto.GeneratedRefreshToken;
import com.mentorialivre.mentoria_livre.model.refreshToken.RefreshToken;
import com.mentorialivre.mentoria_livre.model.user.User;
import com.mentorialivre.mentoria_livre.repository.refreshToken.RefreshTokenRepository;
import com.mentorialivre.mentoria_livre.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserRepository userRepository;

    private final SecureRandom random = new SecureRandom();

    //Geração do token opaco
    private String generateRawToken(){
        byte[] randomBytes = new byte[32];

        random.nextBytes(randomBytes);

        return  Base64.getEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    //Calculando o SHA-256
    private String hashToken(String rawToken){
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            byte[] hash = messageDigest.digest(rawToken.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);

        }catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "Erro ao calcular hash do Refresh Token",
                    e
            );
        }
    }

    private Instant min(Instant a, Instant b) {
        return a.isBefore(b) ? a : b;
    }

    //Criar RefrehToken e persistir no banco
    @Transactional
    public GeneratedRefreshToken create(User user){
        Instant now = Instant.now();

        UUID familyId = UUID.randomUUID();

        Instant familyExpired = now.plus(30, ChronoUnit.DAYS);

        Instant expiredAt = min(now.plus(7, ChronoUnit.DAYS),
                familyExpired);

        String rawToken = generateRawToken();
        String hashToken = hashToken(rawToken);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setFamilyId(familyId);
        refreshToken.setTokenHash(hashToken);
        refreshToken.setCreateAt(now);
        refreshToken.setExpiredAt(expiredAt);
        refreshToken.setFamilyExpiredAt(familyExpired);

        refreshTokenRepository.save(refreshToken);

        return  new GeneratedRefreshToken(rawToken, refreshToken);
    }

    @Transactional
    public GeneratedRefreshToken rotate(String rawToken) {

        Instant now = Instant.now();

        // 1. Calcular o hash do token recebido.
        String tokenHash = hashToken(rawToken);

        // 2. Buscar o token original.
        RefreshToken oldToken = refreshTokenRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(() ->
                        new RuntimeException("Invalid Refresh Token")
                );

        // 3. Verificar reutilização.
        if (oldToken.getConsumedAt() != null) {

            refreshTokenRepository.revokeFamily(
                    oldToken.getFamilyId(),
                    now
            );

            return null; // Substituir por um resultado REUSE_DETECTED.
        }

        // 4. Verificar expiração e revogação.
        if (oldToken.getExpiredAt() == null
                || !oldToken.getExpiredAt().isAfter(now)
                || !oldToken.getFamilyExpiredAt().isAfter(now)
                || oldToken.getRevokedAt() != null) {

            return null; // Resultado INVALID.
        }

        // 5. Consumir atomicamente o token antigo.
        int consumed = refreshTokenRepository.consumeToken(
                oldToken.getId(),
                now
        );

        if (consumed == 0) {

            // Outro processo pode ter consumido o token.
            RefreshToken updatedToken = refreshTokenRepository
                    .findById(oldToken.getId())
                    .orElseThrow();

            if (updatedToken.getConsumedAt() != null) {

                refreshTokenRepository.revokeFamily(
                        updatedToken.getFamilyId(),
                        now
                );

                return null; // REUSE_DETECTED.
            }

            return null; // INVALID.
        }

        // 6. Gerar novo Refresh Token.
        String newRawToken = generateRawToken();

        Instant familyExpiredAt = oldToken.getFamilyExpiredAt();

        Instant expiredAt = min(
                now.plus(7, ChronoUnit.DAYS),
                familyExpiredAt
        );

        // 7. Criar registro na mesma família.
        RefreshToken newToken = new RefreshToken();

        newToken.setUser(oldToken.getUser());
        newToken.setFamilyId(oldToken.getFamilyId());
        newToken.setTokenHash(hashToken(newRawToken));
        newToken.setCreateAt(now);
        newToken.setExpiredAt(expiredAt);
        newToken.setFamilyExpiredAt(familyExpiredAt);

        refreshTokenRepository.save(newToken);

        // 8. Associar o token antigo ao novo.
        oldToken.setReplacedBy(newToken);

        refreshTokenRepository.save(oldToken);

        return new GeneratedRefreshToken(newRawToken, newToken);
    }


}
