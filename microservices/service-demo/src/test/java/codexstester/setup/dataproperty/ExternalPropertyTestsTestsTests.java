package codexstester.setup.dataproperty;

public class ExternalPropertyTestsTestsTests extends InternalPropertyTestsTests {

    protected final String externalUrlBaseTest = externalProp.getProperty("external.tests.base-url");
    protected final String externalUriBaseTest = externalProp.getProperty("external.tests.base-uri");
    protected final String externalAuthorizationBasic = externalProp.getProperty("external.tests.header.authorization-basic");
    protected final String externalAuthorizationBasicInvalid = externalProp.getProperty("external.tests.header.authorization-basic-invalid");
    protected final String externalAuthorizationBearer = externalProp.getProperty("external.tests.header.authorization-bearer");
    protected final String externalAuthorizationBearerInvalid = externalProp.getProperty("external.tests.header.authorization-bearer-invalid");
    protected final String externalAppNameAuthorization = externalProp.getProperty("external.tests.header.api-key.app-name");
    protected final String externalTokenAuthorization = externalProp.getProperty("external.tests.header.api-key.token");
    protected final String externalSecretAuthorization = externalProp.getProperty("external.tests.header.api-key.secret");
    protected final String externalValueAuthorization = externalProp.getProperty("external.tests.header.api-key.value");
    protected final String externalGenericAuthorization = externalProp.getProperty("external.tests.header.api-key.generic");
    protected final String externalAdditionalHeaderName = externalProp.getProperty("external.tests.header.additional-name");
    protected final String externalAdditionalHeaderValue = externalProp.getProperty("external.tests.header.additional-value");

}
