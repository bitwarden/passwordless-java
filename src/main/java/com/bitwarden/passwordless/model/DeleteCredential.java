package com.bitwarden.passwordless.model;

import lombok.*;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class DeleteCredential {
    /**
     * Base64 encoded credential id.
     */
    @NonNull
    String credentialId;
}
