package com.bitwarden.passwordless.factory;

import com.bitwarden.passwordless.PasswordlessOptions;
import com.bitwarden.passwordless.error.PasswordlessProblemDetails;
import com.bitwarden.passwordless.model.TestRequest;
import com.bitwarden.passwordless.model.TestResponse;
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

    public TestResponse testResponse() {
        return TestResponse.builder()
                .field1(456)
                .build();
    }

    public PasswordlessProblemDetails passwordlessProblemDetailsInvalidToken() {
        return PasswordlessProblemDetails.builder()
                .type("https://docs.passwordless.dev/guide/errors.html#invalid_token")
                .title("The token you sent was not correct. The token used for this endpoint should start with 'verify_'. Make sure you are not sending the wrong value. The value you sent started with 'x'")
                .status(400)
                .errorCode("invalid_token")
                .detail("Makes sure your request contains the expected a value for the token.")
                .instance("/login")
                .build();
    }
}
