package com.bitwarden.passwordless.model;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class TestRequest {
    @NonNull
    Integer fieldNumber;
    @Builder.Default
    Integer fieldNumberDefault = 123;

    String fieldString;
    @Builder.Default
    String fieldStringDefault = "testString";

    List<String> fieldList;
    @Builder.Default
    List<String> fieldListDefault = Arrays.asList("testString1", "testString2");

    Boolean fieldBool;
    @Builder.Default
    Boolean fieldBoolDefault = true;

    Instant fieldInstant;
    @Builder.Default
    Instant fieldInstantDefault = Instant.parse("2023-09-10T10:23:45.678910Z");

    LocalDateTime fieldLocalDateTime;
    @Builder.Default
    LocalDateTime fieldLocalDateTimeDefault = LocalDateTime.parse("2022-11-23T20:08:35");

    TestRequestInner testRequestInner;

    @Data
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @Builder
    public static class TestRequestInner {
        String fieldStringInner;
    }
}
