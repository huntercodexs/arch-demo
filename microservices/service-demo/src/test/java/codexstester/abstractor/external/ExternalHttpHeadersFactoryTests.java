package codexstester.abstractor.external;

import codexstester.abstractor.dto.*;
import codexstester.abstractor.internal.InternalHttpHeadersFactoryTests;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

public abstract class ExternalHttpHeadersFactoryTests extends InternalHttpHeadersFactoryTests {

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc externalMockMvc;

    private static final RestTemplate externalRestTemplate = new RestTemplate();

    protected void setUp() {
        externalMockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected void createBeforeExternalTests(String user_data) throws Exception {
        externalMockMvc.perform(
                MockMvcRequestBuilders
                        .post(externalUrlBaseTest + externalUriBaseTest)
                        .content(user_data)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", externalAuthorizationBasic)
        ).andReturn();
    }

    protected void rollbackExternalTests(String id) throws Exception {
        externalMockMvc.perform(
                MockMvcRequestBuilders
                        .delete(externalUrlBaseTest + externalUriBaseTest +"/"+id)
                        .header("Authorization", externalAuthorizationBasic)
        ).andReturn();
    }

    protected void assertExternalTets(String ref, String text) {
        if (text.contains(ref)) {
            Assert.assertEquals(1, 1);
        } else {
            Assert.assertEquals(1, 0);
        }
    }

    protected HttpHeaders codexsTesterExternalHttpRequestHeaders(boolean invalidAuth) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (invalidAuth) {
            httpHeaders.set("Authorization", externalAuthorizationBasicInvalid);
        } else {
            httpHeaders.set("Authorization", externalAuthorizationBasic);
        }
        return httpHeaders;
    }

    protected HttpComponentsClientHttpRequestFactory codexsTesterExternalHttpClientFactory() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    protected static ResponseEntity<Oauth2ResponseTokenDto> codexsTesterExternalOAuth2GetToken(Oauth2RequestTokenDto oauth2RequestTokenDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Basic " + oauth2RequestTokenDto.getAuth().replaceFirst("Basic ", ""));
        String credentials = "?username="+ oauth2RequestTokenDto.getUser()+"&password="+ oauth2RequestTokenDto.getPass()+"&grant_type="+ oauth2RequestTokenDto.getGrant();
        HttpEntity<String> httpEntity = new HttpEntity<>(credentials, httpHeaders);
        return externalRestTemplate.postForEntity(oauth2RequestTokenDto.getUrl() + credentials, httpEntity, Oauth2ResponseTokenDto.class);
    }

    protected static ResponseEntity<Object> codexsTesterExternalOAuth2CheckToken(Oauth2RequestCheckTokenDto oauth2RequestCheckTokenDto) {
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
        return externalRestTemplate.postForEntity(oauth2RequestCheckTokenDto.getUrl() + body, httpEntity, Object.class);
    }

    protected HttpHeaders codexsTesterExternalBuilderHeaders(RequestDto requestDto, HeadersDto headersDto) {

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

}
