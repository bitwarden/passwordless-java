package com.bitwarden.passwordless.factory;

import com.bitwarden.passwordless.PasswordlessOptions;
import com.bitwarden.passwordless.error.PasswordlessProblemDetails;
import com.bitwarden.passwordless.model.*;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

@UtilityClass
public class DataFactory {

    public final String USER_ID = "eb4dee07-2d05-404e-80ed-0f65d0c4e30e";

    public PasswordlessOptions passwordlessOptions(String url) {
        return PasswordlessOptions.builder()
                .apiUrl(url)
                .apiSecret("javaapp:secret:34286d0b2cb24b8687d1639d20eb29fa")
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

    public SetAlias setAlias() {
        return SetAlias.builder()
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

    public UpdateAppsFeature updateAppsFeature() {
        return UpdateAppsFeature.builder()
                .auditLoggingRetentionPeriod(12)
                .build();
    }

    public DeleteCredential deleteCredential() {
        return DeleteCredential.builder()
                .credentialId("ZtmCjN6tOMM5X_KxfYApAHI-5n6C4KRy9YMeMqfNjj8")
                .build();
    }

    public Credential credential1() {
        return Credential.builder()
                .userId(USER_ID)
                .descriptor(Credential.CredentialDescriptor.builder()
                        .type("public-key")
                        .id("ZtmCjN6tOMM5X_KxfYApAHI-5n6C4KRy9YMeMqfNjj8")
                        .build())
                .publicKey("pQECAyYgASFYIOsfC6kHh3pBohSGE6WwwGo8rJYG2lgmSbBfgtIq1gJzIlggr/6DYuFeATzcucHJ2ejCF2qWH7Z43yK4z/UYAV9YrY4=")
                .userHandle("ZWI0ZGVlMDctMmQwNS00MDRlLTgwZWQtMGY2NWQwYzRlMzBl")
                .signatureCounter(2)
                .attestationFmt("none")
                .createdAt(LocalDateTime.parse("2023-09-09T20:05:03.6059728"))
                .aaGuid("00000000-0000-0000-0000-000000000000")
                .lastUsedAt(LocalDateTime.parse("2023-09-09T20:09:59.6593325"))
                .origin("http://localhost:8080")
                .country("PL")
                .device("Chrome, Mac OS X 10")
                .build();
   }

    public Credential credential2() {
        return Credential.builder()
                .userId(USER_ID)
                .descriptor(Credential.CredentialDescriptor.builder()
                        .type("public-key")
                        .id("CYdwzAHqmUr85Dpei2kbWHs9xsBp1clzbG09VUcfnS0")
                        .build())
                .publicKey("pQECAyYgASFYIHSn0S/oH/sgZx12v37duci9gDkg0bB4f25h8p+6ecq2Ilgg2RadGaGqyJpNtm9ETrg+Uinf5n8SdPZN0oibSWb6TDc=")
                .userHandle("ODU5YTIyZTAtYmVmYS00ZWY0LWFjNDktNTRlZDkwYzFkZWIy")
                .signatureCounter(1)
                .attestationFmt("none")
                .createdAt(LocalDateTime.parse("2023-09-09T21:18:00.1309909"))
                .aaGuid("00000000-0000-0000-0000-000000000000")
                .lastUsedAt(LocalDateTime.parse("2023-09-09T21:32:50.2848782"))
                .origin("http://localhost:8080")
                .country("PL")
                .device("Chrome, Mac OS X 10")
                .build();
    }

    public RegisterToken registerToken() {
        return RegisterToken.builder()
                .userId(USER_ID)
                .username("TestUser")
                .attestation("none")
                .authenticatorType("any")
                .discoverable(true)
                .userVerification("preferred")
                .aliases(Collections.emptyList())
                .aliasHashing(true)
                .expiresAt(Instant.parse("2023-09-09T20:07:02.365573Z"))
                .build();
    }

    public RegisteredToken registeredToken() {
        return RegisteredToken.builder()
                .token("register_k8QgFOUhu_arMUbfi_93OZFdc6M39tPdmmNbx5xFZlMUS_TEgdwAE9f_VyjOIGT80GbZJDAwMDAwMDAwLTAwMDAtMDAwMC0wMDAwLTAwMDAwMDAwMDAwMMDAwMDAwMDA2SRlYjRkZWUwNy0yZDA1LTQwNGUtODBlZC0wZjY1ZDBjNGUzMGXAqFRlc3RVc2VypG5vbmWjYW55w6lwcmVmZXJyZWSQw84VNwZS")
                .build();
    }

    public VerifySignIn verifySignIn() {
        return VerifySignIn.builder()
                .token("verify_k8QgiPlgfMVr34FyFipBrkj6jBwKT9QifsFx-DSa1L3Yp_PE1NwAE9f_ppPH0GT80Y_ZJDBlZGQ2NWJjLTliOGQtNGIxYS1iMjA4LTIxYzZjOGYxYWQ5NK5wYXNza2V5X3NpZ25pbsDAwMDAwMDZJGViNGRlZTA3LTJkMDUtNDA0ZS04MGVkLTBmNjVkMGM0ZTMwZdf_ppOx8GT80RepbG9jYWxob3N0tWh0dHA6Ly9sb2NhbGhvc3Q6ODA4MMOzQ2hyb21lLCBNYWMgT1MgWCAxMKJQTMDEIGbZgozerTjDOV_ysX2AKQByPuZ-guCkcvWDHjKnzY4_zhU3BlI")
                .build();
    }

    public VerifiedUser verifiedUser() {
        return VerifiedUser.builder()
                .success(true)
                .userId(USER_ID)
                .timestamp(Instant.parse("2023-09-09T20:09:59.698674300Z"))
                .origin("http://localhost:8080")
                .device("Chrome, Mac OS X 10")
                .country("PL")
                .credentialId("ZtmCjN6tOMM5X/KxfYApAHI+5n6C4KRy9YMeMqfNjj8=")
                .expiresAt(Instant.parse("2023-09-09T20:11:59.698675700Z"))
                .tokenId("0edd65bc-9b8d-4b1a-b208-21c6c8f1ad94")
                .type("passkey_signin")
                .build();
    }

    public UserSummary userSummary1() {
        return UserSummary.builder()
                .userId(USER_ID)
                .aliasCount(2)
                .aliases(Arrays.asList(null, null))
                .credentialsCount(1)
                .lastUsedAt(LocalDateTime.parse("2023-09-09T20:09:59.6593325"))
                .build();
    }

    public UserSummary userSummary2() {
        return UserSummary.builder()
                .userId("859a22e0-befa-4ef4-ac49-54ed90c1deb2")
                .aliasCount(1)
                .aliases(Collections.singletonList("TestUser2"))
                .credentialsCount(1)
                .lastUsedAt(LocalDateTime.parse("2023-09-09T21:09:59.6593325"))
                .build();
    }

    public UserSummary userSummary3() {
        return UserSummary.builder()
                .userId("859a22e0-befa-4ef4-ac49-54ed90c1deb2")
                .aliasCount(0)
                .aliases(null)
                .credentialsCount(0)
                .lastUsedAt(LocalDateTime.parse("2023-09-09T22:09:59.6593325"))
                .build();
    }

    public DeleteUser deleteUser() {
        return DeleteUser.builder()
                .userId(USER_ID)
                .build();
    }

    public SendMagicLinkOptions sendMagicLinkRequest() {
        return SendMagicLinkOptions.builder()
                .userId(USER_ID)
                .emailAddress("support@paswordless.dev")
                .urlTemplate("https://www.example.com?token=$TOKEN")
                .build();
    }

    public GenerateAuthenticationTokenOptions generateAuthenticationTokenOptions() {
        return GenerateAuthenticationTokenOptions.builder()
                .userId(USER_ID)
                .build();
    }

    public GeneratedAuthenticationToken generatedAuthenticationToken() {
        return GeneratedAuthenticationToken.builder()
                .token("verify_k8QgiPlgfMVr34FyFipBrkj6jBwKT9QifsFx-DSa1L3Yp_PE1NwAE9f_ppPH0GT80Y_ZJDBlZGQ2NWJjLTliOGQtNGIxYS1iMjA4LTIxYzZjOGYxYWQ5NK5wYXNza2V5X3NpZ25pbsDAwMDAwMDZJGViNGRlZTA3LTJkMDUtNDA0ZS04MGVkLTBmNjVkMGM0ZTMwZdf_ppOx8GT80RepbG9jYWxob3N0tWh0dHA6Ly9sb2NhbGhvc3Q6ODA4MMOzQ2hyb21lLCBNYWMgT1MgWCAxMKJQTMDEIGbZgozerTjDOV_ysX2AKQByPuZ-guCkcvWDHjKnzY4_zhU3BlI")
                .build();
    }
}
