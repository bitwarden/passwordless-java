package com.bitwarden.passwordless.factory;

import com.bitwarden.passwordless.PasswordlessOptions;
import com.bitwarden.passwordless.model.TestRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DataFactory {
    public PasswordlessOptions passwordlessOptions(String url) {
        return PasswordlessOptions.builder()
                .apiUrl(url)
                .apiPrivateKey("javaapp:secret:34286d0b2cb24b8687d1639d20eb29fa")
                .build();
    }

    public TestRequest testRequest() {
        return TestRequest.builder()
                .fieldNumber(123)
                .build();
    }

}
