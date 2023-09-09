package com.bitwarden.passwordless.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisteredToken {
    String token;
}
