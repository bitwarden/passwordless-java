package com.bitwarden.passwordless.factory;

import com.bitwarden.passwordless.PasswordlessOptions;
import com.bitwarden.passwordless.error.PasswordlessProblemDetails;
import com.bitwarden.passwordless.model.*;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class DataFactory {

    public final String USER_ID = "eb4dee07-2d05-404e-80ed-0f65d0c4e30e";

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
                .instance("/some/api")
                .build();
    }

    public CreateAlias createAlias() {
        return CreateAlias.builder()
                .userId(USER_ID)
                .aliases(Arrays.asList("TestUser1", "TestUser2"))
                .build();
    }

    public Alias alias1() {
        return Alias.builder()
                .userId(USER_ID)
                .alias("xCsgbuTXbvoIhdyD7B2QSKkhWip8Y2zMRVA20sj+ihA=")
                .plaintext(null)
                .tenant("javaapp")
                .build();
    }

    public Alias alias2() {
        return Alias.builder()
                .userId(USER_ID)
                .alias("Yn3R4Gra0U7nr0vGEsHQVxHqpKZKgbchWQ1IoM8Snwk=")
                .plaintext(null)
                .tenant("javaapp")
                .build();
    }

    public UpdateAppsFeature createUpdateAppsFeature() {
        return UpdateAppsFeature.builder()
                .auditLoggingRetentionPeriod(12)
                .build();
    }

    public DeleteCredential createDeleteCredential() {
        return DeleteCredential.builder()
                .credentialId("ZtmCjN6tOMM5X_KxfYApAHI-5n6C4KRy9YMeMqfNjj8")
                .build();
    }
}
