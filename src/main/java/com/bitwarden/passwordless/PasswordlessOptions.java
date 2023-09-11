package com.bitwarden.passwordless;

import lombok.*;

/**
 * Represents all the options you can use to configure a backend Passwordless system.
 */
@Value
@Builder
public class PasswordlessOptions {
    /**
     * Url to use for Passwordless API operations.
     */
    @NonNull
    @Builder.Default
    String apiUrl = "https://v4.passwordless.dev";

    /**
     * Secret API key used to authenticate with the Passwordless API.
     */
    @NonNull
    String apiPrivateKey;
}
