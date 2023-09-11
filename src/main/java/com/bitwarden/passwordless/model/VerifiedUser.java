package com.bitwarden.passwordless.model;

import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class VerifiedUser {
    String userId;
    Instant timestamp;
    String rpId;
    String origin;
    String device;
    String country;
    String nickname;
    String credentialId;
    Boolean success;
    Instant expiresAt;
    String tokenId;
    String type;
}