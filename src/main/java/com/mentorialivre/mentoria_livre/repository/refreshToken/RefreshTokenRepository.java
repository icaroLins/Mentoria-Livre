package com.mentorialivre.mentoria_livre.repository.refreshToken;

import com.mentorialivre.mentoria_livre.model.refreshToken.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    //buscar refresh token pelo hash.
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    //impedir que duas transações consumam o mesmo token.
    @Modifying
    @Query("""
    UPDATE RefreshToken rt
       SET rt.consumedAt = :now
     WHERE rt.id = :id
       AND rt.consumedAt IS NULL
       AND rt.revokedAt IS NULL
       AND rt.expiredAt > :now
       AND rt.familyExpiredAt > :now
    """)
    int consumeToken(
            @Param("id") UUID id,
            @Param("now") Instant now
    );

    //permite reconstruir o histórico da rotação.
    @Modifying
    @Query("""
    UPDATE RefreshToken rt
       SET rt.replacedBy = :replacement
     WHERE rt.id = :oldId
    """)
    int setReplacement(
            @Param("oldId") UUID oldId,
            @Param("replacement") RefreshToken replacement
    );

    //invalida todos os tokens ainda não revogados daquela sessão e logout de uma sessão.
    @Modifying
    @Query("""
    UPDATE RefreshToken rt
       SET rt.revokedAt = :now
     WHERE rt.familyId = :familyId
       AND rt.revokedAt IS NULL
    """)
    int revokeFamily(
            @Param("familyId") UUID familyId,
            @Param("now") Instant now
    );

    //Para logout global.
    @Modifying
    @Query("""
    UPDATE RefreshToken rt
       SET rt.revokedAt = :now
     WHERE rt.user.id = :userId
       AND rt.revokedAt IS NULL
    """)
    int revokeAllByUser(
            @Param("userId") UUID userId,
            @Param("now") Instant now
    );
}
