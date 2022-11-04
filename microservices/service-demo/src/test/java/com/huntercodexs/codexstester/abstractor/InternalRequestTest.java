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
public abstract class InternalRequestTest {

    protected MockMvc mockMvc;
    private static final String propFile = "classpath:internal.test.properties";
    protected final Properties props = loadPropsTest();
    protected String internalUrlBaseTest = props.getProperty("internal.test.base-url");
    protected String internalUriBaseTest = props.getProperty("internal.test.base-uri");
    protected final String internalAuthorizationBasic = props.getProperty("internal.test.header.authorization-basic");
    protected final String internalAuthorizationBasicInvalid = props.getProperty("internal.test.header.authorization-basic-invalid");
    protected final String internalAuthorizationBearer = props.getProperty("internal.test.header.authorization-bearer");
    protected final String internalAuthorizationBearerInvalid = props.getProperty("internal.test.header.authorization-bearer-invalid");
    protected final String internalAppNameAuthorization = props.getProperty("internal.test.header.api-key.app-name");
    protected final String internalTokenAuthorization = props.getProperty("internal.test.header.api-key.token");
    protected final String internalSecretAuthorization = props.getProperty("internal.test.header.api-key.secret");
    protected final String internalValueAuthorization = props.getProperty("internal.test.header.api-key.value");
    protected final String internalGenericAuthorization = props.getProperty("internal.test.header.api-key.generic");
    protected final String internalAdditionalHeaderName = props.getProperty("internal.test.header.additional-name");
    protected final String internalAdditionalHeaderValue = props.getProperty("internal.test.header.additional-value");

    @Autowired
    WebApplicationContext webApplicationContext;

    protected static final RestTemplate restTemplate = new RestTemplate();

    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    public static Properties loadPropsTest() {
        Properties properties = new Properties();

        try {
            File file = ResourceUtils.getFile(InternalRequestTest.propFile);
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

    private MvcResult dispatcher(RequestDto requestDto, HeadersDto headersDto, ResultMatcher status, String method) throws Exception {

        MockHttpServletRequestBuilder requestBuilder = null;

        if (!requestDto.getUri().equals("")) internalUriBaseTest = requestDto.getUri();
        if (!requestDto.getId().equals("")) internalUriBaseTest = internalUriBaseTest +"/"+ requestDto.getId();

        switch (method) {
            case "GET":
                requestBuilder = MockMvcRequestBuilders.get(internalUriBaseTest);
                break;
            case "POST":
                requestBuilder = MockMvcRequestBuilders.post(internalUriBaseTest);
                break;
            case "PUT":
                requestBuilder = MockMvcRequestBuilders.put(internalUriBaseTest);
                break;
            case "DELETE":
                requestBuilder = MockMvcRequestBuilders.delete(internalUriBaseTest);
                break;
            case "PATCH":
                requestBuilder = MockMvcRequestBuilders.patch(internalUriBaseTest);
                break;
            case "HEAD":
                requestBuilder = MockMvcRequestBuilders.head(internalUriBaseTest);
                break;
            case "OPTIONS":
                requestBuilder = MockMvcRequestBuilders.options(internalUriBaseTest);
                break;
            default:
                throw new RuntimeException("INVALID HTTP METHOD: " + method);
        }

        return mockMvc.perform(
                requestBuilder
                        .content(requestDto.getDataRequest())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .headers(getCurrentTestHeaders(requestDto, headersDto))
                ).andExpect(status).andReturn();

    }

    /**
     * @apiNote Using Http GET
     */
    protected MvcResult unauthorizedByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isUnauthorized(), "GET");
    }

