package com.bitwarden.passwordless;

import com.bitwarden.passwordless.error.PasswordlessApiException;
import com.bitwarden.passwordless.factory.DataFactory;
import com.bitwarden.passwordless.model.*;
import com.bitwarden.passwordless.utils.WireMockUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordlessClientBuilderTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .failOnUnmatchedRequests(true)
            .build();

    PasswordlessOptions passwordlessOptions;
    PasswordlessClient passwordlessClient;
    PasswordlessClientBuilder passwordlessClientBuilder;
    WireMockUtils wireMockUtils;

    @BeforeEach
    void setUp() {
        passwordlessOptions = DataFactory.passwordlessOptions(wireMock.baseUrl());
        passwordlessClientBuilder = PasswordlessClientBuilder.create(passwordlessOptions);
        passwordlessClientBuilder.configureObjectMapper();
        wireMockUtils = WireMockUtils.builder()
                .objectMapper(passwordlessClientBuilder.getObjectMapper())
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
    void create_nullPasswordlessOptions_NPE() {
        assertThatThrownBy(() -> PasswordlessClientBuilder.create(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("passwordlessOptions");
    }

    @Test
    void build_defaults_canMakeRequests() throws PasswordlessApiException, IOException {
        passwordlessClient = passwordlessClientBuilder.build();

        validateCanMakeApiRequest();
    }

    @Test
    void build_customHttpClient_canMakeRequests() throws PasswordlessApiException, IOException {
        AtomicInteger counter = new AtomicInteger(0);
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .addRequestInterceptorFirst((ignore, ignore2, ignore3) -> counter.incrementAndGet())
                .build()) {

            passwordlessClient = passwordlessClientBuilder.httpClient(httpClient).build();

            assertThat(counter).hasValue(0);

            validateCanMakeApiRequest();

            assertThat(counter).hasValue(2);
        }
    }

    @Test
    void build_customObjectMapper_canMakeRequest() throws PasswordlessApiException, IOException {
        List<String> expectedRegisteredModulesIds = Stream.of(new JavaTimeModule(), new ParameterNamesModule(),
                        new Jdk8Module())
                .map(Module::getTypeId)
                .map(Object::toString)
                .collect(Collectors.toList());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        passwordlessClient = passwordlessClientBuilder.objectMapper(objectMapper).build();

        assertThat(objectMapper.getRegisteredModuleIds()).isEmpty();
        assertThat(passwordlessClientBuilder.getObjectMapper().getRegisteredModuleIds())
                .containsExactlyInAnyOrderElementsOf(expectedRegisteredModulesIds);
        assertThat(passwordlessClientBuilder.getObjectMapper().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).isFalse();
        assertThat(passwordlessClientBuilder.getObjectMapper().isEnabled(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)).isFalse();
        assertThat(passwordlessClientBuilder.getObjectMapper().isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)).isFalse();

        validateCanMakeApiRequest();
    }

    private void validateCanMakeApiRequest() throws PasswordlessApiException, IOException {
        // Post
        wireMock.stubFor(post(urlEqualTo("/credentials/delete"))
                .willReturn(WireMock.ok()));

        passwordlessClient.deleteCredential(DataFactory.deleteCredential());

        // Get
        Credential credential1 = DataFactory.credential1();
        ListResponse<Credential> credentialListResponse = new ListResponse<>(Collections.singletonList(credential1));

        wireMock.stubFor(get(urlPathEqualTo("/credentials/list"))
                .withQueryParam("userId", equalTo(DataFactory.USER_ID))
                .willReturn(WireMock.ok().withResponseBody(wireMockUtils.createJsonBody(credentialListResponse))));

        List<Credential> credentials = passwordlessClient.getCredentials(DataFactory.USER_ID);

        assertThat(credentials).containsExactlyInAnyOrder(credential1);
    }
}
