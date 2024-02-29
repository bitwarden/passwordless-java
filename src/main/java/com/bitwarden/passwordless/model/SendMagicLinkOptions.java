package com.bitwarden.passwordless.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Jacksonized
public class SendMagicLinkOptions {
    @NonNull
    String emailAddress;

    @NonNull
    String urlTemplate;

    @NonNull
    String userId;

    @Builder.Default
    Integer timeToLive = null;
}
