package com.bitwarden.passwordless;

import com.bitwarden.passwordless.error.PasswordlessApiException;
import com.bitwarden.passwordless.error.PasswordlessProblemDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.net.URIBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
class PasswordlessHttpClient implements Closeable {
    @NonNull
    final PasswordlessOptions passwordlessOptions;
    @NonNull
    final CloseableHttpClient httpClient;
    @NonNull
    final ObjectMapper objectMapper;
    @NonNull
    final Boolean closeHttpClient;

    @Override
    public void close() throws IOException {
        if (closeHttpClient) {
            httpClient.close();
        }
    }

    public ClassicHttpRequest createGetRequest(String path, Map<String, String> queryParameters) throws JsonProcessingException {
        return createRequest(ClassicRequestBuilder.get(), path, null, queryParameters);
    }

    public ClassicHttpRequest createPostRequest(String path, Object payload) throws JsonProcessingException {
        Objects.requireNonNull(payload);

        return createRequest(ClassicRequestBuilder.post(), path, payload, null);
    }

    public <R> R sendRequest(ClassicHttpRequest request) throws IOException, PasswordlessApiException {
        try {
            log.debug("Sending request {}", request);

            return httpClient.execute(request, response -> {
                HttpEntity entity = response.getEntity();

                log.debug("Response {} entity {}", response, entity);

                StatusLine status = new StatusLine(response);
                if (status.isError()) {
                    PasswordlessProblemDetails problemDetails = buildProblemDetails(response, entity);

                    log.debug("Problem details {}", problemDetails);

                    throw new HttpException("passwordless error", new PasswordlessApiException(problemDetails));
                }

                if (entity != null) {
                    try (InputStream inStream = entity.getContent()) {
                        return objectMapper.readValue(inStream, new TypeReference<R>() {
                        });
                    }
                }

                return null;
            });
        } catch (ClientProtocolException clientProtocolException) {
            log.trace("Request failed with error", clientProtocolException);

            if (clientProtocolException.getCause() instanceof HttpException) {
                HttpException httpException = (HttpException) clientProtocolException.getCause();
                if (httpException.getCause() instanceof PasswordlessApiException) {
                    throw (PasswordlessApiException) httpException.getCause();
                }
            }

            throw clientProtocolException;
        }
    }

    private ClassicHttpRequest createRequest(ClassicRequestBuilder requestBuilder, String path, Object payload,
                                             Map<String, String> queryParameters)
            throws JsonProcessingException {
        URI uri;
        try {
            uri = new URIBuilder(passwordlessOptions.getApiUrl())
                    .appendPath(path)
                    .build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }

        requestBuilder.setUri(uri);
        requestBuilder.addHeader("ApiSecret", passwordlessOptions.getApiPrivateKey());

        if (payload != null) {
            byte[] requestBody = objectMapper.writeValueAsBytes(payload);
            requestBuilder.setEntity(requestBody, ContentType.APPLICATION_JSON);
        }

        if (queryParameters != null) {
            queryParameters.forEach(requestBuilder::addParameter);
        }

        log.trace("Create request {} uri {} payload {}", requestBuilder, uri, payload);

        return requestBuilder.build();
    }

    private PasswordlessProblemDetails buildProblemDetails(ClassicHttpResponse response, HttpEntity entity) throws IOException, HttpException {

        PasswordlessProblemDetails details = null;
        if (entity != null) {
            Header contentType = response.getHeader("content-type");
            if (contentType != null && contentType.getValue().equalsIgnoreCase("application/problem+json")) {
                try (InputStream inStream = entity.getContent()) {
                    details = objectMapper.readValue(inStream, new TypeReference<PasswordlessProblemDetails>() {
                    });
                }
            }
        }

        if (details == null) {
            log.trace("Problem details fallback");

            String errorDetail = null;
            if (entity != null) {
                errorDetail = EntityUtils.toString(entity);
            }

            details = PasswordlessProblemDetails.builder()
                    .status(response.getCode())
                    .title("unexpected_error")
                    .detail(errorDetail)
                    .type("https://docs.passwordless.dev/errors")
                    .build();
        }

        return details;
    }
}
