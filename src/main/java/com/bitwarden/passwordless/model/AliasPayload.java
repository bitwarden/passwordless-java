package com.bitwarden.passwordless.model;

import lombok.*;

import java.util.List;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Builder
public class AliasPayload {
    String userId;
    List<String> aliases;
    Boolean hashing;
}
