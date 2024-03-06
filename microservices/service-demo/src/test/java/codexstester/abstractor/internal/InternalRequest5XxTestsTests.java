package codexstester.abstractor.internal;

import codexstester.abstractor.AvailableHttpStatusTests;
import codexstester.abstractor.dto.HeadersDto;
import codexstester.abstractor.dto.RequestDto;

public abstract class InternalRequest5XxTestsTests extends AbstractTestsInternalRequestTests {

    protected void isOk5xxInternalTest() throws Exception {
        executeInternalTest(new RequestDto(), new HeadersDto());
        System.out.println("isOk5xxInternalTest is done");
    }

    /**
     * STATUS CODE 500 (INTERNAL_SERVER_ERROR_500) TESTS
     * */

    protected void codexsTesterInternal_StatusCode500_RetrieveInternalServerError(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.INTERNAL_SERVER_ERROR_500);
        executeInternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 501 (NOT_IMPLEMENTED_501) TESTS
     * */

    protected void codexsTesterInternal_StatusCode501_RetrieveNotImplemented(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.NOT_IMPLEMENTED_501);
        executeInternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 502 (BAD_GATEWAY_502) TESTS
     * */

    protected void codexsTesterInternal_StatusCode502_RetrieveBadGateway(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.BAD_GATEWAY_502);
        executeInternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 503 (SERVICE_UNAVAILABLE_503) TESTS
     * */

    protected void codexsTesterInternal_StatusCode503_RetrieveServiceUnavailable(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.SERVICE_UNAVAILABLE_503);
        executeInternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 504 (GATEWAY_TIMEOUT_504) TESTS
     * */

    protected void codexsTesterInternal_StatusCode504_RetrieveGatewayTimeout(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.GATEWAY_TIMEOUT_504);
        executeInternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 505 (HTTP_VERSION_NOT_SUPPORTED_505) TESTS
     * */

    protected void codexsTesterInternal_StatusCode505_RetrieveHttpVersionNotSupported(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.HTTP_VERSION_NOT_SUPPORTED_505);
        executeInternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 506 (VARIANT_ALSO_NEGOTIATES_506) TESTS
     * */

    protected void codexsTesterInternal_StatusCode506_RetrieveVariantAlsoNegotiates(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.VARIANT_ALSO_NEGOTIATES_506);
        executeInternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 507 (INSUFFICIENT_STORAGE_507) TESTS
     * */

    protected void codexsTesterInternal_StatusCode507_RetrieveInsuficientStorage(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.INSUFFICIENT_STORAGE_507);
        executeInternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 508 (LOOP_DETECTED_WEBDAV_EN_US_508) TESTS
     * */

    protected void codexsTesterInternal_StatusCode508_RetrieveLoopDetected(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.LOOP_DETECTED_WEBDAV_EN_US_508);
        executeInternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 510 (NOT_EXTENDED_510) TESTS
     * */

    protected void codexsTesterInternal_StatusCode510_RetrieveNotExtended(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.NOT_EXTENDED_510);
        executeInternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 511 (NETWORK_AUTHENTICATION_REQUIRED_511) TESTS
     * */

    protected void codexsTesterInternal_StatusCode511_RetrieveNetworkAuthenticationRequired(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.NETWORK_AUTHENTICATION_REQUIRED_511);
        executeInternalTest(requestDto, headersDto);
    }

}
