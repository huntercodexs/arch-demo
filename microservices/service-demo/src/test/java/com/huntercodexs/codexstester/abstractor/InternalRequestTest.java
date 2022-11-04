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

        return headers;
    }

    /**
     * Using Http GET
     */
    protected void unauthorizedByHttpGet(String uri, String id) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(internalUriBaseTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", internalAuthorizationBasicInvalid)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    protected void methodNotAllowedByHttpGet(String uri, String id) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(internalUriBaseTest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isMethodNotAllowed())
                .andReturn();
    }

    protected void badRequestByHttpGet(String uri, String id) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(internalUriBaseTest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    protected void okByHttpGet(String uri, String id) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(internalUriBaseTest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    protected void createdByHttpGet(String uri, String id) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(internalUriBaseTest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isCreated())
                .andReturn();
    }

    protected void acceptedByHttpGet(String uri, String id) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(internalUriBaseTest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isAccepted())
                .andReturn();
    }

    protected void notFoundByHttpGet(String uri, String id) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(internalUriBaseTest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    protected void conflictByHttpGet(String uri, String id) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(internalUriBaseTest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isConflict())
                .andReturn();
    }

    protected void serverErrorByHttpGet(String uri, String id) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(internalUriBaseTest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    /**
     * Using Http POST
     */
    protected MvcResult unauthorizedByHttpPost(RequestDto requestDto, HeadersDto headersDto) throws Exception {

        if (!requestDto.getUri().equals("")) internalUriBaseTest = requestDto.getUri();
        if (!requestDto.getId().equals("")) internalUriBaseTest = internalUriBaseTest +"/"+ requestDto.getId();

        return mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(internalUriBaseTest)
                                .content(requestDto.getDataRequest())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .headers(getCurrentTestHeaders(requestDto, headersDto))
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    protected void methodNotAllowedByHttpPost(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isMethodNotAllowed())
                .andReturn();
    }

    protected void badRequestByHttpPost(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    protected void okByHttpPost(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    protected void createdByHttpPost(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isCreated())
                .andReturn();
    }

    protected void acceptedByHttpPost(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isAccepted())
                .andReturn();
    }

    protected void notFoundByHttpPost(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    protected void foundByHttpPost(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isFound())
                .andReturn();
    }

    protected void conflictByHttpPost(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isConflict())
                .andReturn();
    }

    protected void serverErrorByHttpPost(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    /**
     * Using Http PUT
     */
    protected void unauthorizedByHttpPut(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasicInvalid)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    protected void methodNotAllowedByHttpPut(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isMethodNotAllowed())
                .andReturn();
    }

    protected void badRequestByHttpPut(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    protected void okByHttpPut(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    protected void createdByHttpPut(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isCreated())
                .andReturn();
    }

    protected void acceptedByHttpPut(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isAccepted())
                .andReturn();
    }

    protected void notFoundByHttpPut(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    protected void conflictByHttpPut(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isConflict())
                .andReturn();
    }

    protected void serverErrorByHttpPut(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    /**
     * Using Http DELETE
     */
    protected void unauthorizedByHttpDelete(String uri, String id) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(internalUriBaseTest)
                        .header("Authorization", internalAuthorizationBasicInvalid)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    protected void methodNotAllowedByHttpDelete(String uri, String id) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(internalUriBaseTest)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isMethodNotAllowed())
                .andReturn();
    }

    protected void badRequestByHttpDelete(String uri, String id) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(internalUriBaseTest)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    protected void conflictByHttpDelete(String uri, String id) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(internalUriBaseTest)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isConflict())
                .andReturn();
    }

    protected void okByHttpDelete(String uri, String id) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(internalUriBaseTest)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    protected void acceptedByHttpDelete(String uri, String id) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(internalUriBaseTest)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isAccepted())
                .andReturn();
    }

    protected void notFoundByHttpDelete(String uri, String id) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(internalUriBaseTest)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    protected void serverErrorByHttpDelete(String uri, String id) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(internalUriBaseTest)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    /**
     * Using Http PATCH
     */
    protected void unauthorizedByHttpPatch(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasicInvalid)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    protected void methodNotAllowedByHttpPatch(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isMethodNotAllowed())
                .andReturn();
    }

    protected void badRequestByHttpPatch(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    protected void okByHttpPatch(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    protected void acceptedByHttpPatch(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isAccepted())
                .andReturn();
    }

    protected void notFoundByHttpPatch(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    protected void conflictByHttpPatch(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isConflict())
                .andReturn();
    }

    protected void serverErrorByHttpPatch(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    /**
     * Using Http HEAD
     */
    protected void unauthorizedByHttpHead(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .head(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasicInvalid)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    protected void methodNotAllowedByHttpHead(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .head(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isMethodNotAllowed())
                .andReturn();
    }

    protected void badRequestByHttpHead(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .head(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    protected void okByHttpHead(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .head(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    protected void notFoundByHttpHead(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .head(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    protected void conflictByHttpHead(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .head(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isConflict())
                .andReturn();
    }

    protected void serverErrorByHttpHead(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .head(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    /**
     * Using Http OPTIONS
     */
    protected void unauthorizedByHttpOptions(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .options(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasicInvalid)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    protected void methodNotAllowedByHttpOptions(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .options(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isMethodNotAllowed())
                .andReturn();
    }

    protected void badRequestByHttpOptions(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .options(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    protected void okByHttpOptions(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .options(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    protected void notFoundByHttpOptions(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .options(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    protected void conflictByHttpOptions(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .options(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isConflict())
                .andReturn();
    }

    protected void serverErrorByHttpOptions(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) internalUriBaseTest = uri;
        if (!id.equals("")) internalUriBaseTest = internalUriBaseTest +"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .options(internalUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", internalAuthorizationBasic)
                )
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

}