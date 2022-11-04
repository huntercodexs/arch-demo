package com.huntercodexs.codexstester.address.integration;

import com.huntercodexs.codexstester.abstractor.InternalRequestTest;
import com.huntercodexs.codexstester.abstractor.dto.HeadersDto;
import com.huntercodexs.codexstester.abstractor.dto.Oauth2RequestTokenDto;
import com.huntercodexs.codexstester.abstractor.dto.Oauth2ResponseTokenDto;
import com.huntercodexs.codexstester.abstractor.dto.RequestDto;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;

import static com.huntercodexs.codexstester.address.datasource.AddressDataSource.dataSourceAddressRequest;
import static com.huntercodexs.codexstester.address.datasource.AddressDataSource.dataSourceOAuth2Token;

@SpringBootTest
public class AddressInternalTest extends InternalRequestTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void whenRequestToAddressSearchUsingInvalidAccessTokenTest_RetrieveUnauthorized_401() throws Exception {
        String invalidAccessToken = "906334ee-b40f-4594-a1c7-a4a5f4123456";
        JSONObject dataRequest = dataSourceAddressRequest();

        HeadersDto headersDto = new HeadersDto();
        headersDto.setAuthorizationBearer(invalidAccessToken);
        headersDto.setAddtionalName("Access-Code");
        headersDto.setAddtionalValue("XYZ-1234555555");

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("integration.test.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedCode(401);
        requestDto.setExpetecdMessage(null);

        /*Response StatusCode Check - 401*/
        MvcResult result = unauthorizedByHttpPost(requestDto, headersDto);
        System.out.println(">>> result: " + result);
        /*Response Content Check*/
        //assertIntegration("Rules Server Contact Failed", result.getResponse().getContentAsString());
    }

    @Test
    public void whenRequestToAddressSearchUsingInvalidRulesCodeTest_RetrieveUnauthorized_401() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = dataSourceAddressRequest();
        dataRequest.put("rulesCode", "XXX123456");

        HeadersDto headersDto = new HeadersDto();
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setAddtionalName("Access-Code");
        headersDto.setAddtionalValue("XYZ-1234555555");

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(props.getProperty("integration.test.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());

        /*Response StatusCode Check - 401*/
        MvcResult result = unauthorizedByHttpPost(requestDto, headersDto);
        /*Response Content Check*/
        assertIntegration("Rules is not OK", result.getResponse().getContentAsString());
    }
}
