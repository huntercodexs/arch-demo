package com.huntercodexs.archdemo.demo.address.integration;

import com.huntercodexs.archdemo.demo.abstractor.IntegrationAbstractTest;
import com.huntercodexs.archdemo.demo.abstractor.dto.Oauth2RequestTokenDto;
import com.huntercodexs.archdemo.demo.abstractor.dto.Oauth2ResponseTokenDto;
import com.huntercodexs.archdemo.demo.abstractor.dto.RequestPostDto;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;

import static com.huntercodexs.archdemo.demo.address.datasource.AddressDataSourceTest.dataSourceAddressRequestWrongRulesCode;
import static com.huntercodexs.archdemo.demo.address.datasource.AddressDataSourceTest.dataSourceOAuth2Token;

@SpringBootTest
public class AddressIntegrationTest extends IntegrationAbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void whenRequestToAddressSearchUsingInvalidRulesCodeTest_RetrieveUnauthorized_401() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = dataSourceOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = getToken(oauth2RequestTokenDto);
        JSONObject dataRequest = dataSourceAddressRequestWrongRulesCode();

        RequestPostDto requestPostDto = new RequestPostDto();
        requestPostDto.setUri(props.getProperty("integration.test.base-uri"));
        requestPostDto.setId("");
        requestPostDto.setDataRequest(dataRequest.toString());
        requestPostDto.setAuthKey(response.getBody().getAccess_token());
        requestPostDto.setAuthType("Bearer");

        /*Response StatusCode Check - 401*/
        MvcResult result = unauthorizedByHttpPost(requestPostDto);
        /*Response Content Check*/
        assertIntegration("Rules is not OK", result.getResponse().getContentAsString());
    }
}
