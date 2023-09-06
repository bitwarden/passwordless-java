package com.bitwarden.passwordless.error;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PasswordlessApiException extends Exception {
    private final PasswordlessProblemDetails details;
}
