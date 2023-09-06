package com.bitwarden.passwordless.model;

import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
public class UserSummary {
    String userId;
    Integer aliasCount;
    List<String> aliases;
    Integer credentialsCount;
    Instant lastUsedAt;
}
