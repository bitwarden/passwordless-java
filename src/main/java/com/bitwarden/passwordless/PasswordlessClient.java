package com.bitwarden.passwordless;

import com.bitwarden.passwordless.error.PasswordlessApiException;
import com.bitwarden.passwordless.model.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public interface PasswordlessClient extends Closeable {

    void createAlias(AliasPayload aliasPayload)
            throws PasswordlessApiException, IOException;

    List<AliasPointer> getAliases(String userId)
            throws PasswordlessApiException, IOException;

    void setAppsFeature(AppsSetFeatures appsSetFeature)
            throws PasswordlessApiException, IOException;

    void deleteCredentials(CredentialsDelete credentialsDelete)
            throws PasswordlessApiException, IOException;

    List<StoredCredential> getCredentials(String userId)
            throws PasswordlessApiException, IOException;

    RegisterTokenResponse createRegisterToken(RegisterToken registerToken)
            throws PasswordlessApiException, IOException;

    SignInVerifyToken signInVerify(SignInVerify signInVerify)
            throws PasswordlessApiException, IOException;

    List<UserSummary> getUsers()
            throws PasswordlessApiException, IOException;

    void deleteUser(UserDeletePayload userDeletePayload)
            throws PasswordlessApiException, IOException;
}
