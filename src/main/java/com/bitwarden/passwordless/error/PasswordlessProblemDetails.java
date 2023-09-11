package com.bitwarden.passwordless.error;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * Problem details for Passwordless Api.
 *
 * @see <a href="https://docs.passwordless.dev/guide/errors.html#problem-details">Passwordless Docs Errors</a>
 */
@Data
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class PasswordlessProblemDetails {
    String type;
    String title;
    Integer status;
    String detail;
    String instance;
    String errorCode;
    Map<String, List<String>> errors;
}
