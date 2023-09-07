package com.bitwarden.passwordless.error;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PasswordlessApiException extends Exception {
    @NonNull
    private final PasswordlessProblemDetails details;
}
