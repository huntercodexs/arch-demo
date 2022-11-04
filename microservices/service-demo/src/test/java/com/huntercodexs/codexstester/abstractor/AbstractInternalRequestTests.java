package com.huntercodexs.codexstester.abstractor;

import com.huntercodexs.archdemo.demo.AddressApplication;
import com.huntercodexs.codexstester.abstractor.dto.HeadersDto;
import com.huntercodexs.codexstester.abstractor.dto.Oauth2RequestTokenDto;
import com.huntercodexs.codexstester.abstractor.dto.Oauth2ResponseTokenDto;
import com.huntercodexs.codexstester.abstractor.dto.RequestDto;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AddressApplication.class)
@WebAppConfiguration
public abstract class AbstractInternalRequestTests extends AvailableHttpMethodTests {

    protected MockMvc mockMvc;
    private static final String propFile = "classpath:internal.tests.properties";
    protected final Properties props = loadPropsTest();
    protected String internalUrlBaseTest = props.getProperty("internal.tests.base-url");
    protected String internalUriBaseTest = props.getProperty("internal.tests.base-uri");
    protected final String internalAuthorizationBasic = props.getProperty("internal.tests.header.authorization-basic");
    protected final String internalAuthorizationBasicInvalid = props.getProperty("internal.tests.header.authorization-basic-invalid");
    protected final String internalAuthorizationBearer = props.getProperty("internal.tests.header.authorization-bearer");
    protected final String internalAuthorizationBearerInvalid = props.getProperty("internal.tests.header.authorization-bearer-invalid");
    protected final String internalAppNameAuthorization = props.getProperty("internal.tests.header.api-key.app-name");
    protected final String internalTokenAuthorization = props.getProperty("internal.tests.header.api-key.token");
    protected final String internalSecretAuthorization = props.getProperty("internal.tests.header.api-key.secret");
    protected final String internalValueAuthorization = props.getProperty("internal.tests.header.api-key.value");
    protected final String internalGenericAuthorization = props.getProperty("internal.tests.header.api-key.generic");
    protected final String internalAdditionalHeaderName = props.getProperty("internal.tests.header.additional-name");
    protected final String internalAdditionalHeaderValue = props.getProperty("internal.tests.header.additional-value");

    @Autowired
    WebApplicationContext webApplicationContext;

