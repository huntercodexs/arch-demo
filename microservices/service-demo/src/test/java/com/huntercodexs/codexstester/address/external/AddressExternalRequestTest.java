package com.huntercodexs.codexstester.address.external;

import com.huntercodexs.codexstester.abstractor.ExternalRequestTest;
import com.huntercodexs.codexstester.abstractor.dto.HeadersDto;
import com.huntercodexs.codexstester.abstractor.dto.Oauth2RequestTokenDto;
import com.huntercodexs.codexstester.abstractor.dto.Oauth2ResponseTokenDto;
import com.huntercodexs.codexstester.abstractor.dto.RequestDto;
import com.huntercodexs.codexstester.address.datasource.AddressDataSource;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

public class AddressExternalRequestTest extends ExternalRequestTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void whenRequestToAddressSearchUsingInvalidAccessCodeTest_RetrieveUnauthorized_401() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = AddressDataSource.dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = AddressDataSource.dataSourceAddressRequest();

        HeadersDto headersDto = new HeadersDto();
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setAddtionalName("Access-Code");
        headersDto.setAddtionalValue("XYZ-1234555555");/*Invalid Access-Code*/

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("external.test.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(401);
        requestDto.setExpetecdMessage(null);

        assertResultFromRequestByHttpPost(requestDto, headersDto);
    }

    @Test
    public void whenRequestToAddressSearchUsingInvalidAccessTokenTest_RetrieveUnauthorized_401() throws Exception {
        String invalidAccessToken = "906334ee-b40f-4594-a1c7-a4a5f4123456";/*Invalid Access-Token*/
        JSONObject dataRequest = AddressDataSource.dataSourceAddressRequest();

        HeadersDto headersDto = new HeadersDto();
        headersDto.setAuthorizationBearer(invalidAccessToken);
        headersDto.setAddtionalName("Access-Code");
        headersDto.setAddtionalValue("XYZ-123");

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("external.test.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(401);
        requestDto.setExpetecdMessage(null);

        assertResultFromRequestByHttpPost(requestDto, headersDto);
    }

    @Test
    public void whenRequestToAddressSearchUsingInvalidRulesCodeTest_RetrieveBadRequest_400() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = AddressDataSource.dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = AddressDataSource.dataSourceAddressRequest();
        dataRequest.put("rulesCode", "XXX123456");/*Invalid Rules-Code*/

        HeadersDto headersDto = new HeadersDto();
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setAddtionalName("Access-Code");
        headersDto.setAddtionalValue("XYZ-123");

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("external.test.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(400);
        requestDto.setExpetecdMessage("{\"errorCode\":140,\"message\":\"Rules is not OK\"}");

        /*
        * IMPORTANT: The SERVICE-DEMO should be running
        * */
        assertResultFromRequestByHttpPost(requestDto, headersDto);
    }

    @Test
    public void whenRequestToAddressSearchCorrectlyButServiceDemoIsDownTest_RetrieveError_500() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = AddressDataSource.dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = AddressDataSource.dataSourceAddressRequest();

        HeadersDto headersDto = new HeadersDto();
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setAddtionalName("Access-Code");
        headersDto.setAddtionalValue("XYZ-123");

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("external.test.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(500);
        requestDto.setExpetecdMessage(null);

        /*
         * IMPORTANT: The SERVICE-DEMO should be off
         * */
        assertResultFromRequestByHttpPost(requestDto, headersDto);
    }

    @Test
    public void whenRequestToAddressSearchCorrectlySync_RetrieveOK_200() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = AddressDataSource.dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = AddressDataSource.dataSourceAddressRequest();

        HeadersDto headersDto = new HeadersDto();
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setAddtionalName("Access-Code");
        headersDto.setAddtionalValue("XYZ-123");

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("external.test.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(200);
        requestDto.setExpetecdMessage(null);

        /*
         * IMPORTANT: The SERVICE-DEMO should be running
         * */
        assertResultFromRequestByHttpPost(requestDto, headersDto);
    }

    @Test
    public void whenRequestToAddressSearchCorrectlyButDataNotFoundSync_RetrieveNotFound_404() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = AddressDataSource.dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = AddressDataSource.dataSourceAddressRequest();
        dataRequest.put("postalCode", "62090002");/*Postal Code Not Found*/

        HeadersDto headersDto = new HeadersDto();
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setAddtionalName("Access-Code");
        headersDto.setAddtionalValue("XYZ-123");

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("external.test.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(404);
        requestDto.setExpetecdMessage("{\"errorCode\":120,\"message\":\"Address not found\"}");

        /*
         * IMPORTANT: The SERVICE-DEMO should be running
         * */
        assertResultFromRequestByHttpPost(requestDto, headersDto);
    }

    @Test
    public void whenRequestToAddressSearchCorrectlyASync_RetrieveAccepted_202() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = AddressDataSource.dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = AddressDataSource.dataSourceAddressRequest();
        dataRequest.put("webhook", "http://localhost:33001/huntercodexs/webhook/receptor-fake");/*Async Mode*/

        HeadersDto headersDto = new HeadersDto();
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setAddtionalName("Access-Code");
        headersDto.setAddtionalValue("XYZ-123");

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("external.test.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(202);
        requestDto.setExpetecdMessage(null);

        /*
         * IMPORTANT: The SERVICE-DEMO should be running
         * */
        assertResultFromRequestByHttpPost(requestDto, headersDto);
    }

    @Test
    public void whenRequestToAddressSearchCorrectlyAButDataNotFoundSync_RetrieveAccepted_202() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = AddressDataSource.dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = AddressDataSource.dataSourceAddressRequest();
        dataRequest.put("postalCode", "62090002");/*Postal Code Not Found*/
        dataRequest.put("webhook", "http://localhost:33001/huntercodexs/webhook/receptor-fake");/*Async Mode*/

        HeadersDto headersDto = new HeadersDto();
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setAddtionalName("Access-Code");
        headersDto.setAddtionalValue("XYZ-123");

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("external.test.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(202);
        requestDto.setExpetecdMessage(null);

        /*
         * IMPORTANT: The SERVICE-DEMO should be running
         * */
        assertResultFromRequestByHttpPost(requestDto, headersDto);
    }

}
