package com.bitwarden.passwordless;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

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
        boolean closeHttpClient = false;
        if (httpClient == null) {
            httpClient = HttpClientBuilder.create().build();
            closeHttpClient = true;
        }
        if (objectMapper == null) {
            objectMapper = JsonMapper.builder().build();
        }

        objectMapper.findAndRegisterModules();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        PasswordlessHttpClient passwordlessHttpClient = new PasswordlessHttpClient(passwordlessOptions, httpClient,
                objectMapper, closeHttpClient);

        return new PasswordlessClientImpl(passwordlessHttpClient);
    }
}
