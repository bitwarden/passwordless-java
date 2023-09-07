package com.bitwarden.passwordless;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class PasswordlessOptions {
    @NonNull
    @Builder.Default
    String apiUrl = "https://v4.passwordless.dev";
    @NonNull
    String apiSecretKey;
}
