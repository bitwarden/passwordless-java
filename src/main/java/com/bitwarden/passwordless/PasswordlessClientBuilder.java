package com.bitwarden.passwordless;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.*;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;

import java.util.Objects;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter(AccessLevel.PACKAGE)
public class PasswordlessClientBuilder {

    @NonNull
    private final PasswordlessOptions passwordlessOptions;
    private CloseableHttpClient httpClient;
    private boolean closeHttpClient = true;
    private ObjectMapper objectMapper;

    public static PasswordlessClientBuilder create(PasswordlessOptions passwordlessOptions) {
        Objects.requireNonNull(passwordlessOptions, "passwordlessOptions cannot be null");
        return new PasswordlessClientBuilder(passwordlessOptions);
    }

    public PasswordlessClientBuilder httpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
        this.closeHttpClient = false;
        return this;
    }

    public PasswordlessClientBuilder objectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.copy();
        return this;
    }

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
