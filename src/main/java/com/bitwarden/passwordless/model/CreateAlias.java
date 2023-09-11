package com.bitwarden.passwordless.model;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class CreateAlias {
    @NonNull
    String userId;
    List<String> aliases;
    @Builder.Default
    Boolean hashing = true;
}
