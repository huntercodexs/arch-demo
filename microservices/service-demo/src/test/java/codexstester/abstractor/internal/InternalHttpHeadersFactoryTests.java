package codexstester.abstractor.internal;

import codexstester.abstractor.dto.*;
import codexstester.setup.dataproperty.ExternalPropertyTestsTestsTests;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

public abstract class InternalHttpHeadersFactoryTests extends ExternalPropertyTestsTestsTests {

    @Autowired
    WebApplicationContext webApplicationContext;

    protected void setUp() {
        internalMockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected MockMvc internalMockMvc;

    private static final RestTemplate internalRestTemplate = new RestTemplate();

    protected void createBeforeInternalTests(String user_data) throws Exception {
        internalMockMvc.perform(
                MockMvcRequestBuilders
                        .post(internalUriBaseTest)
                        .content(user_data)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", internalAuthorizationBasic)
        ).andReturn();
    }

    protected void rollbackInternalTests(String id) throws Exception {
        internalMockMvc.perform(
                MockMvcRequestBuilders
                        .delete(internalUrlBaseTest + internalUriBaseTest +"/"+id)
                        .header("Authorization", internalAuthorizationBasic)
        ).andReturn();
    }

    protected void assertInternalTests(String ref, String text) {
        if (text.contains(ref)) {
            Assert.assertEquals(1, 1);
        } else {
            Assert.assertEquals(1, 0);
        }
    }

    protected static ResponseEntity<Oauth2ResponseTokenDto> codexsTesterInternalOAuth2GetToken(Oauth2RequestTokenDto oauth2RequestTokenDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Basic " + oauth2RequestTokenDto.getAuth().replaceFirst("Basic ", ""));
        String credentials = "?username="+ oauth2RequestTokenDto.getUser()+"&password="+ oauth2RequestTokenDto.getPass()+"&grant_type="+ oauth2RequestTokenDto.getGrant();
        HttpEntity<String> httpEntity = new HttpEntity<>(credentials, httpHeaders);
        return internalRestTemplate.postForEntity(oauth2RequestTokenDto.getUrl() + credentials, httpEntity, Oauth2ResponseTokenDto.class);
    }

    protected static ResponseEntity<Object> codexsTesterInternalOAuth2CheckToken(Oauth2RequestCheckTokenDto oauth2RequestCheckTokenDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Basic " + oauth2RequestCheckTokenDto.getAuthorization().replaceFirst("Basic ", ""));

        if (oauth2RequestCheckTokenDto.getAddtionalName() != null && !oauth2RequestCheckTokenDto.getAddtionalName().equals("")) {
            if (oauth2RequestCheckTokenDto.getAddtionalValue() != null && !oauth2RequestCheckTokenDto.getAddtionalValue().equals("")) {
                httpHeaders.set(oauth2RequestCheckTokenDto.getAddtionalName(), oauth2RequestCheckTokenDto.getAddtionalValue());
            }
        }

        String body = "?token="+ oauth2RequestCheckTokenDto.getToken().replaceFirst("Bearer ", "");
        HttpEntity<String> httpEntity = new HttpEntity<>(body, httpHeaders);
        return internalRestTemplate.postForEntity(oauth2RequestCheckTokenDto.getUrl() + body, httpEntity, Object.class);
    }

    protected HttpHeaders codexsTesterInternalBuilderHeaders(RequestDto requestDto, HeadersDto headersDto) {

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

}
