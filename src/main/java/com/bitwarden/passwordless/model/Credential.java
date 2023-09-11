package com.bitwarden.passwordless.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class Credential {
    CredentialDescriptor descriptor;
    String publicKey;
    String userHandle;
    Integer signatureCounter;
    String attestationFmt;
    LocalDateTime createdAt;
    String aaGuid;
    LocalDateTime lastUsedAt;
    String rpId;
    String origin;
    String country;
    String device;
    String nickname;
    String userId;

    @Data
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @Builder
    public static class CredentialDescriptor {
        String type;
        String id;
        List<String> transports;
    }
}
