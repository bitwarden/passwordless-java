package com.bitwarden.passwordless.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.List;

@Value
@AllArgsConstructor
@Builder
@Jacksonized
public class UserSummary {
    String userId;
    Integer aliasCount;
    List<String> aliases;
    Integer credentialsCount;
    LocalDateTime lastUsedAt;
}