    protected static final RestTemplate restTemplate = new RestTemplate();

    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    public static Properties loadPropsTest() {
        Properties properties = new Properties();

        try {
            File file = ResourceUtils.getFile(AbstractInternalRequestTests.propFile);
            InputStream in = Files.newInputStream(file.toPath());
            properties.load(in);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        return properties;
    }

    protected void createBeforeTest(String user_data) throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(internalUriBaseTest)
                                .content(user_data)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                ).andReturn();
    }

    protected void rollbackTest(String id) throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(internalUrlBaseTest + internalUriBaseTest +"/"+id)
                                .header("Authorization", internalAuthorizationBasic)
                ).andReturn();
    }

    protected void assertIntegration(String ref, String text) {
        if (text.contains(ref)) {
            Assert.assertEquals(1, 1);
        } else {
            Assert.assertEquals(1, 0);
        }
    }

    protected static ResponseEntity<Oauth2ResponseTokenDto> getToken(Oauth2RequestTokenDto oauth2RequestTokenDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Basic " + oauth2RequestTokenDto.getAuth().replaceFirst("Basic ", ""));
        String credentials = "?username="+ oauth2RequestTokenDto.getUser()+"&password="+ oauth2RequestTokenDto.getPass()+"&grant_type="+ oauth2RequestTokenDto.getGrant();
        HttpEntity<String> httpEntity = new HttpEntity<>(credentials, httpHeaders);
        return restTemplate.postForEntity(oauth2RequestTokenDto.getUrl() + credentials, httpEntity, Oauth2ResponseTokenDto.class);
    }

    private HttpHeaders getCurrentTestHeaders(RequestDto requestDto, HeadersDto headersDto) {

        HttpHeaders headers = new HttpHeaders();

        /*
         * From properties file
         */
        if (internalAuthorizationBasic != null && !internalAuthorizationBasic.equals("")) {
            headers.set("Authorization", "Basic " + internalAuthorizationBasic.replaceFirst("Basic ", ""));
        }
        if (internalAuthorizationBearer != null && !internalAuthorizationBearer.equals("")) {
            headers.set("Authorization", "Bearer " + internalAuthorizationBearer.replaceFirst("Bearer ", ""));
        }
        if (internalTokenAuthorization != null && !internalTokenAuthorization.equals("")) {
            headers.set("Api-Key-Token", internalTokenAuthorization);
        }
        if (internalAppNameAuthorization != null && !internalAppNameAuthorization.equals("")) {
            headers.set("Api-Key-App-Name", internalAppNameAuthorization);
        }
        if (internalSecretAuthorization != null && !internalSecretAuthorization.equals("")) {
            headers.set("Api-Key-Secret", internalSecretAuthorization);
        }
        if (internalValueAuthorization != null && !internalValueAuthorization.equals("")) {
            headers.set("Api-Key-Value", internalValueAuthorization);
        }
        if (internalGenericAuthorization != null && !internalGenericAuthorization.equals("")) {
            headers.set("Api-Key-Generic", internalGenericAuthorization);
        }
        if (internalAdditionalHeaderName != null && !internalAdditionalHeaderName.equals("")) {
            if (internalAdditionalHeaderValue != null && !internalAdditionalHeaderValue.equals("")) {
                headers.set(internalAdditionalHeaderName, internalAdditionalHeaderValue);
            }
        }

        /*
         * From HeadersDto Class (Overwrite above headers)
         */
        if (headersDto.getAuthorizationBasic() != null && !headersDto.getAuthorizationBasic().equals("")) {
            headers.set("Authorization", "Basic " + headersDto.getAuthorizationBasic().replaceFirst("Basic ", ""));
        }
        if (headersDto.getAuthorizationBearer() != null && !headersDto.getAuthorizationBearer().equals("")) {
            headers.set("Authorization", "Bearer " + headersDto.getAuthorizationBearer().replaceFirst("Bearer ", ""));
        }
        if (headersDto.getApiKeyToken() != null && !headersDto.getApiKeyToken().equals("")) {
            headers.set("Api-Key-Token", headersDto.getApiKeyToken());
        }
        if (headersDto.getApiKeyAppName() != null && !headersDto.getApiKeyAppName().equals("")) {
            headers.set("Api-Key-App-Name", headersDto.getApiKeyAppName());
        }
        if (headersDto.getApiKeySecret() != null && !headersDto.getApiKeySecret().equals("")) {
            headers.set("Api-Key-Secret", headersDto.getApiKeySecret());
        }
        if (headersDto.getApiKeyValue() != null && !headersDto.getApiKeyValue().equals("")) {
            headers.set("Api-Key-Value", headersDto.getApiKeyValue());
        }
        if (headersDto.getApiKeyGeneric() != null && !headersDto.getApiKeyGeneric().equals("")) {
            headers.set("Api-Key-Generic", headersDto.getApiKeyGeneric());
        }
        if (headersDto.getAddtionalName() != null && !headersDto.getAddtionalName().equals("")) {
            if (headersDto.getAddtionalValue() != null && !headersDto.getAddtionalValue().equals("")) {
                headers.set(headersDto.getAddtionalName(), headersDto.getAddtionalValue());
            }
        }

        /*Default Headers*/
        if (headersDto.getContentType() != null && !headersDto.getContentType().equals("")) {
            headers.set("Content-Type", headersDto.getContentType());
        } else {
            headers.set("Content-Type", "application/json;charset=UTF-8");
        }

        return headers;
    }

    private void dispatcher(RequestDto requestDto, HeadersDto headersDto, ResultMatcher status, String method) throws Exception {

        MockHttpServletRequestBuilder requestBuilder = null;

        if (requestDto.getUri() != null && !requestDto.getUri().equals("")) internalUriBaseTest = requestDto.getUri();
        if (requestDto.getId() != null && !requestDto.getId().equals("")) internalUriBaseTest = internalUriBaseTest +"/"+ requestDto.getId();

        switch (method) {
            case "GET":
                requestBuilder = MockMvcRequestBuilders.get(internalUrlBaseTest+internalUriBaseTest);
                break;
            case "POST":
                requestBuilder = MockMvcRequestBuilders.post(internalUrlBaseTest+internalUriBaseTest);
                break;
            case "PUT":
                requestBuilder = MockMvcRequestBuilders.put(internalUrlBaseTest+internalUriBaseTest);
                break;
            case "DELETE":
                requestBuilder = MockMvcRequestBuilders.delete(internalUrlBaseTest+internalUriBaseTest);
                break;
            case "PATCH":
                requestBuilder = MockMvcRequestBuilders.patch(internalUrlBaseTest+internalUriBaseTest);
                break;
            case "HEAD":
                requestBuilder = MockMvcRequestBuilders.head(internalUrlBaseTest+internalUriBaseTest);
                break;
            case "OPTIONS":
                requestBuilder = MockMvcRequestBuilders.options(internalUrlBaseTest+internalUriBaseTest);
                break;
            default:
                throw new RuntimeException("INVALID HTTP METHOD: " + method);
        }

        MvcResult result = mockMvc.perform(
                requestBuilder
                        .content(requestDto.getDataRequest())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .headers(getCurrentTestHeaders(requestDto, headersDto))
        ).andExpect(status).andReturn();

        System.out.println("RESULT[DEBUG]: "+result.getResponse().getContentAsString());

        /*Assert Content as String*/
        if (requestDto.getExpetecdMessage() != null && !requestDto.getExpetecdMessage().equals("")) {
            System.out.println("Try assertIntegration: ");
            System.out.println("> "+requestDto.getExpetecdMessage());
            System.out.println("> "+result.getResponse().getContentAsString());
            assertIntegration(requestDto.getExpetecdMessage(), result.getResponse().getContentAsString());
        }

    }

    /**
     * @apiNote Using Http GET
     */
    protected void unauthorizedByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isUnauthorized(), HTTP_METHOD_GET);
    }

    protected void methodNotAllowedByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isMethodNotAllowed(), HTTP_METHOD_GET);
    }

    protected void badRequestByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isBadRequest(), HTTP_METHOD_GET);
    }

    protected void okByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isOk(), HTTP_METHOD_GET);
    }

    protected void createdByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isCreated(), HTTP_METHOD_GET);
    }

    protected void acceptedByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isAccepted(), HTTP_METHOD_GET);
    }

    protected void notFoundByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isNotFound(), HTTP_METHOD_GET);
    }

    protected void conflictByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isConflict(), HTTP_METHOD_GET);
    }

    protected void serverErrorByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isInternalServerError(), HTTP_METHOD_GET);
    }

    /**
     * @apiNote Using Http POST
     */
    protected void unauthorizedByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isUnauthorized(), HTTP_METHOD_POST);
    }

    protected void methodNotAllowedByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isMethodNotAllowed(), HTTP_METHOD_POST);
    }

    protected void badRequestByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isBadRequest(), HTTP_METHOD_POST);
    }

    protected void okByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isOk(), HTTP_METHOD_POST);
    }

    protected void createdByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isCreated(), HTTP_METHOD_POST);
    }

    protected void acceptedByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isAccepted(), HTTP_METHOD_POST);
    }

    protected void notFoundByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isNotFound(), HTTP_METHOD_POST);
    }

    protected void foundByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isFound(), HTTP_METHOD_POST);
    }

    protected void conflictByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isConflict(), HTTP_METHOD_POST);
    }

    protected void serverErrorByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isInternalServerError(), HTTP_METHOD_POST);
    }

    /**
     * @apiNote Using Http PUT
     */
    protected void unauthorizedByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isUnauthorized(), HTTP_METHOD_PUT);
    }

    protected void methodNotAllowedByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isMethodNotAllowed(), HTTP_METHOD_PUT);
    }

    protected void badRequestByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isBadRequest(), HTTP_METHOD_PUT);
    }

    protected void okByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isOk(), HTTP_METHOD_PUT);
    }

    protected void createdByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isCreated(), HTTP_METHOD_PUT);
    }

    protected void acceptedByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isAccepted(), HTTP_METHOD_PUT);
    }

    protected void notFoundByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isNotFound(), HTTP_METHOD_PUT);
    }

    protected void conflictByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isConflict(), HTTP_METHOD_PUT);
    }

    protected void serverErrorByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isInternalServerError(), HTTP_METHOD_PUT);
    }

    /**
     * @apiNote Using Http DELETE
     */
    protected void unauthorizedByHttpDelete(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isUnauthorized(), HTTP_METHOD_DELETE);
    }

    protected void methodNotAllowedByHttpDelete(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isMethodNotAllowed(), HTTP_METHOD_DELETE);
    }

    protected void badRequestByHttpDelete(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isBadRequest(), HTTP_METHOD_DELETE);
    }

    protected void conflictByHttpDelete(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isConflict(), HTTP_METHOD_DELETE);
    }

    protected void okByHttpDelete(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isOk(), HTTP_METHOD_DELETE);
    }

    protected void acceptedByHttpDelete(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isAccepted(), HTTP_METHOD_DELETE);
    }

    protected void notFoundByHttpDelete(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isNotFound(), HTTP_METHOD_DELETE);
    }

    protected void serverErrorByHttpDelete(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isInternalServerError(), HTTP_METHOD_DELETE);
    }

    /**
     * @apiNote Using Http PATCH
     */
    protected void unauthorizedByHttpPatch(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isUnauthorized(), HTTP_METHOD_PATCH);
    }

    protected void methodNotAllowedByHttpPatch(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isMethodNotAllowed(), HTTP_METHOD_PATCH);
    }

    protected void badRequestByHttpPatch(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isBadRequest(), HTTP_METHOD_PATCH);
    }

    protected void okByHttpPatch(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isOk(), HTTP_METHOD_PATCH);
    }

    protected void acceptedByHttpPatch(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isAccepted(), HTTP_METHOD_PATCH);
    }

    protected void notFoundByHttpPatch(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isNotFound(), HTTP_METHOD_PATCH);
    }

    protected void conflictByHttpPatch(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isConflict(), HTTP_METHOD_PATCH);
    }

    protected void serverErrorByHttpPatch(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isInternalServerError(), HTTP_METHOD_PATCH);
    }

    /**
     * @apiNote Using Http HEAD
     */
    protected void unauthorizedByHttpHead(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isUnauthorized(), HTTP_METHOD_HEAD);
    }

    protected void methodNotAllowedByHttpHead(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isMethodNotAllowed(), HTTP_METHOD_HEAD);
    }

    protected void badRequestByHttpHead(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isBadRequest(), HTTP_METHOD_HEAD);
    }

    protected void okByHttpHead(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isOk(), HTTP_METHOD_HEAD);
    }

    protected void notFoundByHttpHead(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isNotFound(), HTTP_METHOD_HEAD);
    }

    protected void conflictByHttpHead(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isConflict(), HTTP_METHOD_HEAD);
    }

    protected void serverErrorByHttpHead(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isInternalServerError(), HTTP_METHOD_HEAD);
    }

    /**
     * @apiNote Using Http OPTIONS
     */
    protected void unauthorizedByHttpOptions(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isUnauthorized(), HTTP_METHOD_OPTIONS);
    }

    protected void methodNotAllowedByHttpOptions(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isMethodNotAllowed(), HTTP_METHOD_OPTIONS);
    }

    protected void badRequestByHttpOptions(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isBadRequest(), HTTP_METHOD_OPTIONS);
    }

    protected void okByHttpOptions(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isOk(), HTTP_METHOD_OPTIONS);
    }

    protected void notFoundByHttpOptions(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isNotFound(), HTTP_METHOD_OPTIONS);
    }

    protected void conflictByHttpOptions(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isConflict(), HTTP_METHOD_OPTIONS);
    }

    protected void serverErrorByHttpOptions(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, status().isInternalServerError(), HTTP_METHOD_OPTIONS);
    }

}