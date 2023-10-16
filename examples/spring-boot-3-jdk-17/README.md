# Passwordless Java SDK Example

Please read the documentation here: https://docs.passwordless.dev

## Getting Started

This example uses *Spring Boot 3 Web* and provides UI and REST interfaces to interact with
the [Passwordless Java SDK][passwordless-java-sdk] to the *Passwordless API*.

JDK 17 or newer is required to run the application.

1. Get your own API keys here: https://admin.passwordless.dev/signup
2. Change the value of the `passwordless.api.public-key` and `passwordless.api.private-key`
   in [application.properties](src/main/resources/application.properties) with your API Public Key and Secret.
3. (optional) In case of self-hosting, change the value of the `passwordless.api.url` with the base url where
   your *Passwordless API* instance is running.
4. Start the application

```shell
./mvnw spring-boot:run
```

5. The application will now listen on port `8080` e.g. http://localhost:8080, where you can *Sign In* and *Register*
   users within your Application.
6. For all Passwordless API functionalities supported by Java SDK, navigate to http://localhost:8080/swagger-ui/index.html
7. See [Example Passwordless REST Api requests and responses](example-rest-requests/passwordless-api.http)

[passwordless-java-sdk]:https://github.com/passwordless/passwordless-java-
