package com.bitwarden.passwordless.model;

import lombok.Value;

import java.util.List;

@Value
public class AliasPayload {
    String userId;
    List<String> aliases;
    Boolean hashing;
}
