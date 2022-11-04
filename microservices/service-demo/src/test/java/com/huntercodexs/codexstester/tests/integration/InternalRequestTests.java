package com.huntercodexs.codexstester.tests.integration;

import com.huntercodexs.codexstester.abstractor.dto.HeadersDto;
import com.huntercodexs.codexstester.abstractor.dto.Oauth2RequestTokenDto;
import com.huntercodexs.codexstester.abstractor.dto.Oauth2ResponseTokenDto;
import com.huntercodexs.codexstester.abstractor.dto.RequestDto;
import com.huntercodexs.codexstester.tests.datasource.DataSourceTests;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static com.huntercodexs.codexstester.tests.datasource.DataSourceTests.dataSourceAddressRequest;
import static com.huntercodexs.codexstester.tests.datasource.DataSourceTests.dataSourceOAuth2Token;

public class InternalRequestTests extends com.huntercodexs.codexstester.abstractor.AbstractInternalRequestTests {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    /**
     * @implNote Have sure that the Rules Server is down before run this test
     * */
    @Test
    public void whenRequestToAddressSearchButRulesServerIsDownTest_RetrieveError_500() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = dataSourceAddressRequest();

        HeadersDto headersDto = new HeadersDto();
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("internal.tests.base-uri"));
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
        requestDto.setUri(props.getProperty("internal.tests.base-uri"));
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
        requestDto.setUri(props.getProperty("internal.tests.base-uri"));
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
        requestDto.setUri(props.getProperty("internal.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(400);
        requestDto.setExpetecdMessage("{\"errorCode\":140,\"message\":\"Rules is not OK\"}");

        badRequestByHttpPost(requestDto, headersDto);
    }

    @Test
    public void whenRequestToAddressSearchUsingInvalidPostalCodeTest_RetrieveDataNotFound_404() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = DataSourceTests.dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = DataSourceTests.dataSourceAddressRequest();
        dataRequest.put("postalCode", "62090002");/*Postal Code Not Found*/

        HeadersDto headersDto = new HeadersDto();
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("internal.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(404);
        requestDto.setExpetecdMessage("{\"errorCode\":120,\"message\":\"Address not found\"}");

        notFoundByHttpPost(requestDto, headersDto);
    }

    @Test
    public void whenRequestToAddressSearchUsingInvalidPostalCodeTest_Retrieve_400() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = DataSourceTests.dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = DataSourceTests.dataSourceAddressRequest();
        dataRequest.put("postalCode", "");/*Postal Code Not Exists*/

        HeadersDto headersDto = new HeadersDto();
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("internal.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(400);
        requestDto.setExpetecdMessage("{\"errorCode\":100,\"message\":\"Missing Data [postalCode], please check the request\"}");

        badRequestByHttpPost(requestDto, headersDto);
    }

    @Test
    public void whenRequestToAddressSearchUsingCorrectPostalCodeSync_RetrieveOK_200() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = DataSourceTests.dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = DataSourceTests.dataSourceAddressRequest();

        HeadersDto headersDto = new HeadersDto();
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("external.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(200);
        requestDto.setExpetecdMessage(null);

        okByHttpPost(requestDto, headersDto);
    }

    @Test
    public void whenRequestToAddressSearchUsingCorrectPostalCodeSync_RetrieveAccepted_202() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = DataSourceTests.dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = DataSourceTests.dataSourceAddressRequest();
        dataRequest.put("webhook", "http://localhost:33001/huntercodexs/webhook/receptor-fake");/*Async Mode*/

        HeadersDto headersDto = new HeadersDto();
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("external.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(202);
        requestDto.setExpetecdMessage(null);

        acceptedByHttpPost(requestDto, headersDto);
    }

}
