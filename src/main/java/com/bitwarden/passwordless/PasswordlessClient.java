package com.bitwarden.passwordless;

import com.bitwarden.passwordless.error.PasswordlessApiException;
import com.bitwarden.passwordless.model.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public interface PasswordlessClient extends Closeable {

    void createAlias(CreateAlias createAlias)
            throws PasswordlessApiException, IOException;

    List<Alias> getAliases(String userId)
            throws PasswordlessApiException, IOException;

    void updateAppsFeature(UpdateAppsFeature updateAppsFeature)
            throws PasswordlessApiException, IOException;

    void deleteCredential(DeleteCredential deleteCredential)
            throws PasswordlessApiException, IOException;

    List<Credential> getCredentials(String userId)
            throws PasswordlessApiException, IOException;

    RegisteredToken registerToken(RegisterToken registerToken)
            throws PasswordlessApiException, IOException;

    VerifiedUser signIn(VerifySignIn verifySignIn)
            throws PasswordlessApiException, IOException;

    List<UserSummary> getUsers()
            throws PasswordlessApiException, IOException;

    void deleteUser(DeleteUser deleteUser)
            throws PasswordlessApiException, IOException;
}
