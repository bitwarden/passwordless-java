package com.bitwarden.passwordless.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

@Value
@AllArgsConstructor
@Builder
@Jacksonized
public class VerifiedUser {
    Boolean success;
    String userId;
    Instant timestamp;
    String rpId;
    String origin;
    String device;
    String country;
    String nickname;
    /**
     * Base64 encoded credential id.
     */
    String credentialId;
    Instant expiresAt;
    String tokenId;
    String type;
}
