package com.bitwarden.passwordless.model;

import lombok.*;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Builder
public class AliasPointer {
    String userId;
    String alias;
    String plaintext;
    String tenant;
}
