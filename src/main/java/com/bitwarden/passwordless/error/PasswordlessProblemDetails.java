package com.bitwarden.passwordless.error;

import lombok.*;

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
}
