package com.bitwarden.passwordless.model;

import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
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

    @Value
    public static class PublicKeyCredentialDescriptor {
        String type;
        String id;
        List<String> transports;
    }
}
