package com.bitwarden.passwordless.model;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Builder
public class UserSummary {
    String userId;
    Integer aliasCount;
    List<String> aliases;
    Integer credentialsCount;
    Instant lastUsedAt;
}