    protected MvcResult methodNotAllowedByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isMethodNotAllowed(), "GET");
    }

    protected MvcResult badRequestByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isBadRequest(), "GET");
    }

    protected MvcResult okByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isOk(), "GET");
    }

    protected MvcResult createdByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isCreated(), "GET");
    }

    protected MvcResult acceptedByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isAccepted(), "GET");
    }

    protected MvcResult notFoundByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isNotFound(), "GET");
    }

    protected MvcResult conflictByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isConflict(), "GET");
    }

    protected MvcResult serverErrorByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isInternalServerError(), "GET");
    }

    /**
     * @apiNote Using Http POST
     */
    protected MvcResult unauthorizedByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isUnauthorized(), "POST");
    }

    protected MvcResult methodNotAllowedByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isMethodNotAllowed(), "POST");
    }

    protected MvcResult badRequestByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isBadRequest(), "POST");
    }

    protected MvcResult okByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isOk(), "POST");
    }

    protected MvcResult createdByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isCreated(), "POST");
    }

    protected MvcResult acceptedByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isAccepted(), "POST");
    }

    protected MvcResult notFoundByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isNotFound(), "POST");
    }

    protected MvcResult foundByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isFound(), "POST");
    }

    protected MvcResult conflictByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isConflict(), "POST");
    }

    protected MvcResult serverErrorByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isInternalServerError(), "POST");
    }

    /**
     * @apiNote Using Http PUT
     */
    protected MvcResult unauthorizedByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isUnauthorized(), "PUT");
    }

    protected MvcResult methodNotAllowedByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isMethodNotAllowed(), "PUT");
    }

    protected MvcResult badRequestByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isBadRequest(), "PUT");
    }

    protected MvcResult okByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isOk(), "PUT");
    }

    protected MvcResult createdByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isCreated(), "PUT");
    }

    protected MvcResult acceptedByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isAccepted(), "PUT");
    }

    protected MvcResult notFoundByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isNotFound(), "PUT");
    }

    protected MvcResult conflictByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isConflict(), "PUT");
    }

    protected MvcResult serverErrorByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isInternalServerError(), "PUT");
    }

    /**
     * @apiNote Using Http DELETE
     */
    protected MvcResult unauthorizedByHttpDelete(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isUnauthorized(), "DELETE");
    }

    protected MvcResult methodNotAllowedByHttpDelete(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isMethodNotAllowed(), "DELETE");
    }

    protected MvcResult badRequestByHttpDelete(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isBadRequest(), "DELETE");
    }

    protected MvcResult conflictByHttpDelete(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isConflict(), "DELETE");
    }

    protected MvcResult okByHttpDelete(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isOk(), "DELETE");
    }

    protected MvcResult acceptedByHttpDelete(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isAccepted(), "DELETE");
    }

    protected MvcResult notFoundByHttpDelete(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isNotFound(), "DELETE");
    }

    protected MvcResult serverErrorByHttpDelete(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isInternalServerError(), "DELETE");
    }

    /**
     * @apiNote Using Http PATCH
     */
    protected MvcResult unauthorizedByHttpPatch(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isUnauthorized(), "PATCH");
    }

    protected MvcResult methodNotAllowedByHttpPatch(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isMethodNotAllowed(), "PATCH");
    }

    protected MvcResult badRequestByHttpPatch(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isBadRequest(), "PATCH");
    }

    protected MvcResult okByHttpPatch(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isOk(), "PATCH");
    }

    protected MvcResult acceptedByHttpPatch(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isAccepted(), "PATCH");
    }

    protected MvcResult notFoundByHttpPatch(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isNotFound(), "PATCH");
    }

    protected MvcResult conflictByHttpPatch(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isConflict(), "PATCH");
    }

    protected MvcResult serverErrorByHttpPatch(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isInternalServerError(), "PATCH");
    }

    /**
     * @apiNote Using Http HEAD
     */
    protected MvcResult unauthorizedByHttpHead(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isUnauthorized(), "HEAD");
    }

    protected MvcResult methodNotAllowedByHttpHead(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isMethodNotAllowed(), "HEAD");
    }

    protected MvcResult badRequestByHttpHead(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isBadRequest(), "HEAD");
    }

    protected MvcResult okByHttpHead(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isOk(), "HEAD");
    }

    protected MvcResult notFoundByHttpHead(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isNotFound(), "HEAD");
    }

    protected MvcResult conflictByHttpHead(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isConflict(), "HEAD");
    }

    protected MvcResult serverErrorByHttpHead(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isInternalServerError(), "HEAD");
    }

    /**
     * @apiNote Using Http OPTIONS
     */
    protected MvcResult unauthorizedByHttpOptions(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isUnauthorized(), "OPTIONS");
    }

    protected MvcResult methodNotAllowedByHttpOptions(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isMethodNotAllowed(), "OPTIONS");
    }

    protected MvcResult badRequestByHttpOptions(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isBadRequest(), "OPTIONS");
    }

    protected MvcResult okByHttpOptions(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isOk(), "OPTIONS");
    }

    protected MvcResult notFoundByHttpOptions(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isNotFound(), "OPTIONS");
    }

    protected MvcResult conflictByHttpOptions(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isConflict(), "OPTIONS");
    }

    protected MvcResult serverErrorByHttpOptions(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        return dispatcher(requestDto, headersDto, status().isInternalServerError(), "OPTIONS");
    }

}