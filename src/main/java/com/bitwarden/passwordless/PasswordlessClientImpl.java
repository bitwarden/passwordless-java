package com.bitwarden.passwordless;

import com.bitwarden.passwordless.error.PasswordlessApiException;
import com.bitwarden.passwordless.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ClassicHttpRequest;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
class PasswordlessClientImpl implements PasswordlessClient {

    @NonNull
    private final PasswordlessHttpClient passwordlessHttpClient;

    @Override
    public void setAlias(SetAlias setAlias) throws PasswordlessApiException, IOException {
        Objects.requireNonNull(setAlias, "setAlias cannot be null");

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest("alias", setAlias);

        passwordlessHttpClient.sendRequest(request, null);
    }

    @Override
    public List<Alias> getAliases(String userId) throws PasswordlessApiException, IOException {
        Objects.requireNonNull(userId, "userId cannot be null");

        ClassicHttpRequest request = passwordlessHttpClient.createGetRequest("alias/list",
                Collections.singletonMap("userId", userId));

        return passwordlessHttpClient.sendRequest(request, new TypeReference<ListResponse<Alias>>() {
        }).getValues();
    }

    @Override
    public void updateAppsFeature(UpdateAppsFeature updateAppsFeature) throws PasswordlessApiException, IOException {
        Objects.requireNonNull(updateAppsFeature, "updateAppsFeature cannot be null");

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest("apps/features", updateAppsFeature);

        passwordlessHttpClient.sendRequest(request, null);
    }

    @Override
    public void deleteCredential(DeleteCredential deleteCredential) throws PasswordlessApiException, IOException {
        Objects.requireNonNull(deleteCredential, "deleteCredential cannot be null");

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest("credentials/delete", deleteCredential);

        passwordlessHttpClient.sendRequest(request, null);
    }

    @Override
    public List<Credential> getCredentials(String userId) throws PasswordlessApiException, IOException {
        Objects.requireNonNull(userId, "userId cannot be null");

        ClassicHttpRequest request = passwordlessHttpClient.createGetRequest("credentials/list",
                Collections.singletonMap("userId", userId));

        return passwordlessHttpClient.sendRequest(request, new TypeReference<ListResponse<Credential>>() {
        }).getValues();
    }

    @Override
    public RegisteredToken registerToken(RegisterToken registerToken) throws PasswordlessApiException, IOException {
        Objects.requireNonNull(registerToken, "registerToken cannot be null");

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest("register/token", registerToken);

        return passwordlessHttpClient.sendRequest(request, new TypeReference<RegisteredToken>() {
        });
    }

    @Override
    public VerifiedUser signIn(VerifySignIn verifySignIn) throws PasswordlessApiException, IOException {
        Objects.requireNonNull(verifySignIn, "verifySignIn cannot be null");

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest("signin/verify", verifySignIn);

        return passwordlessHttpClient.sendRequest(request, new TypeReference<VerifiedUser>() {
        });
    }

    @Override
    public List<UserSummary> getUsers() throws PasswordlessApiException, IOException {
        ClassicHttpRequest request = passwordlessHttpClient.createGetRequest("users/list", Collections.emptyMap());

        return passwordlessHttpClient.sendRequest(request, new TypeReference<ListResponse<UserSummary>>() {
        }).getValues();
    }

    @Override
    public void deleteUser(DeleteUser deleteUser) throws PasswordlessApiException, IOException {
        Objects.requireNonNull(deleteUser, "deleteUser cannot be null");

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest("users/delete", deleteUser);

        passwordlessHttpClient.sendRequest(request, null);
    }

    @Override
    public void sendMagicLink(SendMagicLinkOptions options) throws PasswordlessApiException, IOException {
        Objects.requireNonNull(options, "options cannot be null");

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest("magic-links/send", options);

        passwordlessHttpClient.sendRequest(request, null);
    }

    @Override
    public GeneratedAuthenticationToken generateAuthenticationToken(GenerateAuthenticationTokenOptions options)
            throws PasswordlessApiException, IOException {
        Objects.requireNonNull(options, "options cannot be null");

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest("signin/generate-token", options);

        return passwordlessHttpClient.sendRequest(request, new TypeReference<GeneratedAuthenticationToken>() {
        });
    }

    @Override
    public void close() throws IOException {
        passwordlessHttpClient.close();
    }
}
