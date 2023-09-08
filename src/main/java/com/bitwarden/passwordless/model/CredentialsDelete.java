package com.bitwarden.passwordless.model;

import lombok.*;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Builder
public class CredentialsDelete {
    String credentialId;
}
