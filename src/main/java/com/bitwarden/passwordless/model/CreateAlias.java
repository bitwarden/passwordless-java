package com.bitwarden.passwordless.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
public class CreateAlias {
    @NonNull
    final String userId;
    @NonNull
    final List<String> aliases;
    @Builder.Default
    Boolean hashing = true;
}
