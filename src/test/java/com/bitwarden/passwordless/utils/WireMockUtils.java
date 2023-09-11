package com.bitwarden.passwordless.utils;

import com.bitwarden.passwordless.PasswordlessOptions;
import com.bitwarden.passwordless.error.PasswordlessProblemDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Body;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ContentType;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RequiredArgsConstructor
@Builder
public class WireMockUtils {

    final ObjectMapper objectMapper;
    final WireMockExtension wireMockExtension;
    final PasswordlessOptions passwordlessOptions;

    public ResponseDefinitionBuilder createProblemDetailsResponse(PasswordlessProblemDetails problemDetails)
            throws JsonProcessingException {
        return WireMock.status(problemDetails.getStatus())
                .withHeader("Content-Type", ContentType.APPLICATION_PROBLEM_JSON.getMimeType())
                .withResponseBody(createJsonBody(problemDetails));
    }

    public Body createJsonBody(Object request) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(request);

        return Body.fromJsonBytes(json.getBytes(StandardCharsets.UTF_8));
    }

    public void verifyPost(String url, Object request) throws JsonProcessingException {
        wireMockExtension.verify(1, postRequestedFor(urlEqualTo(url))
                .withHeader("Content-Type", equalTo(ContentType.APPLICATION_JSON.toString()))
                .withHeader("ApiSecret", equalTo(passwordlessOptions.getApiPrivateKey()))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(request))));
    }

    public void verifyGet(String url, Map<String, String> queryParams) {
        RequestPatternBuilder requestPatternBuilder = getRequestedFor(urlPathEqualTo(url));
        if (queryParams != null) {
            queryParams.forEach((key, value) -> requestPatternBuilder.withQueryParam(key, equalTo(value)));
        }
        requestPatternBuilder.withHeader("ApiSecret", equalTo(passwordlessOptions.getApiPrivateKey()));

        wireMockExtension.verify(1, requestPatternBuilder);
    }
}
