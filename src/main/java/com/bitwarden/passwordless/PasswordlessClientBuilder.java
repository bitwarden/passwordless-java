package com.bitwarden.passwordless;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    public PasswordlessClientBuilder httpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public PasswordlessClientBuilder objectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public PasswordlessClient build() {
        boolean closeHttpClient = false;
        if (httpClient == null) {
            httpClient = HttpClientBuilder.create().build();
            closeHttpClient = true;
        }
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        PasswordlessHttpClient passwordlessHttpClient = new PasswordlessHttpClient(passwordlessOptions, httpClient,
                objectMapper, closeHttpClient);

        return new PasswordlessClientImpl(passwordlessHttpClient);
    }

    public static PasswordlessClientBuilder create(PasswordlessOptions passwordlessOptions) {
        return new PasswordlessClientBuilder(passwordlessOptions);
    }
}
