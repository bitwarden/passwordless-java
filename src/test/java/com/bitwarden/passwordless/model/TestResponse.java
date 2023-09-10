package com.bitwarden.passwordless.model;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class TestResponse {
    @NonNull
    Integer field1;
    Instant field2;
}
