package com.bitwarden.passwordless.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Value
@AllArgsConstructor
@Builder
@Jacksonized
public class Alias {
    String userId;
    String alias;
    String plaintext;
    String tenant;
}
