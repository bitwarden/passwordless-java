package com.bitwarden.passwordless;

import com.bitwarden.passwordless.factory.DataFactory;
import com.bitwarden.passwordless.model.TestRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PasswordlessHttpClientTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig()
//                    .globalTemplating(true)
                    .dynamicPort())
            .failOnUnmatchedRequests(true)
            .build();

    PasswordlessOptions passwordlessOptions;
    PasswordlessHttpClient passwordlessHttpClient;

    @BeforeEach
    void setUp() {
        passwordlessOptions = DataFactory.passwordlessOptions(wireMock.baseUrl());
        passwordlessHttpClient = PasswordlessClientBuilder.create(passwordlessOptions)
                        .buildPasswordlessHttpClient();
    }

    @AfterEach
    void tearDown() throws IOException {
        if (passwordlessHttpClient != null) {
            passwordlessHttpClient.close();
            passwordlessHttpClient = null;
        }
    }

    @Test
    void createPostRequest_invalidApiUrl_IOException() throws IOException {
        passwordlessHttpClient.close();

        PasswordlessOptions passwordlessOptions = DataFactory.passwordlessOptions("http://localhost^:8080");
        passwordlessHttpClient = PasswordlessClientBuilder.create(passwordlessOptions)
                .buildPasswordlessHttpClient();

        TestRequest testRequest = DataFactory.testRequest();

        assertThatThrownBy(() -> passwordlessHttpClient.createPostRequest("login", testRequest))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("http://localhost^:8080");
    }

    @Test
    void createPostRequest_nullPayload_NPE() {
        assertThatThrownBy(() -> passwordlessHttpClient.createPostRequest("login", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("POST payload is null");
    }

    @Test
    void createPostRequest_payload_noError() throws IOException, ProtocolException, URISyntaxException {
        TestRequest testRequest = DataFactory.testRequest();
        String testRequestJson = passwordlessHttpClient.objectMapper.writeValueAsString(testRequest);

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest("login", testRequest);
        validateRequest(request, "POST", "/login", testRequestJson);
    }

    @Test
    void createGetRequest_invalidApiUrl_IOException() throws IOException {
        passwordlessHttpClient.close();

        PasswordlessOptions passwordlessOptions = DataFactory.passwordlessOptions("http://localhost^:8080");
        passwordlessHttpClient = PasswordlessClientBuilder.create(passwordlessOptions)
                .buildPasswordlessHttpClient();

        assertThatThrownBy(() -> passwordlessHttpClient.createGetRequest("login", Collections.singletonMap("userId", "testUser")))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("http://localhost^:8080");
    }

    @Test
    void createGetRequest_queryParametersNull_noError() throws IOException, ProtocolException, URISyntaxException {
        ClassicHttpRequest request = passwordlessHttpClient.createGetRequest("login", null);

        validateRequest(request, "GET", "/login", null);
    }

    @Test
    void createGetRequest_queryParametersEmpty_noError() throws IOException, ProtocolException, URISyntaxException {
        ClassicHttpRequest request = passwordlessHttpClient.createGetRequest("login", Collections.emptyMap());

        validateRequest(request, "GET", "/login", null);
    }

    @Test
    void createGetRequest_twoQueryParameters_noError() throws IOException, ProtocolException, URISyntaxException {
        Map<String, String> queryParameters = new TreeMap<>();
        queryParameters.put("page", "1");
        queryParameters.put("userId", "testUser");

        ClassicHttpRequest request = passwordlessHttpClient.createGetRequest("login", queryParameters);

        validateRequest(request, "GET", "/login?page=1&userId=testUser", null);
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

    private void validateRequest(ClassicHttpRequest request, String method, String path, String payloadJson)
            throws IOException, ProtocolException, URISyntaxException {
        assertThat(request).isNotNull();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getPath()).isEqualTo(path);
        assertThat(request.getUri()).isEqualTo(URI.create(passwordlessOptions.getApiUrl() + path));
        assertThat(request.getHeaders()).hasSize(1);
        assertThat(request.getHeader("ApiSecret")).isNotNull()
                .extracting(Header::getValue)
                .isEqualTo(passwordlessOptions.getApiPrivateKey());
        if (payloadJson != null) {
            assertThat(request.getEntity()).isNotNull();
            assertThat(EntityUtils.toString(request.getEntity())).isEqualTo(payloadJson);
        } else {
            assertThat(request.getEntity()).isNull();
        }
    }
}
