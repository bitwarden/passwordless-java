package com.bitwarden.passwordless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@AllArgsConstructor
@Builder
@Jacksonized
public class GeneratedAuthenticationToken {
    @NonNull
    String token;
}
