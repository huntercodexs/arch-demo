package com.huntercodexs.codexstester.abstractor;

import com.huntercodexs.archdemo.demo.AddressApplication;
import com.huntercodexs.codexstester.abstractor.dto.HeadersDto;
import com.huntercodexs.codexstester.abstractor.dto.Oauth2RequestTokenDto;
import com.huntercodexs.codexstester.abstractor.dto.Oauth2ResponseTokenDto;
import com.huntercodexs.codexstester.abstractor.dto.RequestDto;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AddressApplication.class)
@WebAppConfiguration
public abstract class ExternalRequestTest extends CodexsHttpMethod {

    protected MockMvc mockMvc;
    protected static final RestTemplate restTemplate = new RestTemplate();
    private static final String propFile = "classpath:external.test.properties";
    protected final Properties props = loadPropsTest();
    protected String externalBaseTest = props.getProperty("external.test.base-url");
    protected String externalUriBaseTest = props.getProperty("external.test.base-uri");
    protected final String externalAuthorizationBasic = props.getProperty("external.test.header.authorization-basic");
    protected final String externalAuthorizationBasicInvalid = props.getProperty("external.test.header.authorization-basic-invalid");
    protected final String externalAuthorizationBearer = props.getProperty("external.test.header.authorization-bearer");
    protected final String externalAuthorizationBearerInvalid = props.getProperty("external.test.header.authorization-bearer-invalid");
    protected final String externalAppNameAuthorization = props.getProperty("external.test.header.api-key.app-name");
    protected final String externalTokenAuthorization = props.getProperty("external.test.header.api-key.token");
    protected final String externalSecretAuthorization = props.getProperty("external.test.header.api-key.secret");
    protected final String externalValueAuthorization = props.getProperty("external.test.header.api-key.value");
    protected final String externalGenericAuthorization = props.getProperty("external.test.header.api-key.generic");
    protected final String externalAdditionalHeaderName = props.getProperty("external.test.header.additional-name");
    protected final String externalAdditionalHeaderValue = props.getProperty("external.test.header.additional-value");

    @Autowired
    WebApplicationContext webApplicationContext;

    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    public static Properties loadPropsTest() {
        Properties properties = new Properties();

        try {
            File file = ResourceUtils.getFile(ExternalRequestTest.propFile);
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
                                .post(externalBaseTest+ externalUriBaseTest)
                                .content(user_data)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", externalAuthorizationBasic)
                ).andReturn();
    }

