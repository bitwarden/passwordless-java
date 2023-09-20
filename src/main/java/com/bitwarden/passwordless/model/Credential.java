package com.bitwarden.passwordless.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.List;

@Value
@AllArgsConstructor
@Builder
@Jacksonized
public class Credential {
    CredentialDescriptor descriptor;
    /**
     * Base64 encoded public key.
     */
    String publicKey;
    /**
     * Base64 encoded user handle.
     */
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
        /**
         * Base64 encoded credential descriptor id.
         */
        String id;
        List<String> transports;
    }
}
