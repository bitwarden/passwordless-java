package com.bitwarden.passwordless;

import com.bitwarden.passwordless.error.PasswordlessApiException;
import com.bitwarden.passwordless.error.PasswordlessProblemDetails;
import com.bitwarden.passwordless.factory.DataFactory;
import com.bitwarden.passwordless.model.TestRequest;
import com.bitwarden.passwordless.model.TestResponse;
import com.bitwarden.passwordless.utils.WireMockUtils;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Body;
import com.github.tomakehurst.wiremock.http.ContentTypeHeader;
import com.github.tomakehurst.wiremock.http.MimeType;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class PasswordlessHttpClientTest {

    private static final String TEST_URL = "/login";

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .failOnUnmatchedRequests(true)
            .build();

    PasswordlessOptions passwordlessOptions;
    PasswordlessHttpClient passwordlessHttpClient;

    ObjectMapper objectMapper;

    WireMockUtils wireMockUtils;

    @BeforeEach
    void setUp() {
        passwordlessOptions = DataFactory.passwordlessOptions(wireMock.baseUrl());
    }

    @AfterEach
    void tearDown() throws IOException {
        if (passwordlessHttpClient != null) {
            passwordlessHttpClient.close();
            passwordlessHttpClient = null;
        }
    }

    @Test
    void createPostRequest_invalidApiUrl_IOException() {
        PasswordlessOptions passwordlessOptions = DataFactory.passwordlessOptions("http://localhost^:8080");

        initPasswordlessHttpClient(passwordlessOptions);

        TestRequest testRequest = DataFactory.testRequest();

        assertThatThrownBy(() -> passwordlessHttpClient.createPostRequest(TEST_URL, testRequest))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("http://localhost^:8080");
    }

    @Test
    void createPostRequest_nullPayload_NPE() {
        initPasswordlessHttpClient();

        assertThatThrownBy(() -> passwordlessHttpClient.createPostRequest(TEST_URL, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("POST payload is null");
    }

    @Test
    void createPostRequest_payload_noError() throws IOException, ProtocolException, URISyntaxException {
        initPasswordlessHttpClient();

        TestRequest testRequest = DataFactory.testRequest();
        String testRequestJson = objectMapper.writeValueAsString(testRequest);

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest(TEST_URL, testRequest);
        validateRequest(request, "POST", TEST_URL, testRequestJson);
    }

    @Test
    void createGetRequest_invalidApiUrl_IOException() {
        PasswordlessOptions passwordlessOptions = DataFactory.passwordlessOptions("http://localhost^:8080");
        initPasswordlessHttpClient(passwordlessOptions);

        assertThatThrownBy(() -> passwordlessHttpClient.createGetRequest(TEST_URL,
                Collections.singletonMap("userId", "testUser")))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("http://localhost^:8080");
    }

    @Test
    void createGetRequest_queryParametersNull_noError() throws IOException, ProtocolException, URISyntaxException {
        initPasswordlessHttpClient();

        ClassicHttpRequest request = passwordlessHttpClient.createGetRequest(TEST_URL, null);

        validateRequest(request, "GET", TEST_URL, null);
    }

    @Test
    void createGetRequest_queryParametersEmpty_noError() throws IOException, ProtocolException, URISyntaxException {
        initPasswordlessHttpClient();

        ClassicHttpRequest request = passwordlessHttpClient.createGetRequest(TEST_URL, Collections.emptyMap());

        validateRequest(request, "GET", TEST_URL, null);
    }

    @Test
    void createGetRequest_twoQueryParameters_noError() throws IOException, ProtocolException, URISyntaxException {
        initPasswordlessHttpClient();

        Map<String, String> queryParameters = new TreeMap<>();
        queryParameters.put("page", "1");
        queryParameters.put("userId", "testUser");

        ClassicHttpRequest request = passwordlessHttpClient.createGetRequest(TEST_URL, queryParameters);

        validateRequest(request, "GET", TEST_URL + "?page=1&userId=testUser", null);
    }

    @Test
    void sendRequest_unreachableApiUrl_IOException() throws IOException {
        int unreachablePort = 0;
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            assertThat(serverSocket).isNotNull();
            assertThat(serverSocket.getLocalPort()).isGreaterThan(0);
            unreachablePort = serverSocket.getLocalPort();
        } catch (IOException e) {
            fail("Port is not available");
        }

        String apiUrl = "http://localhost:" + unreachablePort;
        PasswordlessOptions passwordlessOptions = DataFactory.passwordlessOptions(apiUrl);

        initPasswordlessHttpClient(passwordlessOptions);

        TestRequest testRequest = DataFactory.testRequest();

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest(TEST_URL, testRequest);

        assertThatThrownBy(() -> passwordlessHttpClient.sendRequest(request, new TypeReference<TestResponse>() {
        }))
                .isInstanceOf(IOException.class)
                .hasMessageContaining(apiUrl)
                .hasMessageContaining("Connection refused");
    }

    @Test
    void sendRequest_notFoundPathResponseNoPayload_PasswordlessApiException() throws IOException {
        initPasswordlessHttpClient();

        wireMock.stubFor(post(urlEqualTo(TEST_URL))
                .willReturn(WireMock.notFound()));

        TestRequest testRequest = DataFactory.testRequest();

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest(TEST_URL, testRequest);

        PasswordlessApiException passwordlessApiException = catchThrowableOfType(
                () -> passwordlessHttpClient.sendRequest(request, new TypeReference<TestResponse>() {
                }), PasswordlessApiException.class);

        assertThat(passwordlessApiException).isInstanceOf(PasswordlessApiException.class);
        PasswordlessProblemDetails problemDetails = passwordlessApiException.getProblemDetails();
        assertThat(problemDetails).isNotNull();
        assertThat(problemDetails.getType()).isEqualTo("https://docs.passwordless.dev/guide/errors.html");
        assertThat(problemDetails.getTitle()).isEqualTo("Unexpected error");
        assertThat(problemDetails.getStatus()).isEqualTo(404);
        assertThat(problemDetails.getDetail()).isNull();
    }

    @Test
    void sendRequest_notFoundPathResponseWithPayload_PasswordlessApiException() throws IOException {
        initPasswordlessHttpClient();

        TestResponse testResponse = DataFactory.testResponse();
        String testResponseJson = objectMapper.writeValueAsString(testResponse);

        wireMock.stubFor(post(urlEqualTo(TEST_URL))
                .willReturn(WireMock.notFound()
                        .withResponseBody(wireMockUtils.createJsonBody(testResponse))));

        TestRequest testRequest = DataFactory.testRequest();

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest(TEST_URL, testRequest);

        PasswordlessApiException passwordlessApiException = catchThrowableOfType(
                () -> passwordlessHttpClient.sendRequest(request, new TypeReference<TestResponse>() {
                }), PasswordlessApiException.class);

        assertThat(passwordlessApiException).isInstanceOf(PasswordlessApiException.class);
        PasswordlessProblemDetails problemDetails = passwordlessApiException.getProblemDetails();
        assertThat(problemDetails).isNotNull();
        assertThat(problemDetails.getType()).isEqualTo("https://docs.passwordless.dev/guide/errors.html");
        assertThat(problemDetails.getTitle()).isEqualTo("Unexpected error");
        assertThat(problemDetails.getStatus()).isEqualTo(404);
        assertThat(problemDetails.getDetail()).isEqualTo(testResponseJson);
    }

    @Test
    void sendRequest_errorResponseProblemJson_PasswordlessApiException() throws IOException {
        initPasswordlessHttpClient();

        PasswordlessProblemDetails problemDetails = DataFactory.passwordlessProblemDetailsInvalidToken();

        wireMock.stubFor(post(urlEqualTo(TEST_URL))
                .willReturn(wireMockUtils.createProblemDetailsResponse(problemDetails)));

        TestRequest testRequest = DataFactory.testRequest();

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest(TEST_URL, testRequest);

        PasswordlessApiException passwordlessApiException = catchThrowableOfType(
                () -> passwordlessHttpClient.sendRequest(request, new TypeReference<TestResponse>() {
                }), PasswordlessApiException.class);

        assertThat(passwordlessApiException).isInstanceOf(PasswordlessApiException.class);
        assertThat(passwordlessApiException.getProblemDetails()).isEqualTo(problemDetails);
    }

    @Test
    void sendRequest_okResponseNotJson_IOException() throws IOException {
        initPasswordlessHttpClient();

        String xml = "<?xml version=\"1.0\"?><note><title>Test Node</title></node>";
        wireMock.stubFor(post(urlEqualTo(TEST_URL))
                .willReturn(WireMock.ok()
                        .withResponseBody(Body.ofBinaryOrText(xml.getBytes(StandardCharsets.UTF_8),
                                new ContentTypeHeader(MimeType.XML.toString())))));

        TestRequest testRequest = DataFactory.testRequest();

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest(TEST_URL, testRequest);

        IOException exception = catchThrowableOfType(
                () -> passwordlessHttpClient.sendRequest(request, new TypeReference<TestResponse>() {
                }), IOException.class);

        assertThat(exception).isInstanceOf(IOException.class).isInstanceOf(JacksonException.class);
    }

    @Test
    void sendRequest_getRequestOkResponseWithPayload_noError() throws PasswordlessApiException, IOException {
        initPasswordlessHttpClient();

        TestResponse testResponse = DataFactory.testResponse();

        wireMock.stubFor(get(urlEqualTo(TEST_URL))
                .willReturn(WireMock.ok()
                        .withResponseBody(wireMockUtils.createJsonBody(testResponse))));

        ClassicHttpRequest request = passwordlessHttpClient.createGetRequest(TEST_URL, null);

        TestResponse response = passwordlessHttpClient.sendRequest(request, new TypeReference<TestResponse>() {
        });

        assertThat(response).isEqualTo(testResponse);
    }

    @Test
    void sendRequest_postRequestOkResponseWithPayload_noError() throws PasswordlessApiException, IOException {
        initPasswordlessHttpClient();

        TestResponse testResponse = DataFactory.testResponse();

        wireMock.stubFor(post(urlEqualTo(TEST_URL))
                .willReturn(WireMock.ok()
                        .withResponseBody(wireMockUtils.createJsonBody(testResponse))));

        TestRequest testRequest = DataFactory.testRequest();

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest(TEST_URL, testRequest);

        TestResponse response = passwordlessHttpClient.sendRequest(request, new TypeReference<TestResponse>() {
        });

        assertThat(response).isEqualTo(testResponse);
    }

    @Test
    void sendRequest_postRequestOkResponseNoPayload_noError() throws IOException, PasswordlessApiException {
        initPasswordlessHttpClient();

        wireMock.stubFor(post(urlEqualTo(TEST_URL))
                .willReturn(WireMock.ok()));

        TestRequest testRequest = DataFactory.testRequest();

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest(TEST_URL, testRequest);

        Object response = passwordlessHttpClient.sendRequest(request, new TypeReference<TestResponse>() {
        });

        assertThat(response).isNull();
    }

    @Test
    void close_userProvidedHttpClient_httpClientNotClosed() throws IOException, PasswordlessApiException {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            PasswordlessClientBuilder clientBuilder = PasswordlessClientBuilder.create(passwordlessOptions)
                    .httpClient(httpClient);

            passwordlessHttpClient = clientBuilder.buildPasswordlessHttpClient();

            passwordlessHttpClient.close();

            // Apache HttpClient once closed cannot be re-used, verify by sending request
            wireMock.stubFor(post(urlEqualTo(TEST_URL))
                    .willReturn(WireMock.ok()));

            TestRequest testRequest = DataFactory.testRequest();

            ClassicHttpRequest request = passwordlessHttpClient.createPostRequest(TEST_URL, testRequest);

            passwordlessHttpClient.sendRequest(request, new TypeReference<TestResponse>() {
            });
        }
    }

    @Test
    void close_defaultHttpClient_httpClientClosed() throws IOException {
        initPasswordlessHttpClient();

        passwordlessHttpClient.close();

        wireMock.stubFor(post(urlEqualTo(TEST_URL))
                .willReturn(WireMock.ok()));

        TestRequest testRequest = DataFactory.testRequest();

        ClassicHttpRequest request = passwordlessHttpClient.createPostRequest(TEST_URL, testRequest);

        Exception exception = catchException(() -> passwordlessHttpClient.sendRequest(request, new TypeReference<TestResponse>() {
        }));

        assertThat(exception).isNotNull();

        wireMock.verify(0, anyRequestedFor(anyUrl()));
    }

    private void initPasswordlessHttpClient() {
        initPasswordlessHttpClient(this.passwordlessOptions);
    }

    private void initPasswordlessHttpClient(PasswordlessOptions passwordlessOptions) {
        PasswordlessClientBuilder clientBuilder = PasswordlessClientBuilder.create(passwordlessOptions);
        passwordlessHttpClient = clientBuilder.buildPasswordlessHttpClient();

        objectMapper = clientBuilder.getObjectMapper();

        wireMockUtils = WireMockUtils.builder()
                .objectMapper(objectMapper)
                .build();
    }

    private void validateRequest(ClassicHttpRequest request, String method, String path, String payloadJson)
            throws IOException, ProtocolException, URISyntaxException {
        assertThat(request).isNotNull();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getPath()).isEqualTo(path);
        assertThat(request.getUri()).isEqualTo(URI.create(passwordlessOptions.getApiUrl() + path));
        assertThat(request.getHeaders()).hasSize(1);
        assertThat(request.getHeader("ApiSecret")).isNotNull()
                .extracting(Header::getValue)
                .isEqualTo(passwordlessOptions.getApiPrivateKey());
        if (payloadJson != null) {
            assertThat(request.getEntity()).isNotNull();
            assertThat(EntityUtils.toString(request.getEntity())).isEqualTo(payloadJson);
        } else {
            assertThat(request.getEntity()).isNull();
        }
    }
}
