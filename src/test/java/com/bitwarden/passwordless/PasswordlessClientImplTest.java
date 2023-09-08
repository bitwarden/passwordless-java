package com.bitwarden.passwordless;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;

class PasswordlessClientImplTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig()
//                    .globalTemplating(true)
                    .dynamicPort())
            .failOnUnmatchedRequests(true)
            .build();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createAlias_requestNull_NPE() {
    }

    @Test
    void createAlias_errorResponse_PasswordlessApiException() {
    }

    @Test
    void createAlias_validRequest_noError() {
    }

    @Test
    void getAliases_requestNull_NPE() {
    }

    @Test
    void getAliases_errorResponse_PasswordlessApiException() {
    }

    @Test
    void getAliases_validUserId_validResponse() {
    }

    @Test
    void setAppsFeature_requestNull_NPE() {
    }

    @Test
    void setAppsFeature_errorResponse_PasswordlessApiException() {
    }

    @Test
    void setAppsFeature_validRequest_noError() {
    }

    @Test
    void deleteCredentials_requestNull_NPE() {
    }

    @Test
    void deleteCredentials_errorResponse_PasswordlessApiException() {
    }

    @Test
    void deleteCredentials_validRequest_noError() {
    }

    @Test
    void getCredentials_requestNull_NPE() {
    }

    @Test
    void getCredentials_errorResponse_PasswordlessApiException() {
    }

    @Test
    void getCredentials_validUserId_validResponse() {
    }

    @Test
    void createRegisterToken_requestNull_NPE() {
    }

    @Test
    void createRegisterToken_errorResponse_PasswordlessApiException() {
    }

    @Test
    void createRegisterToken_validRequest_validResponse() {
    }

    @Test
    void signInVerify_requestNull_NPE() {
    }

    @Test
    void signInVerify_errorResponse_PasswordlessApiException() {
    }

    @Test
    void signInVerify_validRequest_validResponse() {
    }

    @Test
    void getUsers_requestNull_NPE() {
    }

    @Test
    void getUsers_errorResponse_PasswordlessApiException() {
    }

    @Test
    void getUsers_valid_validResponse() {
    }

    @Test
    void deleteUser_requestNull_NPE() {
    }

    @Test
    void deleteUser_errorResponse_PasswordlessApiException() {
    }

    @Test
    void deleteUser_validRequest_validResponse() {
    }

    @Test
    void close_closing_httpClientClosed() {

    }
}
