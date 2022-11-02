package com.huntercodexs.archdemo.demo.abstractor;

import com.huntercodexs.archdemo.demo.AddressApplication;
import com.huntercodexs.archdemo.demo.abstractor.dto.Oauth2RequestTokenDto;
import com.huntercodexs.archdemo.demo.abstractor.dto.Oauth2ResponseTokenDto;
import com.huntercodexs.archdemo.demo.abstractor.dto.RequestPostDto;
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
public abstract class IntegrationAbstractTest {

    protected MockMvc mockMvc;
    private static final String propFile = "classpath:integration.test.properties";
    protected final Properties props = loadPropsTest();
    protected String integrationUrlBaseTest = props.getProperty("integration.test.base-url");
    protected String integrationUriBaseTest = props.getProperty("integration.test.base-uri");
    protected final String integrationAuthorizationBasic = props.getProperty("integration.test.header.authorization-basic");
    protected final String integrationAuthorizationBasicInvalid = props.getProperty("integration.test.header.authorization-basic-invalid");
    protected final String integrationAuthorizationBearer = props.getProperty("integration.test.header.authorization-bearer");
    protected final String integrationAuthorizationBearerInvalid = props.getProperty("integration.test.header.authorization-bearer-invalid");
    protected final String integrationAppNameAuthorization = props.getProperty("integration.test.header.api-key.app-name");
    protected final String integrationTokenAuthorization = props.getProperty("integration.test.header.api-key.token");
    protected final String integrationSecretAuthorization = props.getProperty("integration.test.header.api-key.secret");
    protected final String integrationValueAuthorization = props.getProperty("integration.test.header.api-key.value");
    protected final String integrationGenericAuthorization = props.getProperty("integration.test.header.api-key.generic");
    protected final String integrationAdditionalHeaderName = props.getProperty("integration.test.header.additional-name");
    protected final String integrationAdditionalHeaderValue = props.getProperty("integration.test.header.additional-value");

    @Autowired
    WebApplicationContext webApplicationContext;

    protected static final RestTemplate restTemplate = new RestTemplate();

    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    public static Properties loadPropsTest() {
        Properties properties = new Properties();

        try {
            File file = ResourceUtils.getFile(IntegrationAbstractTest.propFile);
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
                                .post(integrationUriBaseTest)
                                .content(user_data)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                ).andReturn();
    }

    protected void rollbackTest(String id) throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(integrationUrlBaseTest+integrationUriBaseTest+"/"+id)
                                .header("Authorization", integrationAuthorizationBasic)
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

    private HttpHeaders getCurrentTestHeaders(RequestPostDto requestPostDto) {

        HttpHeaders headers = new HttpHeaders();

        switch (requestPostDto.getAuthType()) {
            case "Basic":
                if (requestPostDto.getAuthKey().equals("") || requestPostDto.getAuthKey() == null) {
                    headers.set("Authorization", "Basic " + integrationAuthorizationBasic.replaceFirst("Basic ", ""));
                } else {
                    headers.set("Authorization", "Basic " + requestPostDto.getAuthKey().replaceFirst("Basic ", ""));
                }
                break;
            case "Bearer":
                if (requestPostDto.getAuthKey().equals("") || requestPostDto.getAuthKey() == null) {
                    headers.set("Authorization", "Bearer " + integrationAuthorizationBearer.replaceFirst("Bearer ", ""));
                } else {
                    headers.set("Authorization", "Bearer " + requestPostDto.getAuthKey().replaceFirst("Bearer ", ""));
                }
                break;
            default:
                headers.set("Authorization", null);
        }

        if (!integrationTokenAuthorization.equals("")) {
            headers.set("Api-Key-Token", integrationTokenAuthorization);
        }
        if (!integrationAppNameAuthorization.equals("")) {
            headers.set("Api-Key-App-Name", integrationAppNameAuthorization);
        }
        if (!integrationSecretAuthorization.equals("")) {
            headers.set("Api-Key-Secret", integrationSecretAuthorization);
        }
        if (!integrationValueAuthorization.equals("")) {
            headers.set("Api-Key-Value", integrationValueAuthorization);
        }
        if (!integrationGenericAuthorization.equals("")) {
            headers.set("Api-Key-Generic", integrationGenericAuthorization);
        }
        if (!integrationAdditionalHeaderName.equals("") && !integrationAdditionalHeaderValue.equals("")) {
            headers.set(integrationAdditionalHeaderName, integrationAdditionalHeaderValue);
        }

        return headers;
    }

    /**
     * Using Http GET
     */
    protected void unauthorizedByHttpGet(String uri, String id) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(integrationUriBaseTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", integrationAuthorizationBasicInvalid)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    protected void methodNotAllowedByHttpGet(String uri, String id) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(integrationUriBaseTest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isMethodNotAllowed())
                .andReturn();
    }

    protected void badRequestByHttpGet(String uri, String id) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(integrationUriBaseTest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    protected void okByHttpGet(String uri, String id) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(integrationUriBaseTest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    protected void createdByHttpGet(String uri, String id) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(integrationUriBaseTest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isCreated())
                .andReturn();
    }

    protected void acceptedByHttpGet(String uri, String id) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(integrationUriBaseTest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isAccepted())
                .andReturn();
    }

