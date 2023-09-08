package com.bitwarden.passwordless.model;

import lombok.*;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class RegisterToken {
    String userId;
    String displayName;
    String username;
    @Builder.Default
    String attestation = "none";
    @Builder.Default
    String authenticatorType = "any";
    @Builder.Default
    Boolean discoverable = true;
    @Builder.Default
    String userVerification = "preferred";
    @Builder.Default
    List<String> aliases = Collections.emptyList();
    @Builder.Default
    Boolean aliasHashing = true;
    @Builder.Default
    Instant expiresAt = Instant.now().plusSeconds(120);
}
