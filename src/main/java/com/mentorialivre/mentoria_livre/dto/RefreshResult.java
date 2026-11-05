package com.mentorialivre.mentoria_livre.dto;

import com.mentorialivre.mentoria_livre.model.refreshToken.RefreshResultStatus;

public record RefreshResult(
        RefreshResultStatus status,
        String accessToken,
        String refreshToken
) {
}
