package com.bitwarden.passwordless;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;

class PasswordlessHttpClientTest {

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
    void createPostRequest_invalidApiUrl_IOException() {
    }

    @Test
    void createPostRequest_nullPayload_NPE() {
    }

    @Test
    void createPostRequest_payload_noError() {
    }

    @Test
    void createGetRequest_invalidApiUrl_IOException() {
    }

    @Test
    void createGetRequest_queryParametersNull_noError() {
    }

    @Test
    void createGetRequest_queryParametersEmpty_noError() {
    }

    @Test
    void createGetRequest_twoQueryParameters_noError() {
    }

    @Test
    void sendRequest_unreachableApiUrl_IOException() {
    }

    @Test
    void sendRequest_notFoundPath_PasswordlessApiException() {
    }

    @Test
    void sendRequest_notFoundPathWithResponse_PasswordlessApiException() {
    }

    @Test
    void sendRequest_errorResponseProblemJson_PasswordlessApiException() {
    }

    @Test
    void sendRequest_okResponseNotJson_IOException() {
    }

    @Test
    void sendRequest_okResponseNoPayload_noError() {
    }

    @Test
    void sendRequest_okResponseJson_noError() {
    }

    @Test
    void close_userProvidedHttpClient_httpClientNotClosed() {
    }

    @Test
    void close_defaultHttpClient_httpClientClosed() {
    }
}
