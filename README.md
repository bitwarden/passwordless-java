![CI](https://github.com/bitwarden/passwordless-java/actions/workflows/ci.yml/badge.svg)
[![Maven Central](https://img.shields.io/maven-central/v/com.bitwarden/passwordless.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.bitwarden%22%20AND%20a:%22passwordless%22)


# Passwordless Java SDK

The official [Bitwarden Passwordless.dev](https://passwordless.dev/) Java library, for Java 8+ runtime.

## Installation

**Maven**: add to the `<dependencies>` in *pom.xml* file:

```xml

<dependency>
    <groupId>com.bitwarden</groupId>
    <artifactId>passwordless</artifactId>
    <version>1.2.3</version>
</dependency>
```

**Gradle**: add to the `dependencies` in *gradle.build* file:

```groovy
implementation group: 'com.bitwarden', name: 'passwordless', version: '1.2.3'
```

### Dependencies

- [Apache HttpClient][apache-http-client] for HTTP API
- [FasterXML jackson-databind][fasterxml-jackson-databind] for JSON (de)serialization

## Getting Started

Follow the [Get started guide][api-docs].

### Create `PasswordlessClient` instance:

```java
import com.bitwarden.passwordless.*;

import java.io.*;

public class PasswordlessJavaSdkExample implements Closeable {

    private final PasswordlessClient client;

    public PasswordlessClientExample() {
        PasswordlessOptions options = PasswordlessOptions.builder()
                .apiSecret("your_api_secret")
                .build();

        client = PasswordlessClientBuilder.create(options)
                .build();
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
```

**Note:** You need to close the underlying http client resources when you are done
using `PasswordlessClient` with `close` method.

### Register a passkey

```java
import com.bitwarden.passwordless.*;
import com.bitwarden.passwordless.error.*;
import com.bitwarden.passwordless.model.*;

import java.io.*;
import java.util.*;

public class PasswordlessJavaSdkExample {

    private final PasswordlessClient client;

    // Constructor

    public String getRegisterToken(String alias) throws PasswordlessApiException, IOException {

        // Get existing userid from session or create a new user.
        String userId = UUID.randomUUID().toString();

        // Options to give the Api
        RegisterToken registerToken = RegisterToken.builder()
                // your user id
                .userId(userId)
                // e.g. user email, is shown in browser ui
                .username(alias)
                // Optional: Link this userid to an alias (e.g. email)
                .aliases(Arrays.asList(alias))
                .build();

        RegisteredToken response = client.registerToken(registerToken);

        // return this token
        return response.getToken();
    }
}
```

### Verify user

```java
import com.bitwarden.passwordless.*;
import com.bitwarden.passwordless.error.*;
import com.bitwarden.passwordless.model.*;

import java.io.*;

public class PasswordlessJavaSdkExample {

    private final PasswordlessClient client;

    // Constructor

    public VerifiedUser verifySignInToken(String token) throws PasswordlessApiException, IOException {

        VerifySignIn signInVerify = VerifySignIn.builder()
                .token(token)
                .build();

        // Sign the user in, set a cookie, etc,
        return client.signIn(signInVerify);
    }
}
```

### Customization

Customize `PasswordlessOptions` by providing `apiSecret` with your Application's Private API Key.
You can also change the `apiUrl` if you prefer to self-host.

Customize `PasswordlessClientBuilder` by providing `httpClient` [CloseableHttpClient][apache-http-client] instance
and `objectMapper` [ObjectMapper][fasterxml-jackson-databind].

### Examples

See [Passwordless Example](examples/spring-boot-3-jdk-17) for Spring Boot 3 application using Java 17 runtime.

## Documentation

For a comprehensive list of examples, check out the [API documentation][api-docs].

## Contributing

This library compiles to Java 8 compatible runtime and requires minimum JDK 8 installed.

Newer JDK are still backwards compatible to version 8, so you are free to use any of the JDK version - tested up to
version 20.

Download and install [JDK 8](https://adoptium.net/temurin/releases/?version=8) if you do not have compatible JDK.

The `JAVA_HOME` environment variable needs to contain installed JDK path.

Build using Maven wrapper:

```shell
./mvnw clean install
```

(Or `mvwnw.cmd` for Windows)

[api-docs]:https://docs.passwordless.dev/guide/get-started.html

[apache-http-client]:https://hc.apache.org/httpcomponents-client-5.2.x/index.html

[fasterxml-jackson-databind]:https://github.com/FasterXML/jackson-databind
