package com.bitwarden.passwordless.rest;

import com.bitwarden.passwordless.PasswordlessClient;
import com.bitwarden.passwordless.error.PasswordlessApiException;
import com.bitwarden.passwordless.error.PasswordlessProblemDetails;
import com.bitwarden.passwordless.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "passwordless/api",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class PasswordlessRestController {

    private final PasswordlessClient passwordlessClient;

    @Autowired
    public PasswordlessRestController(PasswordlessClient passwordlessClient) {
        this.passwordlessClient = passwordlessClient;
    }

    @PostMapping(path = "login")
    public VerifiedUser login(@RequestBody VerifySignIn verifySignIn) throws PasswordlessApiException, IOException {
        return passwordlessClient.signIn(verifySignIn);
    }

    @PostMapping(path = "register")
    public RegisteredToken register(@RequestBody RegisterToken registerToken) throws PasswordlessApiException, IOException {
        return passwordlessClient.registerToken(registerToken);
    }

    @PostMapping(path = "alias")
    public void setAlias(@RequestBody SetAlias setAlias) throws PasswordlessApiException, IOException {
        passwordlessClient.setAlias(setAlias);
    }

    @GetMapping(path = "alias/{userId}")
    public List<Alias> getAliases(@PathVariable String userId) throws PasswordlessApiException, IOException {
        return passwordlessClient.getAliases(userId);
    }

    @PutMapping(path = "apps/feature")
    public void setAppsFeature(@RequestBody UpdateAppsFeature updateAppsFeature) throws PasswordlessApiException, IOException {
        passwordlessClient.updateAppsFeature(updateAppsFeature);
    }

    @GetMapping(path = "credentials/{userId}")
    public List<Credential> getCredentials(@PathVariable String userId) throws PasswordlessApiException, IOException {
        return passwordlessClient.getCredentials(userId);
    }

    @DeleteMapping(path = "credentials")
    public void deleteCredential(@RequestBody DeleteCredential deleteCredential) throws PasswordlessApiException, IOException {
        passwordlessClient.deleteCredential(deleteCredential);
    }

    @GetMapping(path = "users")
    public List<UserSummary> getUsers() throws PasswordlessApiException, IOException {
        return passwordlessClient.getUsers();
    }

    @DeleteMapping(path = "users")
    public void deleteUser(@RequestBody DeleteUser deleteUser) throws PasswordlessApiException, IOException {
        passwordlessClient.deleteUser(deleteUser);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordlessApiException.class)
    public PasswordlessProblemDetails handlePasswordlessApiException(PasswordlessApiException exception) {
        return exception.getProblemDetails();
    }
}
