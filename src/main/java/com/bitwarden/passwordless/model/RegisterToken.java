package com.bitwarden.passwordless.model;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Builder
public class RegisterToken {
    String userId;
    String displayName;
    String username;
    String attestation;
    String authenticatorType;
    Boolean discoverable;
    String userVerification;
    List<String> aliases;
    Boolean aliasHashing;
    Instant expiresAt;
    String tokenId;
    String type;
}
