package com.bitwarden.passwordless;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represents all the options you can use to configure a backend Passwordless system.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordlessOptions {
    /**
     * Url to use for Passwordless API operations.
     */
    @Builder.Default
    String apiUrl = "https://v4.passwordless.dev";

    /**
     * Secret API key used to authenticate with the Passwordless API.
     */
    @NonNull
    final String apiPrivateKey;
}
