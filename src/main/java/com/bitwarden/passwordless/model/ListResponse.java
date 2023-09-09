package com.bitwarden.passwordless.model;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class ListResponse<T> {
    List<T> values;
}
