package codexstester.abstractor.external;

import codexstester.abstractor.AvailableHttpStatusTests;
import codexstester.abstractor.dto.HeadersDto;
import codexstester.abstractor.dto.RequestDto;

public abstract class ExternalRequest3XxTestsTests extends ExternalRequest4XxTestsTests {

    protected void isOk3xxExternalTest() throws Exception {
        executeExternalTest(new RequestDto(), new HeadersDto());
        System.out.println("isOk3xxExternalTest is done");
    }

    /**
     * STATUS CODE 300 (MULTIPLE_CHOICE_300) TESTS
     * */
    protected void codexsTesterExternal_StatusCode300_RetrieveMultipleChoice(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.MULTIPLE_CHOICE_300);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 301 (MOVED_PERMANENTLY_301) TESTS
     * */
    protected void codexsTesterExternal_StatusCode301_RetrieveMovedPermanently(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.MOVED_PERMANENTLY_301);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 302 (FOUND_302) TESTS
     * */
    protected void codexsTesterExternal_StatusCode302_RetrieveFound(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.FOUND_302);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 303 (SEE_OTHER_303) TESTS
     * */
    protected void codexsTesterExternal_StatusCode303_RetrieveSeeOther(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.SEE_OTHER_303);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 304 (NOT_MODIFIED_304) TESTS
     * */
    protected void codexsTesterExternal_StatusCode304_RetrieveNotModified(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.NOT_MODIFIED_304);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 305 (USE_PROXY_DEPRECATED_305) TESTS
     * */
    protected void codexsTesterExternal_StatusCode305_RetrieveUseProxyDeprecated(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.USE_PROXY_DEPRECATED_305);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 306 (UNUSED_DEPRECATED_306) TESTS
     * */
    protected void codexsTesterExternal_StatusCode306_RetrieveUnusedDeprecated(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.UNUSED_DEPRECATED_306);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 307 (TEMPORARY_REDIRECT_307) TESTS
     * */
    protected void codexsTesterExternal_StatusCode307_RetrieveTemporaryRedirect(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.TEMPORARY_REDIRECT_307);
        executeExternalTest(requestDto, headersDto);
    }

    /**
     * STATUS CODE 308 (PERMANENT_REDIRECT_308) TESTS
     * */
    protected void codexsTesterExternal_StatusCode308_RetrievePermanentRedirect(HeadersDto headersDto, RequestDto requestDto) throws Exception {
        requestDto.setExpectedCode(AvailableHttpStatusTests.PERMANENT_REDIRECT_308);
        executeExternalTest(requestDto, headersDto);
    }

}
