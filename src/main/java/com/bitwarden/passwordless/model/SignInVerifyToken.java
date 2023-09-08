package com.bitwarden.passwordless.model;

import lombok.*;

import java.time.Instant;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Builder
public class SignInVerifyToken {
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
