package codexstester.abstractor.internal;

import codexstester.abstractor.AvailableHttpMethodTests;
import codexstester.abstractor.dto.HeadersDto;
import codexstester.abstractor.dto.RequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public abstract class AbstractTestsInternalRequestTests extends AvailableHttpMethodTests {

    @Autowired
    WebApplicationContext webApplicationContext;

    protected void setUp() {
        internalMockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    void executeInternalTest(RequestDto requestDto, HeadersDto headersDto) throws Exception {

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
                assertResultInternalTest();
        }
    }

    private void dispatcher(RequestDto requestDto, HeadersDto headersDto, String method) throws Exception {

        MockHttpServletRequestBuilder requestBuilder = null;
        String uri = internalUriBaseTest;
        ResultMatcher status = statusMockMvcTranslator(requestDto);

        if (requestDto.getUri() != null && !requestDto.getUri().equals("")) uri = requestDto.getUri();
        if (requestDto.getId() != null && !requestDto.getId().equals("")) uri = uri + "/" + requestDto.getId();

        String url = internalUrlBaseTest + uri;

        switch (method) {
            case "GET":
                requestBuilder = MockMvcRequestBuilders.get(url);
                break;
            case "POST":
                requestBuilder = MockMvcRequestBuilders.post(url);
                break;
            case "PUT":
                requestBuilder = MockMvcRequestBuilders.put(url);
                break;
            case "DELETE":
                requestBuilder = MockMvcRequestBuilders.delete(url);
                break;
            case "PATCH":
                requestBuilder = MockMvcRequestBuilders.patch(url);
                break;
            case "HEAD":
                requestBuilder = MockMvcRequestBuilders.head(url);
                break;
            case "OPTIONS":
                requestBuilder = MockMvcRequestBuilders.options(url);
                break;
            default:
                throw new RuntimeException("EXCEPTION[INVALID-HTTP-METHOD]: " + method);
        }

        MvcResult result = null;

        try {
            result = internalMockMvc.perform(
                    requestBuilder
                            .content(requestDto.getDataRequest())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .headers(codexsTesterInternalBuilderHeaders(requestDto, headersDto))
            ).andExpect(status).andReturn();

            System.out.println("RESULT[DEBUG]: "+result.getResponse().getContentAsString());

        } catch (Exception ex) {
            System.out.println("EXCEPTION[MOCK-MVC]: " + ex.getMessage());
        }

        /*Assert Content as String*/
        if (requestDto.getExpectedMessage() != null && !requestDto.getExpectedMessage().equals("")) {
            if (result != null && !result.getResponse().getContentAsString().equals("")) {
                System.out.println("Try assertIntegration: ");
                System.out.println("> " + requestDto.getExpectedMessage());
                System.out.println("> " + result.getResponse().getContentAsString());
                assertInternalTests(requestDto.getExpectedMessage(), result.getResponse().getContentAsString());
            }
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

    private void assertResultInternalTest() {
        System.out.println("CODEXS TESTER IS RUNNING IN INTERNAL MODE");
    }

}