package com.bitwarden.passwordless.config;

import com.bitwarden.passwordless.PasswordlessClient;
import com.bitwarden.passwordless.PasswordlessClientBuilder;
import com.bitwarden.passwordless.PasswordlessOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordlessConfig {

    @Bean(destroyMethod = "close")
    public PasswordlessClient passwordlessApiClient(@Autowired PasswordlessApiConfig passwordlessApiConfig,
            @Autowired ObjectMapper objectMapper) {
        PasswordlessOptions passwordlessOptions = PasswordlessOptions.builder()
                .apiUrl(passwordlessApiConfig.getUrl())
                .apiSecret(passwordlessApiConfig.getSecret())
                .build();
        return PasswordlessClientBuilder.create(passwordlessOptions)
                .objectMapper(objectMapper)
                .build();
    }
}
