package com.bitwarden.passwordless.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Jacksonized
public class RegisterToken {
    @NonNull
    final String userId;
    @NonNull
    final String username;
    String displayName;
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
