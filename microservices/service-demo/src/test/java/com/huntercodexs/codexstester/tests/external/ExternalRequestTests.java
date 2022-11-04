package com.huntercodexs.codexstester.tests.external;

import com.huntercodexs.codexstester.abstractor.dto.HeadersDto;
import com.huntercodexs.codexstester.abstractor.dto.Oauth2RequestTokenDto;
import com.huntercodexs.codexstester.abstractor.dto.Oauth2ResponseTokenDto;
import com.huntercodexs.codexstester.abstractor.dto.RequestDto;
import com.huntercodexs.codexstester.tests.datasource.DataSourceTests;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

public class ExternalRequestTests extends com.huntercodexs.codexstester.abstractor.AbstractExternalRequestTests {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void whenRequestToAddressSearchUsingInvalidAccessCodeTest_RetrieveUnauthorized_401() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = DataSourceTests.dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = DataSourceTests.dataSourceAddressRequest();

        HeadersDto headersDto = new HeadersDto();
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setAddtionalName("Access-Code");
        headersDto.setAddtionalValue("XYZ-1234555555");/*Invalid Access-Code*/

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("external.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(401);
        requestDto.setExpetecdMessage(null);

        assertResultFromRequestByHttpPost(requestDto, headersDto);
    }

    @Test
    public void whenRequestToAddressSearchUsingInvalidAccessTokenTest_RetrieveUnauthorized_401() throws Exception {
        String invalidAccessToken = "906334ee-b40f-4594-a1c7-a4a5f4123456";/*Invalid Access-Token*/
        JSONObject dataRequest = DataSourceTests.dataSourceAddressRequest();

        HeadersDto headersDto = new HeadersDto();
        headersDto.setAuthorizationBearer(invalidAccessToken);
        headersDto.setAddtionalName("Access-Code");
        headersDto.setAddtionalValue("XYZ-123");

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("external.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(401);
        requestDto.setExpetecdMessage(null);

        assertResultFromRequestByHttpPost(requestDto, headersDto);
    }

    /**
     * @apiNote The SERVICE-DEMO should be running
     * */
    @Test
    public void whenRequestToAddressSearchUsingInvalidRulesCodeTest_RetrieveBadRequest_400() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = DataSourceTests.dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = DataSourceTests.dataSourceAddressRequest();
        dataRequest.put("rulesCode", "XXX123456");/*Invalid Rules-Code*/

        HeadersDto headersDto = new HeadersDto();
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setAddtionalName("Access-Code");
        headersDto.setAddtionalValue("XYZ-123");

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("external.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(400);
        requestDto.setExpetecdMessage("{\"errorCode\":140,\"message\":\"Rules is not OK\"}");

        assertResultFromRequestByHttpPost(requestDto, headersDto);
    }

    /**
     * @apiNote The SERVICE-DEMO should be off
     * */
    @Test
    public void whenRequestToAddressSearchCorrectlyButServiceDemoIsDownTest_RetrieveError_500() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = DataSourceTests.dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = DataSourceTests.dataSourceAddressRequest();

        HeadersDto headersDto = new HeadersDto();
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setAddtionalName("Access-Code");
        headersDto.setAddtionalValue("XYZ-123");

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("external.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(500);
        requestDto.setExpetecdMessage(null);

        assertResultFromRequestByHttpPost(requestDto, headersDto);
    }

    @Test
    public void whenRequestToAddressSearchCorrectlySync_RetrieveOK_200() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = DataSourceTests.dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = DataSourceTests.dataSourceAddressRequest();

        HeadersDto headersDto = new HeadersDto();
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setAddtionalName("Access-Code");
        headersDto.setAddtionalValue("XYZ-123");

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("external.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(200);
        requestDto.setExpetecdMessage(null);

        assertResultFromRequestByHttpPost(requestDto, headersDto);
    }

    @Test
    public void whenRequestToAddressSearchCorrectlyButDataNotFoundSync_RetrieveNotFound_404() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = DataSourceTests.dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = DataSourceTests.dataSourceAddressRequest();
        dataRequest.put("postalCode", "62090002");/*Postal Code Not Found*/

        HeadersDto headersDto = new HeadersDto();
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setAddtionalName("Access-Code");
        headersDto.setAddtionalValue("XYZ-123");

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("external.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(404);
        requestDto.setExpetecdMessage("{\"errorCode\":120,\"message\":\"Address not found\"}");

        assertResultFromRequestByHttpPost(requestDto, headersDto);
    }

    @Test
    public void whenRequestToAddressSearchCorrectlyASync_RetrieveAccepted_202() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = DataSourceTests.dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = DataSourceTests.dataSourceAddressRequest();
        dataRequest.put("webhook", "http://localhost:33001/huntercodexs/webhook/receptor-fake");/*Async Mode*/

        HeadersDto headersDto = new HeadersDto();
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setAddtionalName("Access-Code");
        headersDto.setAddtionalValue("XYZ-123");

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("external.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(202);
        requestDto.setExpetecdMessage(null);

        assertResultFromRequestByHttpPost(requestDto, headersDto);
    }

    @Test
    public void whenRequestToAddressSearchCorrectlyAButDataNotFoundSync_RetrieveAccepted_202() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = DataSourceTests.dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = DataSourceTests.dataSourceAddressRequest();
        dataRequest.put("postalCode", "62090002");/*Postal Code Not Found*/
        dataRequest.put("webhook", "http://localhost:33001/huntercodexs/webhook/receptor-fake");/*Async Mode*/

        HeadersDto headersDto = new HeadersDto();
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setAddtionalName("Access-Code");
        headersDto.setAddtionalValue("XYZ-123");

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("external.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(202);
        requestDto.setExpetecdMessage(null);

        assertResultFromRequestByHttpPost(requestDto, headersDto);
    }

}
