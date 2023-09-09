package com.bitwarden.passwordless;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;

@RequiredArgsConstructor
public class PasswordlessClientBuilder {

    @NonNull
    final PasswordlessOptions passwordlessOptions;

    private CloseableHttpClient httpClient;
    private ObjectMapper objectMapper;

    public static PasswordlessClientBuilder create(PasswordlessOptions passwordlessOptions) {
        return new PasswordlessClientBuilder(passwordlessOptions);
    }

    public PasswordlessClientBuilder httpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public PasswordlessClientBuilder objectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.copy();
        return this;
    }

    public PasswordlessClient build() {
        PasswordlessHttpClient passwordlessHttpClient = buildPasswordlessHttpClient();

        return new PasswordlessClientImpl(passwordlessHttpClient);
    }

    PasswordlessHttpClient buildPasswordlessHttpClient() {
        if (objectMapper == null) {
            objectMapper = JsonMapper.builder().build();
        }
        objectMapper.findAndRegisterModules();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        boolean closeHttpClient = false;
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
            closeHttpClient = true;
        }

        return new PasswordlessHttpClient(passwordlessOptions, httpClient, objectMapper, closeHttpClient);
    }
}
