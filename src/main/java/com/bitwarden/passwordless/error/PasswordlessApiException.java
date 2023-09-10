package com.bitwarden.passwordless.error;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class PasswordlessApiException extends Exception {
    @NonNull
    private final PasswordlessProblemDetails details;

    public PasswordlessApiException(@NonNull PasswordlessProblemDetails details) {
        super(details.toString());
        this.details = details;
    }
}
