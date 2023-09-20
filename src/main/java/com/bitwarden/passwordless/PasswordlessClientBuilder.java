package com.bitwarden.passwordless;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;

import java.util.Objects;

/**
 * The {@link PasswordlessClient} builder, which provides APIs that help you interact with Passwordless.dev.
 * <p>
 * Example:
 * <pre>
 * {@code
 * PasswordlessOptions passwordlessOptions = PasswordlessOptions.builder()
 *     .apiPrivateKey("...")
 *     .build();
 * PasswordlessClient passwordlessClient = PasswordlessClientBuilder.create(passwordlessOptions)
 *     .build();
 * }
 * </pre>
 * <p>
 * You can optionally provide custom {@link CloseableHttpClient Apache Http Client} with
 * {@link #httpClient(CloseableHttpClient)}.
 * Or provide custom {@link ObjectMapper FasterXml Jackson-Databind JSON (de)serializer} with
 * {@link #objectMapper(ObjectMapper)}.
 *
 * @see PasswordlessOptions
 * @see PasswordlessClient
 */
@RequiredArgsConstructor
@Getter(AccessLevel.PACKAGE)
public class PasswordlessClientBuilder {

    @NonNull
    private final PasswordlessOptions passwordlessOptions;
    private CloseableHttpClient httpClient;
    private boolean closeHttpClient = true;
    private ObjectMapper objectMapper;

    /**
     * Creates the builder for given passwordless configuration options.
     *
     * @param passwordlessOptions {@link PasswordlessOptions} with configuration options for Passwordless Api.
     * @return the builder instance.
     * @see PasswordlessOptions
     */
    public static PasswordlessClientBuilder create(PasswordlessOptions passwordlessOptions) {
        Objects.requireNonNull(passwordlessOptions, "passwordlessOptions cannot be null");
        return new PasswordlessClientBuilder(passwordlessOptions);
    }

    /**
     * Configures custom {@link CloseableHttpClient Apache Http Client} to use for Passwordless API client.
     * When not provided, the http client is built during the {@link #build()}.
     * <p>
     * Note that the {@link PasswordlessClient#close()} does not close custom provided http client.
     *
     * @param httpClient Your http client.
     * @return builder instance
     * @see CloseableHttpClient
     */
    public PasswordlessClientBuilder httpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
        this.closeHttpClient = false;
        return this;
    }

    /**
     * Configures custom {@link ObjectMapper FasterXml Jackson-Databind JSON (de)serializer} to use for
     * Passwordless API client.
     * When not provided, the ObjectMapper is built during the {@link #build()}.
     * <p>
     * Note that the provided ObjectMapper is copied into separate instance, managed by this builder with additional
     * configuration options.
     * This is done to maximize compatibility for the JSON (de)serialization used in Passwordless API.
     *
     * @param objectMapper Your JSON (de)serializer
     * @return builder instance
     * @see ObjectMapper
     */
    public PasswordlessClientBuilder objectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.copy();
        return this;
    }

    /**
     * Builds the Passwordless client.
     *
     * @return The {@link PasswordlessClient} instance.
     * @see PasswordlessClient
     */
    public PasswordlessClient build() {
        return new PasswordlessClientImpl(buildPasswordlessHttpClient());
    }

    void configureObjectMapper() {
        if (objectMapper == null) {
            objectMapper = JsonMapper.builder().build();
        }
        objectMapper.findAndRegisterModules();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    void configureHttpClient() {
        if (httpClient == null) {
            HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                    .setDefaultConnectionConfig(ConnectionConfig.custom()
                            .setSocketTimeout(Timeout.ofSeconds(30))
                            .setConnectTimeout(Timeout.ofSeconds(30))
                            .build())
                    .build();
            httpClient = HttpClientBuilder.create()
                    .setConnectionManager(connectionManager)
                    .build();
        }
    }

    PasswordlessHttpClient buildPasswordlessHttpClient() {
        configureObjectMapper();
        configureHttpClient();

        return new PasswordlessHttpClient(passwordlessOptions, httpClient, objectMapper,
                closeHttpClient);
    }
}
