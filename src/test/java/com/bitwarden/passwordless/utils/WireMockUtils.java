package com.bitwarden.passwordless.utils;

import com.bitwarden.passwordless.error.PasswordlessProblemDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Body;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ContentType;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Builder
public class WireMockUtils {

    final ObjectMapper objectMapper;

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
}
