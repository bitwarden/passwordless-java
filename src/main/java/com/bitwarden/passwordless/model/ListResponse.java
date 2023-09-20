package com.bitwarden.passwordless.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@AllArgsConstructor
@Builder
@Jacksonized
public class ListResponse<T> {
    List<T> values;
}
