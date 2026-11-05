package com.mentorialivre.mentoria_livre.model.refreshToken;

import com.mentorialivre.mentoria_livre.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_token_tb")
@NoArgsConstructor
@Getter @Setter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "family_id", nullable = false)
    private UUID familyId;

    @Column(name = "token_hash",
            nullable = false,
            unique = true,
            length = 64
    )
    private String tokenHash;

    @Column(name = "create_at", nullable = false)
    private Instant createAt;

    @Column(name = "expired_at", nullable = false)
    private Instant expiredAt;

    @Column(name = "family_expired_at", nullable = false)
    private Instant familyExpiredAt;

    @Column(name = "consumed_at")
    private Instant consumedAt;

    @Column(name = "revoke_at")
    private Instant revokedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replaced_by_id")
    private RefreshToken replacedBy;
}
