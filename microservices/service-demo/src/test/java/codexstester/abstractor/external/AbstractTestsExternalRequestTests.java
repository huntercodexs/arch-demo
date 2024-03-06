package codexstester.abstractor.external;

import codexstester.abstractor.AvailableHttpMethodTests;
import codexstester.abstractor.dto.HeadersDto;
import codexstester.abstractor.dto.RequestDto;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

public abstract class AbstractTestsExternalRequestTests extends AvailableHttpMethodTests {

    @Autowired
    WebApplicationContext webApplicationContext;

    protected void setUp() {
        internalMockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    private static final RestTemplate abstractorRestTemplate = new RestTemplate();

    void executeExternalTest(RequestDto requestDto, HeadersDto headersDto) throws Exception {

        /*Fix a bug when getHttpMethod is null*/
        try {
            String method = headersDto.getHttpMethod();
            if (method == null) headersDto.setHttpMethod(HTTP_METHOD_TESTER);
        } catch (Exception ex) {
            headersDto.setHttpMethod(HTTP_METHOD_TESTER);
        }

        switch (headersDto.getHttpMethod()) {
            case HTTP_METHOD_GET:
                assertResultFromRequestByHttpGet(requestDto, headersDto);
                break;
            case HTTP_METHOD_POST:
                assertResultFromRequestByHttpPost(requestDto, headersDto);
                break;
            case HTTP_METHOD_PUT:
                assertResultFromRequestByHttpPut(requestDto, headersDto);
                break;
            case HTTP_METHOD_DELETE:
                assertResultFromRequestByHttpDelete(requestDto, headersDto);
                break;
            case HTTP_METHOD_PATCH:
                assertResultFromRequestByHttpPatch(requestDto, headersDto);
                break;
            case HTTP_METHOD_HEAD:
                assertResultFromRequestByHttpHead(requestDto, headersDto);
                break;
            case HTTP_METHOD_OPTIONS:
                assertResultFromRequestByHttpOptions(requestDto, headersDto);
                break;
            default:
                assertResultExternalTest();
        }
    }

    private void dispatcher(RequestDto requestDto, HeadersDto headersDto, String method) {

        String uri = externalUriBaseTest;

        if (requestDto.getUri() != null && !requestDto.getUri().equals("")) uri = requestDto.getUri();
        if (requestDto.getId() != null && !requestDto.getId().equals("")) uri = uri +"/"+ requestDto.getId();

        String url = externalUrlBaseTest + uri;
        HttpEntity<?> httpEntity = new HttpEntity<>(requestDto.getDataRequest(), codexsTesterExternalBuilderHeaders(requestDto, headersDto));

        try {

            ResponseEntity<Object> response = null;

            switch (method) {
                case HTTP_METHOD_GET:
                    response = abstractorRestTemplate.exchange(url, HttpMethod.GET, httpEntity, Object.class);
                    break;
                case HTTP_METHOD_POST:
                    response = abstractorRestTemplate.postForEntity(url, httpEntity, Object.class);
                    break;
                case HTTP_METHOD_DELETE:
                    response = abstractorRestTemplate.exchange(url, HttpMethod.DELETE, httpEntity, Object.class);
                    break;
                case HTTP_METHOD_PUT:
                    response = abstractorRestTemplate.exchange(url, HttpMethod.PUT, httpEntity, Object.class);
                    break;
                case HTTP_METHOD_PATCH:
                    abstractorRestTemplate.setRequestFactory(codexsTesterExternalHttpClientFactory());
                    response = abstractorRestTemplate.exchange(url, HttpMethod.PATCH, httpEntity, Object.class);
                    break;
                case HTTP_METHOD_HEAD:
                    response = abstractorRestTemplate.exchange(url, HttpMethod.HEAD, httpEntity, Object.class);
                    break;
                case HTTP_METHOD_OPTIONS:
                    response = abstractorRestTemplate.exchange(url, HttpMethod.OPTIONS, httpEntity, Object.class);
                    break;
                default:
                    throw new RuntimeException("INVALID HTTP METHOD: " + method);
            }

            Assert.assertEquals(response.getStatusCodeValue(), requestDto.getExpectedCode());

            System.out.println("RESPONSE[BODY]: " + response.getBody());

            if (requestDto.getExpectedMessage() != null && !requestDto.getExpectedMessage().equals("")) {
                Assert.assertEquals(requestDto.getExpectedMessage(), response.getBody());
            }

        } catch (HttpClientErrorException ex) {

            System.out.println("EXCEPTION[MESSAGE]: " + ex.getMessage());
            System.out.println("EXCEPTION[BODY]: " + ex.getResponseBodyAsString());

            Assert.assertEquals(ex.getRawStatusCode(), requestDto.getExpectedCode());

            if (requestDto.getExpectedMessage() != null && !requestDto.getExpectedMessage().equals("")) {
                try {
                    Assert.assertEquals(requestDto.getExpectedMessage(), ex.getResponseBodyAsString());
                } catch (Exception e) {
                    Assert.assertEquals(requestDto.getExpectedMessage(), ex.getMessage());
                }
            }
        } catch (HttpServerErrorException se) {

            System.out.println(se.getMessage());
            System.out.println(se.getResponseBodyAsString());

            Assert.assertEquals(se.getRawStatusCode(), requestDto.getExpectedCode());
        }
    }

    /**
     * @apiNote Using Http GET with Rest Template
     */
    private void assertResultFromRequestByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, HTTP_METHOD_GET);
    }

    /**
     * @apiNote Using Http POST with Rest Template
     */
    private void assertResultFromRequestByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, HTTP_METHOD_POST);
    }

    /**
     * @apiNote Using Http DELETE with Rest Template
     */
    private void assertResultFromRequestByHttpDelete(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, HTTP_METHOD_DELETE);
    }

    /**
     * @apiNote Using Http PUT with Rest Template
     */
    private void assertResultFromRequestByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, HTTP_METHOD_PUT);
    }

    /**
     * @apiNote Using Http PATCH with Rest Template
     */
    private void assertResultFromRequestByHttpPatch(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, HTTP_METHOD_PATCH);
    }

    /**
     * @apiNote Using Http HEAD with Rest Template
     */
    private void assertResultFromRequestByHttpHead(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, HTTP_METHOD_HEAD);
    }

    /**
     * @apiNote Using Http OPTIONS with Rest Template
     */
    private void assertResultFromRequestByHttpOptions(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, HTTP_METHOD_OPTIONS);
    }

    private void assertResultExternalTest() {
        System.out.println("CODEXS TESTER IS RUNNING IN EXTERNAL MODE");
    }

}