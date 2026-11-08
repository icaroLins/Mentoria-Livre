package com.mentorialivre.mentoria_livre.service.passwordResetToken;

import com.mentorialivre.mentoria_livre.model.passwordResetToken.PasswordResetToken;
import com.mentorialivre.mentoria_livre.model.user.User;
import com.mentorialivre.mentoria_livre.repository.passwordResetToken.PasswordResetTokenRepository;
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

@Service

public class PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final SecureRandom random = new SecureRandom();
    private static final long TOKEN_EXPIRATION_MINUTES = 15;

    // Construtor que recebe o repositório por injeção de dependência.
    public PasswordResetTokenService(
        PasswordResetTokenRepository passwordResetTokenRepository) {
            this.passwordResetTokenRepository = passwordResetTokenRepository;
}
   private String generateRawToken() {
    byte[] randomBytes = new byte[32];
    random.nextBytes(randomBytes);

    return Base64.getEncoder()
            .withoutPadding()
            .encodeToString(randomBytes);
}

  // Gera um hash SHA-256 do token original para permitir sua verificação sem armazená-lo em texto puro no banco de dados.
  private String hashToken(String rawToken) {

    try {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(
                rawToken.getBytes(StandardCharsets.UTF_8)
        );

        return HexFormat.of().formatHex(hash);

    } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException(
                "Erro ao calcular hash do token de recuperação",
                e
        );
    }
}
// Cria o token de recuperação e salva seus dados no banco.
@Transactional
public String createToken(User user) {
    Instant now = Instant.now();
    String rawToken = generateRawToken();
    String tokenHash = hashToken(rawToken);

    PasswordResetToken passwordResetToken = new PasswordResetToken();

    passwordResetToken.setUser(user);
    passwordResetToken.setTokenHash(tokenHash);
    passwordResetToken.setCreatedAt(now);
    passwordResetToken.setExpiresAt(
        now.plus(TOKEN_EXPIRATION_MINUTES, ChronoUnit.MINUTES)
);
    passwordResetTokenRepository.save(passwordResetToken);
    return rawToken;
}

// Valida se o token existe, não foi utilizado e ainda está dentro do prazo de validade.
public PasswordResetToken validateToken(String rawToken) {
    String tokenHash = hashToken(rawToken);
    PasswordResetToken token = passwordResetTokenRepository
            .findByTokenHash(tokenHash)
            .orElseThrow(() ->
                    new IllegalArgumentException("Token inválido.")
            );

    if (token.getUsedAt() != null) {
        throw new IllegalArgumentException("Token já utilizado.");
    }

    if (!token.getExpiresAt().isAfter(Instant.now())) {
        throw new IllegalArgumentException("Token expirado.");
    }
    return token;
}

}
