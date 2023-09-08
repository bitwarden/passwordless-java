package com.bitwarden.passwordless.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterTokenResponse {
    String token;
}