    protected void rollbackTest(String id) throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(externalBaseTest+ externalUriBaseTest +"/"+id)
                                .header("Authorization", externalAuthorizationBasic)
                ).andReturn();
    }

    protected void assertionTest(String ref, String text) {
        if (text.contains(ref)) {
            Assert.assertEquals(1, 1);
        } else {
            Assert.assertEquals(1, 0);
        }
    }

    protected HttpHeaders httpRequestHeaders(boolean invalidAuth) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (invalidAuth) {
            httpHeaders.set("Authorization", externalAuthorizationBasicInvalid);
        } else {
            httpHeaders.set("Authorization", externalAuthorizationBasic);
        }
        return httpHeaders;
    }

    protected HttpComponentsClientHttpRequestFactory httpClientFactory() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        return new HttpComponentsClientHttpRequestFactory(httpClient);
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
        if (externalAuthorizationBasic != null && !externalAuthorizationBasic.equals("")) {
            headers.set("Authorization", "Basic " + externalAuthorizationBasic.replaceFirst("Basic ", ""));
        }
        if (externalAuthorizationBearer != null && !externalAuthorizationBearer.equals("")) {
            headers.set("Authorization", "Bearer " + externalAuthorizationBearer.replaceFirst("Bearer ", ""));
        }
        if (externalTokenAuthorization != null && !externalTokenAuthorization.equals("")) {
            headers.set("Api-Key-Token", externalTokenAuthorization);
        }
        if (externalAppNameAuthorization != null && !externalAppNameAuthorization.equals("")) {
            headers.set("Api-Key-App-Name", externalAppNameAuthorization);
        }
        if (externalSecretAuthorization != null && !externalSecretAuthorization.equals("")) {
            headers.set("Api-Key-Secret", externalSecretAuthorization);
        }
        if (externalValueAuthorization != null && !externalValueAuthorization.equals("")) {
            headers.set("Api-Key-Value", externalValueAuthorization);
        }
        if (externalGenericAuthorization != null && !externalGenericAuthorization.equals("")) {
            headers.set("Api-Key-Generic", externalGenericAuthorization);
        }
        if (externalAdditionalHeaderName != null && !externalAdditionalHeaderName.equals("")) {
            if (externalAdditionalHeaderValue != null && !externalAdditionalHeaderValue.equals("")) {
                headers.set(externalAdditionalHeaderName, externalAdditionalHeaderValue);
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

    private void dispatcher(RequestDto requestDto, HeadersDto headersDto, String method) {

        if (!requestDto.getUri().equals("")) externalUriBaseTest = requestDto.getUri();
        if (!requestDto.getId().equals("")) externalUriBaseTest = externalUriBaseTest +"/"+ requestDto.getId();

        String url = externalBaseTest + externalUriBaseTest;
        HttpEntity<?> httpEntity = new HttpEntity<>(requestDto.getDataRequest(), getCurrentTestHeaders(requestDto, headersDto));

        try {

            ResponseEntity<Object> response = null;

            switch (method) {
                case HTTP_METHOD_GET:
                    response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Object.class);
                    break;
                case HTTP_METHOD_POST:
                    response = restTemplate.postForEntity(url, httpEntity, Object.class);
                    break;
                case HTTP_METHOD_DELETE:
                    response = restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, Object.class);
                    break;
                case HTTP_METHOD_PUT:
                    response = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Object.class);
                    break;
                case HTTP_METHOD_PATCH:
                    restTemplate.setRequestFactory(httpClientFactory());
                    response = restTemplate.exchange(url, HttpMethod.PATCH, httpEntity, Object.class);
                    break;
                case HTTP_METHOD_HEAD:
                    response = restTemplate.exchange(url, HttpMethod.HEAD, httpEntity, Object.class);
                    break;
                case HTTP_METHOD_OPTIONS:
                    response = restTemplate.exchange(url, HttpMethod.OPTIONS, httpEntity, Object.class);
                    break;
                default:
                    throw new RuntimeException("INVALID HTTP METHOD: " + method);
            }

            Assert.assertEquals(response.getStatusCodeValue(), requestDto.getExpectedCode());

            System.out.println("RESPONSE[BODY]: " + response.getBody());

            if (requestDto.getExpetecdMessage() != null && !requestDto.getExpetecdMessage().equals("")) {
                Assert.assertEquals(requestDto.getExpetecdMessage(), response.getBody());
            }

        } catch (HttpClientErrorException ex) {

            System.out.println("EXCEPTION[MESSAGE]: " + ex.getMessage());
            System.out.println("EXCEPTION[BODY]: " + ex.getResponseBodyAsString());

            Assert.assertEquals(ex.getRawStatusCode(), requestDto.getExpectedCode());

            if (requestDto.getExpetecdMessage() != null && !requestDto.getExpetecdMessage().equals("")) {
                try {
                    Assert.assertEquals(requestDto.getExpetecdMessage(), ex.getResponseBodyAsString());
                } catch (Exception e) {
                    Assert.assertEquals(requestDto.getExpetecdMessage(), ex.getMessage());
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
    protected void assertResultFromRequestByHttpGet(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, HTTP_METHOD_GET);
    }

    /**
     * @apiNote Using Http POST with Rest Template
     */
    protected void assertResultFromRequestByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, HTTP_METHOD_POST);
    }

    /**
     * @apiNote Using Http DELETE with Rest Template
     */
    protected void assertResultFromRequestByHttpDelete(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, HTTP_METHOD_DELETE);
    }

    /**
     * @apiNote Using Http PUT with Rest Template
     */
    protected void assertResultFromRequestByHttpPut(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, HTTP_METHOD_PUT);
    }

    /**
     * @apiNote Using Http PATCH with Rest Template
     */
    protected void assertResultFromRequestByHttpPatch(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, HTTP_METHOD_PATCH);
    }

    /**
     * @apiNote Using Http HEAD with Rest Template
     */
    protected void assertResultFromRequestByHttpHead(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, HTTP_METHOD_HEAD);
    }

    /**
     * @apiNote Using Http OPTIONS with Rest Template
     */
    protected void aassertResultFromRequestByHttpOptions(RequestDto requestDto, HeadersDto headersDto) throws Exception {
        dispatcher(requestDto, headersDto, HTTP_METHOD_OPTIONS);
    }
}