package com.bitwarden.passwordless.model;

import lombok.Value;

@Value
public class AliasPointer {
    String userId;
    String alias;
    String plaintext;
    String tenant;
}
