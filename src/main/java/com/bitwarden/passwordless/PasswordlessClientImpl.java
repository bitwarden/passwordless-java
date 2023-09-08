package com.bitwarden.passwordless;

import com.bitwarden.passwordless.error.PasswordlessApiException;
import com.bitwarden.passwordless.model.*;
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
    final PasswordlessHttpClient passwordlessHttpClient;

    @Override
    public void createAlias(AliasPayload aliasPayload) throws PasswordlessApiException, IOException {
        Objects.requireNonNull(aliasPayload, "AliasPayload cannot be null");

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest("alias", aliasPayload);

        passwordlessHttpClient.sendRequest(request);
    }

    @Override
    public List<AliasPointer> getAliases(String userId) throws PasswordlessApiException, IOException {
        Objects.requireNonNull(userId, "userId cannot be null");

        ClassicHttpRequest request = passwordlessHttpClient.createGetRequest("alias/list",
                Collections.singletonMap("userId", userId));

        return passwordlessHttpClient.sendRequest(request);
    }

    @Override
    public void setAppsFeature(AppsSetFeatures appsSetFeature) throws PasswordlessApiException, IOException {
        Objects.requireNonNull(appsSetFeature, "AppsSetFeatures cannot be null");

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest("apps/features", appsSetFeature);

        passwordlessHttpClient.sendRequest(request);
    }

    @Override
    public void deleteCredentials(CredentialsDelete credentialsDelete) throws PasswordlessApiException, IOException {
        Objects.requireNonNull(credentialsDelete, "CredentialsDelete cannot be null");

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest("credentials/delete", credentialsDelete);

        passwordlessHttpClient.sendRequest(request);
    }

    @Override
    public List<StoredCredential> getCredentials(String userId) throws PasswordlessApiException, IOException {
        Objects.requireNonNull(userId, "userId cannot be null");

        ClassicHttpRequest request = passwordlessHttpClient.createGetRequest("credentials/list",
                Collections.singletonMap("userId", userId));

        return passwordlessHttpClient.sendRequest(request);
    }

    @Override
    public RegisterTokenResponse createRegisterToken(RegisterToken registerToken) throws PasswordlessApiException, IOException {
        Objects.requireNonNull(registerToken, "RegisterToken cannot be null");

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest("register/token", registerToken);

        return passwordlessHttpClient.sendRequest(request);
    }

    @Override
    public SignInVerifyToken signInVerify(SignInVerify signInVerify) throws PasswordlessApiException, IOException {
        Objects.requireNonNull(signInVerify, "SignInVerify cannot be null");

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest("signin/verify", signInVerify);

        return passwordlessHttpClient.sendRequest(request);
    }

    @Override
    public List<UserSummary> getUsers() throws PasswordlessApiException, IOException {
        ClassicHttpRequest request = passwordlessHttpClient.createGetRequest("users/list", Collections.emptyMap());

        return passwordlessHttpClient.sendRequest(request);
    }

    @Override
    public void deleteUser(UserDeletePayload userDeletePayload) throws PasswordlessApiException, IOException {
        Objects.requireNonNull(userDeletePayload, "UserDeletePayload cannot be null");

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest("users/delete", userDeletePayload);

        passwordlessHttpClient.sendRequest(request);
    }

    @Override
    public void close() throws IOException {
        passwordlessHttpClient.close();
    }
}
