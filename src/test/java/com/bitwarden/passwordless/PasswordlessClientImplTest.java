package com.bitwarden.passwordless;

import com.bitwarden.passwordless.error.PasswordlessApiException;
import com.bitwarden.passwordless.error.PasswordlessProblemDetails;
import com.bitwarden.passwordless.factory.DataFactory;
import com.bitwarden.passwordless.model.*;
import com.bitwarden.passwordless.utils.WireMockUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class PasswordlessClientImplTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .failOnUnmatchedRequests(true)
            .build();

    PasswordlessOptions passwordlessOptions;

    ObjectMapper objectMapper;

    PasswordlessClient passwordlessClient;

    WireMockUtils wireMockUtils;

    @BeforeEach
    void setUp() {
        passwordlessOptions = DataFactory.passwordlessOptions(wireMock.baseUrl());
        PasswordlessHttpClient passwordlessHttpClient = PasswordlessClientBuilder.create(passwordlessOptions)
                .buildPasswordlessHttpClient();

        objectMapper = passwordlessHttpClient.objectMapper;

        passwordlessClient = new PasswordlessClientImpl(passwordlessHttpClient);

        wireMockUtils = WireMockUtils.builder()
                .wireMockExtension(wireMock)
                .objectMapper(objectMapper)
                .passwordlessOptions(passwordlessOptions)
                .build();
    }

    @AfterEach
    void tearDown() throws IOException {
        if (passwordlessClient != null) {
            passwordlessClient.close();
            passwordlessClient = null;
        }
    }

    @Test
    void createAlias_requestNull_NPE() {
        assertThatThrownBy(() -> passwordlessClient.createAlias(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("createAlias");
    }

    @Test
    void createAlias_errorResponse_PasswordlessApiException() throws JsonProcessingException {
        PasswordlessProblemDetails problemDetails = DataFactory.passwordlessProblemDetailsInvalidToken();

        wireMock.stubFor(post(urlEqualTo("/alias"))
                .willReturn(wireMockUtils.createProblemDetailsResponse(problemDetails)));

        CreateAlias createAlias = DataFactory.createAlias();

        PasswordlessApiException passwordlessApiException = catchThrowableOfType(
                () -> passwordlessClient.createAlias(createAlias), PasswordlessApiException.class);

        assertThat(passwordlessApiException).isNotNull();
        assertThat(passwordlessApiException.getDetails()).isEqualTo(problemDetails);

        wireMockUtils.verifyPost("/alias", createAlias);
    }

    @Test
    void createAlias_validRequest_noError() throws PasswordlessApiException, IOException {
        wireMock.stubFor(post(urlEqualTo("/alias"))
                .willReturn(WireMock.ok()));

        CreateAlias createAlias = DataFactory.createAlias();

        passwordlessClient.createAlias(createAlias);

        wireMockUtils.verifyPost("/alias", createAlias);
    }

    @Test
    void getAliases_requestNull_NPE() {
        assertThatThrownBy(() -> passwordlessClient.getAliases(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("userId");
    }

    @Test
    void getAliases_errorResponse_PasswordlessApiException() throws JsonProcessingException {
        PasswordlessProblemDetails problemDetails = DataFactory.passwordlessProblemDetailsInvalidToken();

        wireMock.stubFor(get(urlPathEqualTo("/alias/list"))
                .withQueryParam("userId", equalTo(DataFactory.USER_ID))
                .willReturn(wireMockUtils.createProblemDetailsResponse(problemDetails)));

        PasswordlessApiException passwordlessApiException = catchThrowableOfType(
                () -> passwordlessClient.getAliases(DataFactory.USER_ID), PasswordlessApiException.class);

        assertThat(passwordlessApiException).isNotNull();
        assertThat(passwordlessApiException.getDetails()).isEqualTo(problemDetails);

        wireMockUtils.verifyGet("/alias/list", Collections.singletonMap("userId", DataFactory.USER_ID));
    }

    @Test
    void getAliases_validUserId_validResponse() throws IOException, PasswordlessApiException {
        Alias alias1 = DataFactory.alias1();
        Alias alias2 = DataFactory.alias2();
        ListResponse<Alias> aliasListResponse = new ListResponse<>(Arrays.asList(alias1, alias2));

        wireMock.stubFor(get(urlPathEqualTo("/alias/list"))
                .withQueryParam("userId", equalTo(DataFactory.USER_ID))
                .willReturn(WireMock.ok()
                        .withResponseBody(wireMockUtils.createJsonBody(aliasListResponse))));

        List<Alias> aliases = passwordlessClient.getAliases(DataFactory.USER_ID);

        assertThat(aliases).containsExactlyInAnyOrder(alias1, alias2);

        wireMockUtils.verifyGet("/alias/list", Collections.singletonMap("userId", DataFactory.USER_ID));
    }

    @Test
    void updateAppsFeature_requestNull_NPE() {
        assertThatThrownBy(() -> passwordlessClient.updateAppsFeature(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("updateAppsFeature");
    }

    @Test
    void updateAppsFeature_errorResponse_PasswordlessApiException() throws JsonProcessingException {
        PasswordlessProblemDetails problemDetails = DataFactory.passwordlessProblemDetailsInvalidToken();

        wireMock.stubFor(post(urlEqualTo("/apps/features"))
                .willReturn(wireMockUtils.createProblemDetailsResponse(problemDetails)));

        UpdateAppsFeature updateAppsFeature = DataFactory.createUpdateAppsFeature();

        PasswordlessApiException passwordlessApiException = catchThrowableOfType(
                () -> passwordlessClient.updateAppsFeature(updateAppsFeature), PasswordlessApiException.class);

        assertThat(passwordlessApiException).isNotNull();
        assertThat(passwordlessApiException.getDetails()).isEqualTo(problemDetails);

        wireMockUtils.verifyPost("/apps/features", updateAppsFeature);
    }

    @Test
    void updateAppsFeature_validRequest_noError() throws PasswordlessApiException, IOException {
        wireMock.stubFor(post(urlEqualTo("/apps/features"))
                .willReturn(WireMock.ok()));

        UpdateAppsFeature updateAppsFeature = DataFactory.createUpdateAppsFeature();

        passwordlessClient.updateAppsFeature(updateAppsFeature);

        wireMockUtils.verifyPost("/apps/features", updateAppsFeature);
    }

    @Test
    void deleteCredentials_requestNull_NPE() {
        assertThatThrownBy(() -> passwordlessClient.deleteCredential(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("deleteCredential");
    }

    @Test
    void deleteCredentials_errorResponse_PasswordlessApiException() throws JsonProcessingException {
        PasswordlessProblemDetails problemDetails = DataFactory.passwordlessProblemDetailsInvalidToken();

        wireMock.stubFor(post(urlEqualTo("/credentials/delete"))
                .willReturn(wireMockUtils.createProblemDetailsResponse(problemDetails)));

        DeleteCredential deleteCredential = DataFactory.createDeleteCredential();

        PasswordlessApiException passwordlessApiException = catchThrowableOfType(
                () -> passwordlessClient.deleteCredential(deleteCredential), PasswordlessApiException.class);

        assertThat(passwordlessApiException).isNotNull();
        assertThat(passwordlessApiException.getDetails()).isEqualTo(problemDetails);

        wireMockUtils.verifyPost("/credentials/delete", deleteCredential);
    }

    @Test
    void deleteCredentials_validRequest_noError() throws PasswordlessApiException, IOException {
        wireMock.stubFor(post(urlEqualTo("/credentials/delete"))
                .willReturn(WireMock.ok()));

        DeleteCredential deleteCredential = DataFactory.createDeleteCredential();

        passwordlessClient.deleteCredential(deleteCredential);

        wireMockUtils.verifyPost("/credentials/delete", deleteCredential);
    }

    @Test
    void getCredentials_requestNull_NPE() {
        assertThatThrownBy(() -> passwordlessClient.getCredentials(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("userId");
    }

    @Test
    void getCredentials_errorResponse_PasswordlessApiException() {
    }

    @Test
    void getCredentials_validUserId_validResponse() {
    }

    @Test
    void registerToken_requestNull_NPE() {
        assertThatThrownBy(() -> passwordlessClient.registerToken(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("registerToken");
    }

    @Test
    void registerToken_errorResponse_PasswordlessApiException() {
    }

    @Test
    void registerToken_validRequest_validResponse() {
    }

    @Test
    void signIn_requestNull_NPE() {
        assertThatThrownBy(() -> passwordlessClient.signIn(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("verifySignIn");
    }

    @Test
    void signIn_errorResponse_PasswordlessApiException() {
    }

    @Test
    void signIn_validRequest_validResponse() {
    }

    @Test
    void getUsers_errorResponse_PasswordlessApiException() {
    }

    @Test
    void getUsers_valid_validResponse() {
    }

    @Test
    void deleteUser_requestNull_NPE() {
        assertThatThrownBy(() -> passwordlessClient.deleteUser(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("deleteUser");
    }

    @Test
    void deleteUser_errorResponse_PasswordlessApiException() {
    }

    @Test
    void deleteUser_validRequest_validResponse() {
    }

    @Test
    void close_closing_httpClientClosed() throws IOException {
        passwordlessClient.close();

        Exception exception = catchException(() -> passwordlessClient.getUsers());

        assertThat(exception).isNotNull();
    }
}
