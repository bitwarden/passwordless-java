package com.bitwarden.passwordless.error;

import com.bitwarden.passwordless.PasswordlessClient;
import lombok.Getter;
import lombok.NonNull;

/**
 * Thrown when Passwordless API returns an error during the {@link PasswordlessClient} method execution.
 * Access the {@link PasswordlessProblemDetails problem details} with {@link #getDetails()}
 *
 * @see PasswordlessProblemDetails
 * @see PasswordlessClient
 */
@Getter
public class PasswordlessApiException extends Exception {
    @NonNull
    private final PasswordlessProblemDetails details;

    public PasswordlessApiException(@NonNull PasswordlessProblemDetails details) {
        super(details.toString());
        this.details = details;
    }
}
