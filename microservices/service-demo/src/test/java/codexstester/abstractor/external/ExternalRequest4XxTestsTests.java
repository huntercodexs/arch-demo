package codexstester.abstractor.external;

import codexstester.abstractor.AvailableHttpStatusTests;
import codexstester.abstractor.dto.HeadersDto;
import codexstester.abstractor.dto.RequestDto;

public abstract class ExternalRequest4XxTestsTests extends ExternalRequest5XxTestsTests {

    protected void isOk4xxExternalTest() throws Exception {
        executeExternalTest(new RequestDto(), new HeadersDto());
        System.out.println("isOk4xxExternalTest is done");
    }

    /**
     * STATUS CODE 400 (BAD-REQUEST) TESTS
     * */

    protected void codexsTesterExternal_StatusCode400_RetrieveBadRequest(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.BAD_REQUEST_400);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 401 (UNAUTHORIZED) TESTS
     * */

    protected void codexsTesterExternal_StatusCode401_RetrieveUnauthorized(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.UNAUTHORIZED_401);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 402 (PAYMENT-REQUIRED) TESTS
     * */

    protected void codexsTesterExternal_StatusCode402_RetrievePaymentRequired(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.PAYMENT_REQUIRED_EXPERIMENTAL_402);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 403 (FORBIDDEN) TESTS
     * */

    protected void codexsTesterExternal_StatusCode403_RetrieveForbidden(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.FORBIDDEN_403);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 404 (NOT-FOUND) TESTS
     * */

    protected void codexsTesterExternal_StatusCode404_RetrieveNotFound(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.NOT_FOUND_404);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 405 (METHOD-NOT-ALLOWED) TESTS
     * */

    protected void codexsTesterExternal_StatusCode405_RetrieveMethodNotAllowed(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.METHOD_NOT_ALLOWED_405);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 406 (NOT-ACCEPTABLE) TESTS
     * */

    protected void codexsTesterExternal_StatusCode406_RetrieveNotAcceptable(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.NOT_ACCEPTABLE_406);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 407 (PROXY-AUTHENTICATION-REQUIRED) TESTS
     * */

    protected void codexsTesterExternal_StatusCode407_RetrieveProxyAuthenticationRequired(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.PROXY_AUTHENTICATION_REQUIRED_407);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 408 (REQUEST-TIMEOUT) TESTS
     * */

    protected void codexsTesterExternal_StatusCode408_RetrieveRequestTimeout(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.REQUEST_TIMEOUT_408);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 409 (CONFLICT) TESTS
     * */

    protected void codexsTesterExternal_StatusCode409_RetrieveConflict(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.CONFLICT_409);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 410 (GONE) TESTS
     * */

    protected void codexsTesterExternal_StatusCode410_RetrieveGone(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.GONE_410);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 411 (LENGTH_REQUIRED_411) TESTS
     * */

    protected void codexsTesterExternal_StatusCode411_RetrieveLengthRequired(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.LENGTH_REQUIRED_411);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 412 (PRECONDITION_FAILED_412) TESTS
     * */

    protected void codexsTesterExternal_StatusCode412_RetrievePreconditionFailed(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.PRECONDITION_FAILED_412);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 413 (PAYLOAD_TOO_LARGE_413) TESTS
     * */

    protected void codexsTesterExternal_StatusCode413_RetrievePayloadTooLarge(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.PAYLOAD_TOO_LARGE_413);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 414 (URI_TOO_LONG_414) TESTS
     * */

    protected void codexsTesterExternal_StatusCode414_RetrieveUriTooLong(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.URI_TOO_LONG_414);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 415 (UNSUPPORTED_MEDIA_TYPE_415) TESTS
     * */

    protected void codexsTesterExternal_StatusCode415_RetrieveUnsupportedMediaType(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.UNSUPPORTED_MEDIA_TYPE_415);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 416 (REQUESTED_RANGE_NOT_SATISFIABLE_416) TESTS
     * */

    protected void codexsTesterExternal_StatusCode416_RetrieveRequestedRangeNotSatisfiable(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.REQUESTED_RANGE_NOT_SATISFIABLE_416);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 417 (EXPECTATION_FAILED_417) TESTS
     * */

    protected void codexsTesterExternal_StatusCode417_RetrieveExpectationFailed(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.EXPECTATION_FAILED_417);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 418 (IM_A_TEAPOT_418) TESTS
     * */

    protected void codexsTesterExternal_StatusCode418_RetrieveImATeapotLengthRequired(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.IM_A_TEAPOT_418);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 421 (MISDIRECTED_REQUEST_421) TESTS
     * */

    protected void codexsTesterExternal_StatusCode421_RetrieveMisDirectedRequest(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.MISDIRECTED_REQUEST_421);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 422 (UNPROCESSABLE_ENTITY_WEBDAV_EN_US_422) TESTS
     * */

    protected void codexsTesterExternal_StatusCode422_RetrieveUnprocessableEntity(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.UNPROCESSABLE_ENTITY_WEBDAV_EN_US_422);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 423 (LOCKED_WEBDAV_EN_US_423) TESTS
     * */

    protected void codexsTesterExternal_StatusCode423_RetrieveLocked(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.LOCKED_WEBDAV_EN_US_423);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 424 (FAILED_DEPENDENCY_WEBDAV_EN_US_424) TESTS
     * */

    protected void codexsTesterExternal_StatusCode424_RetrieveFailedDependency(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.FAILED_DEPENDENCY_WEBDAV_EN_US_424);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 425 (TOO_EARLY_425) TESTS
     * */

    protected void codexsTesterExternal_StatusCode425_RetrieveTooEarly(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.TOO_EARLY_425);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 426 (UPGRADE_REQUIRED_426) TESTS
     * */

    protected void codexsTesterExternal_StatusCode426_RetrieveUpgradeRequired(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.UPGRADE_REQUIRED_426);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 428 (PRECONDITION_REQUIRED_428) TESTS
     * */

    protected void codexsTesterExternal_StatusCode428_RetrievePreConditionRequired(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.PRECONDITION_REQUIRED_428);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 429 (TOO_MANY_REQUESTS_429) TESTS
     * */

    protected void codexsTesterExternal_StatusCode429_RetrieveTooManyRequest(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.TOO_MANY_REQUESTS_429);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 431 (REQUEST_HEADER_FIELDS_TOO_LARGE_431) TESTS
     * */

    protected void codexsTesterExternal_StatusCode431_RetrieveRequestHeaderFieldsTooLarge(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.REQUEST_HEADER_FIELDS_TOO_LARGE_431);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 451 (UNAVAILABLE_FOR_LEGAL_REASONS_451) TESTS
     * */

    protected void codexsTesterExternal_StatusCode451_RetrieveUnavailableForLegalReasons(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.UNAVAILABLE_FOR_LEGAL_REASONS_451);
        executeExternalTest(requestDto, headersDto);
    }
}
