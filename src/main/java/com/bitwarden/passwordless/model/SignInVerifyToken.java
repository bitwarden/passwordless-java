package com.bitwarden.passwordless.model;

import lombok.Value;

import java.time.Instant;

@Value
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
