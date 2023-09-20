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
        PasswordlessClientBuilder clientBuilder = PasswordlessClientBuilder.create(passwordlessOptions);

        passwordlessClient = clientBuilder.build();

        objectMapper = clientBuilder.getObjectMapper();

        wireMockUtils = WireMockUtils.builder()
                .objectMapper(objectMapper)
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
        assertThat(passwordlessApiException.getProblemDetails()).isEqualTo(problemDetails);
    }

    @Test
    void createAlias_validRequest_noError() throws PasswordlessApiException, IOException {
        wireMock.stubFor(post(urlEqualTo("/alias"))
                .willReturn(WireMock.ok()));

        CreateAlias createAlias = DataFactory.createAlias();

        passwordlessClient.createAlias(createAlias);
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
        assertThat(passwordlessApiException.getProblemDetails()).isEqualTo(problemDetails);
    }

    @Test
    void getAliases_validUserId_validResponse() throws IOException, PasswordlessApiException {
        Alias alias1 = DataFactory.alias1();
        Alias alias2 = DataFactory.alias2();
        ListResponse<Alias> aliasListResponse = new ListResponse<>(Arrays.asList(alias1, alias2));

        wireMock.stubFor(get(urlPathEqualTo("/alias/list"))
                .withQueryParam("userId", equalTo(DataFactory.USER_ID))
                .willReturn(WireMock.ok().withResponseBody(wireMockUtils.createJsonBody(aliasListResponse))));

        List<Alias> aliases = passwordlessClient.getAliases(DataFactory.USER_ID);

        assertThat(aliases).containsExactlyInAnyOrder(alias1, alias2);
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

        UpdateAppsFeature updateAppsFeature = DataFactory.updateAppsFeature();

        PasswordlessApiException passwordlessApiException = catchThrowableOfType(
                () -> passwordlessClient.updateAppsFeature(updateAppsFeature), PasswordlessApiException.class);

        assertThat(passwordlessApiException).isNotNull();
        assertThat(passwordlessApiException.getProblemDetails()).isEqualTo(problemDetails);
    }

    @Test
    void updateAppsFeature_validRequest_noError() throws PasswordlessApiException, IOException {
        wireMock.stubFor(post(urlEqualTo("/apps/features"))
                .willReturn(WireMock.ok()));

        UpdateAppsFeature updateAppsFeature = DataFactory.updateAppsFeature();

        passwordlessClient.updateAppsFeature(updateAppsFeature);
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

        DeleteCredential deleteCredential = DataFactory.deleteCredential();

        PasswordlessApiException passwordlessApiException = catchThrowableOfType(
                () -> passwordlessClient.deleteCredential(deleteCredential), PasswordlessApiException.class);

        assertThat(passwordlessApiException).isNotNull();
        assertThat(passwordlessApiException.getProblemDetails()).isEqualTo(problemDetails);
    }

    @Test
    void deleteCredentials_validRequest_noError() throws PasswordlessApiException, IOException {
        wireMock.stubFor(post(urlEqualTo("/credentials/delete"))
                .willReturn(WireMock.ok()));

        DeleteCredential deleteCredential = DataFactory.deleteCredential();

        passwordlessClient.deleteCredential(deleteCredential);
    }

    @Test
    void getCredentials_requestNull_NPE() {
        assertThatThrownBy(() -> passwordlessClient.getCredentials(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("userId");
    }

    @Test
    void getCredentials_errorResponse_PasswordlessApiException() throws JsonProcessingException {
        PasswordlessProblemDetails problemDetails = DataFactory.passwordlessProblemDetailsInvalidToken();

        wireMock.stubFor(get(urlPathEqualTo("/credentials/list"))
                .withQueryParam("userId", equalTo(DataFactory.USER_ID))
                .willReturn(wireMockUtils.createProblemDetailsResponse(problemDetails)));

        PasswordlessApiException passwordlessApiException = catchThrowableOfType(
                () -> passwordlessClient.getCredentials(DataFactory.USER_ID), PasswordlessApiException.class);

        assertThat(passwordlessApiException).isNotNull();
        assertThat(passwordlessApiException.getProblemDetails()).isEqualTo(problemDetails);
    }

    @Test
    void getCredentials_validUserId_validResponse() throws IOException, PasswordlessApiException {
        Credential credential1 = DataFactory.credential1();
        Credential credential2 = DataFactory.credential2();
        ListResponse<Credential> credentialListResponse = new ListResponse<>(Arrays.asList(credential1, credential2));

        wireMock.stubFor(get(urlPathEqualTo("/credentials/list"))
                .withQueryParam("userId", equalTo(DataFactory.USER_ID))
                .willReturn(WireMock.ok().withResponseBody(wireMockUtils.createJsonBody(credentialListResponse))));

        List<Credential> credentials = passwordlessClient.getCredentials(DataFactory.USER_ID);

        assertThat(credentials).containsExactlyInAnyOrder(credential1, credential2);
    }

    @Test
    void registerToken_requestNull_NPE() {
        assertThatThrownBy(() -> passwordlessClient.registerToken(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("registerToken");
    }

    @Test
    void registerToken_errorResponse_PasswordlessApiException() throws JsonProcessingException {
        PasswordlessProblemDetails problemDetails = DataFactory.passwordlessProblemDetailsInvalidToken();

        wireMock.stubFor(post(urlEqualTo("/register/token"))
                .willReturn(wireMockUtils.createProblemDetailsResponse(problemDetails)));

        RegisterToken registerToken = DataFactory.registerToken();

        PasswordlessApiException passwordlessApiException = catchThrowableOfType(
                () -> passwordlessClient.registerToken(registerToken), PasswordlessApiException.class);

        assertThat(passwordlessApiException).isNotNull();
        assertThat(passwordlessApiException.getProblemDetails()).isEqualTo(problemDetails);
    }

    @Test
    void registerToken_validRequest_validResponse() throws PasswordlessApiException, IOException {
        RegisteredToken expectedRegisteredToken = DataFactory.registeredToken();

        wireMock.stubFor(post(urlEqualTo("/register/token"))
                .willReturn(WireMock.ok().withResponseBody(wireMockUtils.createJsonBody(expectedRegisteredToken))));

        RegisterToken registerToken = DataFactory.registerToken();

        RegisteredToken registeredToken = passwordlessClient.registerToken(registerToken);

        assertThat(registeredToken).isEqualTo(expectedRegisteredToken);
    }

    @Test
    void signIn_requestNull_NPE() {
        assertThatThrownBy(() -> passwordlessClient.signIn(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("verifySignIn");
    }

    @Test
    void signIn_errorResponse_PasswordlessApiException() throws JsonProcessingException {
        PasswordlessProblemDetails problemDetails = DataFactory.passwordlessProblemDetailsInvalidToken();

        wireMock.stubFor(post(urlEqualTo("/signin/verify"))
                .willReturn(wireMockUtils.createProblemDetailsResponse(problemDetails)));

        VerifySignIn verifySignIn = DataFactory.verifySignIn();

        PasswordlessApiException passwordlessApiException = catchThrowableOfType(
                () -> passwordlessClient.signIn(verifySignIn), PasswordlessApiException.class);

        assertThat(passwordlessApiException).isNotNull();
        assertThat(passwordlessApiException.getProblemDetails()).isEqualTo(problemDetails);
    }

    @Test
    void signIn_validRequest_validResponse() throws IOException, PasswordlessApiException {
        VerifiedUser expectedVerifiedUser = DataFactory.verifiedUser();

        wireMock.stubFor(post(urlEqualTo("/signin/verify"))
                .willReturn(WireMock.ok().withResponseBody(wireMockUtils.createJsonBody(expectedVerifiedUser))));

        VerifySignIn verifySignIn = DataFactory.verifySignIn();

        VerifiedUser verifiedUser = passwordlessClient.signIn(verifySignIn);

        assertThat(verifiedUser).isEqualTo(expectedVerifiedUser);
    }

    @Test
    void getUsers_errorResponse_PasswordlessApiException() throws JsonProcessingException {
        PasswordlessProblemDetails problemDetails = DataFactory.passwordlessProblemDetailsInvalidToken();

        wireMock.stubFor(get(urlPathEqualTo("/users/list"))
                .willReturn(wireMockUtils.createProblemDetailsResponse(problemDetails)));

        PasswordlessApiException passwordlessApiException = catchThrowableOfType(
                () -> passwordlessClient.getUsers(), PasswordlessApiException.class);

        assertThat(passwordlessApiException).isNotNull();
        assertThat(passwordlessApiException.getProblemDetails()).isEqualTo(problemDetails);
    }

    @Test
    void getUsers_valid_validResponse() throws PasswordlessApiException, IOException {
        UserSummary userSummary1 = DataFactory.userSummary1();
        UserSummary userSummary2 = DataFactory.userSummary2();
        UserSummary userSummary3 = DataFactory.userSummary3();
        ListResponse<UserSummary> userSummaryListResponse = new ListResponse<>(Arrays.asList(userSummary1, userSummary2,
                userSummary3));

        wireMock.stubFor(get(urlPathEqualTo("/users/list"))
                .willReturn(WireMock.ok().withResponseBody(wireMockUtils.createJsonBody(userSummaryListResponse))));

        List<UserSummary> users = passwordlessClient.getUsers();

        assertThat(users).containsExactlyInAnyOrder(userSummary1, userSummary2, userSummary3);
    }

    @Test
    void deleteUser_requestNull_NPE() {
        assertThatThrownBy(() -> passwordlessClient.deleteUser(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("deleteUser");
    }

    @Test
    void deleteUser_errorResponse_PasswordlessApiException() throws JsonProcessingException {
        PasswordlessProblemDetails problemDetails = DataFactory.passwordlessProblemDetailsInvalidToken();

        wireMock.stubFor(post(urlEqualTo("/users/delete"))
                .willReturn(wireMockUtils.createProblemDetailsResponse(problemDetails)));

        DeleteUser deleteUser = DataFactory.deleteUser();

        PasswordlessApiException passwordlessApiException = catchThrowableOfType(
                () -> passwordlessClient.deleteUser(deleteUser), PasswordlessApiException.class);

        assertThat(passwordlessApiException).isNotNull();
        assertThat(passwordlessApiException.getProblemDetails()).isEqualTo(problemDetails);
    }

    @Test
    void deleteUser_validRequest_validResponse() throws PasswordlessApiException, IOException {
        wireMock.stubFor(post(urlEqualTo("/users/delete"))
                .willReturn(WireMock.ok()));

        DeleteUser deleteUser = DataFactory.deleteUser();

        passwordlessClient.deleteUser(deleteUser);
    }

    @Test
    void close_closing_httpClientClosed() throws IOException {
        passwordlessClient.close();

        Exception exception = catchException(() -> passwordlessClient.getUsers());

        assertThat(exception).isNotNull();
    }
}
