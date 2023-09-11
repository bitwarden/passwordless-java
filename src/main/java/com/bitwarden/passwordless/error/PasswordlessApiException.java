package com.bitwarden.passwordless.error;

import com.bitwarden.passwordless.PasswordlessClient;

import java.util.Objects;

/**
 * Thrown when Passwordless API returns an error during the {@link PasswordlessClient} method execution.
 * Access the {@link PasswordlessProblemDetails problem details} with {@link #getProblemDetails()}
 *
 * @see PasswordlessProblemDetails
 * @see PasswordlessClient
 */
public class PasswordlessApiException extends Exception {

    private final PasswordlessProblemDetails problemDetails;

    public PasswordlessApiException(PasswordlessProblemDetails problemDetails) {
        super(Objects.requireNonNull(problemDetails, "problemDetails cannot be null").toString());
        this.problemDetails = problemDetails;
    }

    public PasswordlessProblemDetails getProblemDetails() {
        return problemDetails;
    }
}
