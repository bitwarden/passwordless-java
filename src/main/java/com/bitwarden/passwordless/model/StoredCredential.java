package com.bitwarden.passwordless.model;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class StoredCredential {
    PublicKeyCredentialDescriptor descriptor;
    String publicKey;
    String userHandle;
    Integer signatureCounter;
    String attestationFmt;
    Instant createdAt;
    String aaGuid;
    Instant lastUsedAt;
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
    public static class PublicKeyCredentialDescriptor {
        String type;
        String id;
        List<String> transports;
    }
}
