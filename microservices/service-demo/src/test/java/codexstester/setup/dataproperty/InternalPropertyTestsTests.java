package codexstester.setup.dataproperty;

public class InternalPropertyTestsTests extends UnitaryPropertyTests {

    protected final String internalUrlBaseTest = internalProp.getProperty("internal.tests.base-url");
    protected final String internalUriBaseTest = internalProp.getProperty("internal.tests.base-uri");
    protected final String internalAuthorizationBasic = internalProp.getProperty("internal.tests.header.authorization-basic");
    protected final String internalAuthorizationBasicInvalid = internalProp.getProperty("internal.tests.header.authorization-basic-invalid");
    protected final String internalAuthorizationBearer = internalProp.getProperty("internal.tests.header.authorization-bearer");
    protected final String internalAuthorizationBearerInvalid = internalProp.getProperty("internal.tests.header.authorization-bearer-invalid");
    protected final String internalAppNameAuthorization = internalProp.getProperty("internal.tests.header.api-key.app-name");
    protected final String internalTokenAuthorization = internalProp.getProperty("internal.tests.header.api-key.token");
    protected final String internalSecretAuthorization = internalProp.getProperty("internal.tests.header.api-key.secret");
    protected final String internalValueAuthorization = internalProp.getProperty("internal.tests.header.api-key.value");
    protected final String internalGenericAuthorization = internalProp.getProperty("internal.tests.header.api-key.generic");
    protected final String internalAdditionalHeaderName = internalProp.getProperty("internal.tests.header.additional-name");
    protected final String internalAdditionalHeaderValue = internalProp.getProperty("internal.tests.header.additional-value");

}
