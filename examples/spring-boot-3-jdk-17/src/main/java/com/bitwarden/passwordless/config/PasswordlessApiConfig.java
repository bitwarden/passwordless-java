package com.bitwarden.passwordless.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "passwordless.api")
@Getter
@Setter
public class PasswordlessApiConfig {
    private String url;
    private String key;
    private String secret;
}
