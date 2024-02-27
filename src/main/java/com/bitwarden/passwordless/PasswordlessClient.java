package com.bitwarden.passwordless;

import com.bitwarden.passwordless.error.PasswordlessApiException;
import com.bitwarden.passwordless.model.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * Provides APIs that help you interact with Passwordless.dev.
 * <p>
 * Create new instance of the client with {@link PasswordlessClientBuilder#create(PasswordlessOptions)}.
 * <p>
 * When done, close the underlying I/O resources with {@link #close()}.
 *
 * @see PasswordlessClientBuilder
 */
public interface PasswordlessClient extends Closeable {

    /**
     * Creates or replaces existing aliases for a given user.
     *
     * @param setAlias {@link SetAlias} containing details about the aliases for a user.
     * @throws PasswordlessApiException If the Passwordless Api responds with an error.
     * @throws IOException              If there's an IO-related issue.
     * @see SetAlias
     */
    void setAlias(SetAlias setAlias)
            throws PasswordlessApiException, IOException;

    /**
     * List all aliases for a given user.
     *
     * @param userId The userId of the user for which the aliases will be returned.
     * @return List of {@link Alias aliases}.
     * @throws PasswordlessApiException If the Passwordless Api responds with an error.
     * @throws IOException              If there's an IO-related issue.
     * @see Alias
     */
    List<Alias> getAliases(String userId)
            throws PasswordlessApiException, IOException;

    /**
     * Updates application features for the account associated with your ApiSecret.
     *
     * @param updateAppsFeature {@link UpdateAppsFeature} containing details about the updatable features for an
     *                          application.
     * @throws PasswordlessApiException If the Passwordless Api responds with an error.
     * @throws IOException              If there's an IO-related issue.
     * @see UpdateAppsFeature
     */
    void updateAppsFeature(UpdateAppsFeature updateAppsFeature)
            throws PasswordlessApiException, IOException;

    /**
     * Attempts to delete a credential.
     *
     * @param deleteCredential {@link DeleteCredential} containing details about the credential id to be
     *                         deleted.
     * @throws PasswordlessApiException If the Passwordless Api responds with an error.
     * @throws IOException              If there's an IO-related issue.
     * @see PasswordlessApiException
     */
    void deleteCredential(DeleteCredential deleteCredential)
            throws PasswordlessApiException, IOException;

    /**
     * List all credentials for a given user.
     *
     * @param userId The userId of the user for which the credentials will be returned.
     * @return List of {@link Credential credentials}.
     * @throws PasswordlessApiException If the Passwordless Api responds with an error.
     * @throws IOException              If there's an IO-related issue.
     * @see Credential
     */
    List<Credential> getCredentials(String userId)
            throws PasswordlessApiException, IOException;

    /**
     * Creates a register token which will be used by your frontend to negotiate the creation of a WebAuth credential.
     *
     * @param registerToken {@link RegisterToken} containing details about the registration of the token.
     * @return Successfully {@link RegisteredToken registered token}.
     * @throws PasswordlessApiException If the Passwordless Api responds with an error.
     * @throws IOException              If there's an IO-related issue.
     * @see RegisterToken
     * @see RegisteredToken
     */
    RegisteredToken registerToken(RegisterToken registerToken)
            throws PasswordlessApiException, IOException;

    /**
     * Verifies that the given token is valid and returns information packed into it.
     * The token should have been generated via calling a <code>signInWith</code> method from your frontend code.
     *
     * @param verifySignIn {@link VerifySignIn} containing details about the token to verify.
     * @return {@link VerifiedUser User token details} upon successful verification of the token.
     * @throws PasswordlessApiException If the Passwordless Api responds with an error.
     * @throws IOException              If there's an IO-related issue.
     * @see VerifySignIn
     * @see VerifiedUser
     */
    VerifiedUser signIn(VerifySignIn verifySignIn)
            throws PasswordlessApiException, IOException;

    /**
     * List all users for the account associated with your ApiSecret.
     *
     * @return List of {@link UserSummary user summaries}.
     * @throws PasswordlessApiException If the Passwordless Api responds with an error.
     * @throws IOException              If there's an IO-related issue.
     * @see UserSummary
     */
    List<UserSummary> getUsers()
            throws PasswordlessApiException, IOException;

    /**
     * Deletes a user.
     *
     * @param deleteUser {@link DeleteUser} containing details about the user to delete.
     * @throws PasswordlessApiException If the Passwordless Api responds with an error.
     * @throws IOException              If there's an IO-related issue.
     * @see DeleteUser
     */
    void deleteUser(DeleteUser deleteUser)
            throws PasswordlessApiException, IOException;

    /**
     * Sends a magic link.
     *
     * @param magicLink {@link SendMagicLinkRequest} containing details about the magic link to send.
     * @throws PasswordlessApiException If the Passwordless Api responds with an error.
     * @throws IOException              If there's an IO-related issue.
     * @see SendMagicLinkRequest
     */
    void sendMagicLink(SendMagicLinkRequest magicLink)
            throws PasswordlessApiException, IOException;

    /**
     * Tries to close the I/O http client resources.
     *
     * @throws IOException If there's an IO-related issue.
     */
    @Override
    void close() throws IOException;
}
