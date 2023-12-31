package com.bitwarden.passwordless;

import com.bitwarden.passwordless.error.MalformedUrlException;
import com.bitwarden.passwordless.error.PasswordlessApiException;
import com.bitwarden.passwordless.error.PasswordlessProblemDetails;
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
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.net.URIBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
class PasswordlessHttpClient implements Closeable {
    @NonNull
    private final PasswordlessOptions passwordlessOptions;
    @NonNull
    private final CloseableHttpClient httpClient;
    @NonNull
    private final ObjectMapper objectMapper;
    @NonNull
    private final Boolean closeHttpClient;

    @Override
    public void close() throws IOException {
        if (closeHttpClient) {
            httpClient.close();
        }
    }

    public ClassicHttpRequest createGetRequest(String path, Map<String, String> queryParameters) throws IOException {
        return createRequest(ClassicRequestBuilder.get(), path, null, queryParameters);
    }

    public ClassicHttpRequest createPostRequest(String path, Object payload) throws IOException {
        Objects.requireNonNull(payload, "POST payload is null");

        return createRequest(ClassicRequestBuilder.post(), path, payload, null);
    }

    public <R> R sendRequest(ClassicHttpRequest request, TypeReference<R> typeReference) throws IOException, PasswordlessApiException {
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
                    byte[] responseBytes = EntityUtils.toByteArray(entity);

                    if (log.isDebugEnabled()) {
                        log.debug("Response body {}", new String(responseBytes, StandardCharsets.UTF_8));
                    }

                    if (typeReference != null && responseBytes != null && responseBytes.length > 0) {
                        return objectMapper.readValue(responseBytes, typeReference);
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
            throws IOException {
        URI uri;
        try {
            uri = new URIBuilder(passwordlessOptions.getApiUrl())
                    .appendPath(path)
                    .build();
        } catch (URISyntaxException e) {
            throw new MalformedUrlException("Invalid uri " + passwordlessOptions.getApiUrl() + "/" + path, e);
        }

        requestBuilder.setUri(uri);
        requestBuilder.addHeader(new LogMaskingHeader("ApiSecret", passwordlessOptions.getApiSecret()));

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
            if (contentType != null
                    && contentType.getValue().equalsIgnoreCase(ContentType.APPLICATION_PROBLEM_JSON.getMimeType())) {
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
                byte[] responseBytes = EntityUtils.toByteArray(entity);
                if (responseBytes != null && responseBytes.length > 0) {
                    errorDetail = new String(responseBytes, StandardCharsets.UTF_8);
                }
            }

            details = PasswordlessProblemDetails.builder()
                    .type("https://docs.passwordless.dev/guide/errors.html")
                    .status(response.getCode())
                    .title("Unexpected error")
                    .detail(errorDetail)
                    .build();
        }

        return details;
    }

    public static class LogMaskingHeader extends BasicHeader {

        public LogMaskingHeader(String name, Object value) {
            super(name, value, true);
        }

        @Override
        public String toString() {
            return getName() + ": **MASKED**";
        }
    }
}
