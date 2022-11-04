package com.huntercodexs.codexstester.tests.integration;

import com.huntercodexs.codexstester.abstractor.InternalRequestTest;
import com.huntercodexs.codexstester.abstractor.dto.HeadersDto;
import com.huntercodexs.codexstester.abstractor.dto.Oauth2RequestTokenDto;
import com.huntercodexs.codexstester.abstractor.dto.Oauth2ResponseTokenDto;
import com.huntercodexs.codexstester.abstractor.dto.RequestDto;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;

import static com.huntercodexs.codexstester.tests.datasource.DataSource.dataSourceAddressRequest;
import static com.huntercodexs.codexstester.tests.datasource.DataSource.dataSourceOAuth2Token;

public class InternalTest extends InternalRequestTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void whenRequestToAddressSearchButRulesServerIsDownTest_RetrieveError_500() throws Exception {
        String invalidAccessToken = "906334ee-b40f-4594-a1c7-a4a5f4123456";/*Fake Token*/
        JSONObject dataRequest = dataSourceAddressRequest();

        HeadersDto headersDto = new HeadersDto();
        headersDto.setAuthorizationBearer(invalidAccessToken);

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("internal.test.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(500);
        requestDto.setExpetecdMessage("{\"errorCode\":150,\"message\":\"Rules Server Contact Failed\"}");

        serverErrorByHttpPost(requestDto, headersDto);
    }

    /**
     * @implNote Change the application.properties before running this test [router.access-code]
     * */
    @Test
    public void whenRequestToAddressSearchUsingInvalidAccessCodeTest_RetrieveUnauthorized_401() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = dataSourceAddressRequest();

        HeadersDto headersDto = new HeadersDto();
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("internal.test.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(401);
        requestDto.setExpetecdMessage("{\"errorCode\":111,\"message\":\"Access Denied\"}");

        unauthorizedByHttpPost(requestDto, headersDto);
    }

    @Test
    public void whenRequestToAddressSearchUsingInvalidAccessTokenTest_RetrieveUnauthorized_401() throws Exception {
        String invalidAccessToken = "906334ee-b40f-4594-a1c7-a4a5f4123456";/*Invalid Token*/
        JSONObject dataRequest = dataSourceAddressRequest();

        HeadersDto headersDto = new HeadersDto();
        headersDto.setAuthorizationBearer(invalidAccessToken);

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("internal.test.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(401);
        requestDto.setExpetecdMessage("{\"errorCode\":111,\"message\":\"Access Denied\"}");

        unauthorizedByHttpPost(requestDto, headersDto);
    }

    @Test
    public void whenRequestToAddressSearchUsingInvalidRulesCodeTest_RetrieveBadRequest_400() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = dataSourceAddressRequest();
        dataRequest.put("rulesCode", "XXX123456");/*Invalid Rules-Code*/

        HeadersDto headersDto = new HeadersDto();
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("internal.test.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(400);
        requestDto.setExpetecdMessage("{\"errorCode\":140,\"message\":\"Rules is not OK\"}");

        badRequestByHttpPost(requestDto, headersDto);
    }

}
