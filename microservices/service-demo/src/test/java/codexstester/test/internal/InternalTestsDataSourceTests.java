package codexstester.test.internal;

import codexstester.abstractor.dto.HeadersDto;
import codexstester.abstractor.dto.Oauth2RequestTokenDto;
import codexstester.abstractor.dto.Oauth2ResponseTokenDto;
import codexstester.abstractor.dto.RequestDto;
import codexstester.setup.SetupInternalDataSourceTests;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static codexstester.abstractor.SecurityTests.codexsTesterSecurityOAuth2Token;
import static codexstester.setup.datasource.DataSourceTests.dataSourceAddressRequest;

public class InternalTestsDataSourceTests extends SetupInternalDataSourceTests {

    /**
     * @implNote Have sure that the Rules Server is down before run this test
     * */
    @Test
    public void whenRequestToAddressSearchButRulesServerIsDownTest_RetrieveError_500() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = codexsTesterSecurityOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = codexsTesterInternalOAuth2GetToken(oauth2RequestTokenDto);
        JSONObject dataRequest = dataSourceAddressRequest();

        HeadersDto headersDto = new HeadersDto();
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setHttpMethod(HTTP_METHOD_POST);

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(internalProp.getProperty("internal.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedMessage("{\"errorCode\":150,\"message\":\"Rules Server Contact Failed\"}");

        codexsTesterInternal_StatusCode500_RetrieveInternalServerError(headersDto, requestDto);
    }

    /**
     * @implNote Change the application.properties before running this test [router.access-code]
     * */
    @Test
    public void whenRequestToAddressSearchUsingInvalidAccessCodeTest_RetrieveUnauthorized_401() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = codexsTesterSecurityOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = codexsTesterInternalOAuth2GetToken(oauth2RequestTokenDto);
        JSONObject dataRequest = dataSourceAddressRequest();

        HeadersDto headersDto = new HeadersDto();
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setHttpMethod(HTTP_METHOD_POST);

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(internalProp.getProperty("internal.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedMessage("{\"errorCode\":111,\"message\":\"Access Denied\"}");

        codexsTesterInternal_StatusCode401_RetrieveUnauthorized(headersDto, requestDto);
    }

    @Test
    public void whenRequestToAddressSearchUsingInvalidAccessTokenTest_RetrieveUnauthorized_401() throws Exception {
        String invalidAccessToken = "906334ee-b40f-4594-a1c7-a4a5f4123456";/*Invalid Token*/
        JSONObject dataRequest = dataSourceAddressRequest();

        HeadersDto headersDto = new HeadersDto();
        headersDto.setAuthorizationBearer(invalidAccessToken);
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setHttpMethod(HTTP_METHOD_POST);

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(internalProp.getProperty("internal.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedMessage("{\"errorCode\":111,\"message\":\"Access Denied\"}");

        codexsTesterInternal_StatusCode401_RetrieveUnauthorized(headersDto, requestDto);
    }

    @Test
    public void whenRequestToAddressSearchUsingInvalidRulesCodeTest_RetrieveBadRequest_400() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = codexsTesterSecurityOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = codexsTesterInternalOAuth2GetToken(oauth2RequestTokenDto);
        JSONObject dataRequest = dataSourceAddressRequest();
        dataRequest.put("rulesCode", "XXX123456");/*Invalid Rules-Code*/

        HeadersDto headersDto = new HeadersDto();
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setHttpMethod(HTTP_METHOD_POST);

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(internalProp.getProperty("internal.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedMessage("{\"errorCode\":140,\"message\":\"Rules is not OK\"}");

        codexsTesterInternal_StatusCode400_RetrieveBadRequest(headersDto, requestDto);
    }

    @Test
    public void whenRequestToAddressSearchUsingInvalidPostalCodeTest_RetrieveDataNotFound_404() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = codexsTesterSecurityOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = codexsTesterInternalOAuth2GetToken(oauth2RequestTokenDto);
        JSONObject dataRequest = dataSourceAddressRequest();
        dataRequest.put("postalCode", "62090002");/*Postal Code Not Found*/

        HeadersDto headersDto = new HeadersDto();
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setHttpMethod(HTTP_METHOD_POST);

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(internalProp.getProperty("internal.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedMessage("{\"errorCode\":120,\"message\":\"Address not found\"}");

        codexsTesterInternal_StatusCode404_RetrieveNotFound(headersDto, requestDto);
    }

    @Test
    public void whenRequestToAddressSearchUsingCorrectPostalCodeSync_RetrieveOK_200() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = codexsTesterSecurityOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = codexsTesterInternalOAuth2GetToken(oauth2RequestTokenDto);
        JSONObject dataRequest = dataSourceAddressRequest();

        HeadersDto headersDto = new HeadersDto();
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setHttpMethod(HTTP_METHOD_POST);

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(internalProp.getProperty("external.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedMessage(null);

        codexsTesterInternal_StatusCode200_RetrieveOK(headersDto, requestDto);
    }

    @Test
    public void whenRequestToAddressSearchUsingCorrectPostalCodeSync_RetrieveAccepted_202() throws Exception {
        Oauth2RequestTokenDto oauth2RequestTokenDto = codexsTesterSecurityOAuth2Token();
        ResponseEntity<Oauth2ResponseTokenDto> response = codexsTesterInternalOAuth2GetToken(oauth2RequestTokenDto);
        JSONObject dataRequest = dataSourceAddressRequest();
        dataRequest.put("webhook", "http://localhost:33001/huntercodexs/webhook/receptor-fake");/*Async Mode*/

        HeadersDto headersDto = new HeadersDto();
        headersDto.setAuthorizationBearer(response.getBody().getAccess_token());
        headersDto.setContentType("application/json;charset=UTF-8");
        headersDto.setHttpMethod(HTTP_METHOD_POST);

        RequestDto requestDto = new RequestDto();
        requestDto.setUri(internalProp.getProperty("external.tests.base-uri"));
        requestDto.setId("");
        requestDto.setDataRequest(dataRequest.toString());
        requestDto.setExpectedMessage(null);

        codexsTesterInternal_StatusCode202_RetrieveAccepted(headersDto, requestDto);
    }

}