    protected void notFoundByHttpGet(String uri, String id) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(integrationUriBaseTest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    protected void conflictByHttpGet(String uri, String id) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(integrationUriBaseTest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isConflict())
                .andReturn();
    }

    protected void serverErrorByHttpGet(String uri, String id) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(integrationUriBaseTest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    /**
     * Using Http POST
     *
     * @return
     */
    protected MvcResult unauthorizedByHttpPost(RequestPostDto requestPostDto) throws Exception {

        if (!requestPostDto.getUri().equals("")) integrationUriBaseTest = requestPostDto.getUri();
        if (!requestPostDto.getId().equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+requestPostDto.getId();

        return mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(integrationUrlBaseTest + integrationUriBaseTest)
                                .content(requestPostDto.getDataRequest())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .headers(getCurrentTestHeaders(requestPostDto))
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    protected void methodNotAllowedByHttpPost(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isMethodNotAllowed())
                .andReturn();
    }

    protected void badRequestByHttpPost(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    protected void okByHttpPost(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    protected void createdByHttpPost(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isCreated())
                .andReturn();
    }

    protected void acceptedByHttpPost(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isAccepted())
                .andReturn();
    }

    protected void notFoundByHttpPost(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    protected void foundByHttpPost(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isFound())
                .andReturn();
    }

    protected void conflictByHttpPost(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isConflict())
                .andReturn();
    }

    protected void serverErrorByHttpPost(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    /**
     * Using Http PUT
     */
    protected void unauthorizedByHttpPut(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasicInvalid)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    protected void methodNotAllowedByHttpPut(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isMethodNotAllowed())
                .andReturn();
    }

    protected void badRequestByHttpPut(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    protected void okByHttpPut(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    protected void createdByHttpPut(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isCreated())
                .andReturn();
    }

    protected void acceptedByHttpPut(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isAccepted())
                .andReturn();
    }

    protected void notFoundByHttpPut(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    protected void conflictByHttpPut(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isConflict())
                .andReturn();
    }

    protected void serverErrorByHttpPut(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    /**
     * Using Http DELETE
     */
    protected void unauthorizedByHttpDelete(String uri, String id) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(integrationUriBaseTest)
                        .header("Authorization", integrationAuthorizationBasicInvalid)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    protected void methodNotAllowedByHttpDelete(String uri, String id) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(integrationUriBaseTest)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isMethodNotAllowed())
                .andReturn();
    }

    protected void badRequestByHttpDelete(String uri, String id) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(integrationUriBaseTest)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    protected void conflictByHttpDelete(String uri, String id) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(integrationUriBaseTest)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isConflict())
                .andReturn();
    }

    protected void okByHttpDelete(String uri, String id) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(integrationUriBaseTest)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    protected void acceptedByHttpDelete(String uri, String id) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(integrationUriBaseTest)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isAccepted())
                .andReturn();
    }

    protected void notFoundByHttpDelete(String uri, String id) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(integrationUriBaseTest)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    protected void serverErrorByHttpDelete(String uri, String id) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(integrationUriBaseTest)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    /**
     * Using Http PATCH
     */
    protected void unauthorizedByHttpPatch(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasicInvalid)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    protected void methodNotAllowedByHttpPatch(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isMethodNotAllowed())
                .andReturn();
    }

    protected void badRequestByHttpPatch(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    protected void okByHttpPatch(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    protected void acceptedByHttpPatch(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isAccepted())
                .andReturn();
    }

    protected void notFoundByHttpPatch(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    protected void conflictByHttpPatch(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isConflict())
                .andReturn();
    }

    protected void serverErrorByHttpPatch(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    /**
     * Using Http HEAD
     */
    protected void unauthorizedByHttpHead(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .head(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasicInvalid)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    protected void methodNotAllowedByHttpHead(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .head(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isMethodNotAllowed())
                .andReturn();
    }

    protected void badRequestByHttpHead(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .head(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    protected void okByHttpHead(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .head(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    protected void notFoundByHttpHead(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .head(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    protected void conflictByHttpHead(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .head(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isConflict())
                .andReturn();
    }

    protected void serverErrorByHttpHead(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .head(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    /**
     * Using Http OPTIONS
     */
    protected void unauthorizedByHttpOptions(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .options(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasicInvalid)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    protected void methodNotAllowedByHttpOptions(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .options(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isMethodNotAllowed())
                .andReturn();
    }

    protected void badRequestByHttpOptions(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .options(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    protected void okByHttpOptions(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .options(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    protected void notFoundByHttpOptions(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .options(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    protected void conflictByHttpOptions(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .options(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isConflict())
                .andReturn();
    }

    protected void serverErrorByHttpOptions(String uri, String id, String dataRequest) throws Exception {

        if (!uri.equals("")) integrationUriBaseTest = uri;
        if (!id.equals("")) integrationUriBaseTest = integrationUriBaseTest+"/"+id;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .options(integrationUriBaseTest)
                                .content(dataRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", integrationAuthorizationBasic)
                )
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

}