package codexstester.setup.dataproperty;

import codexstester.abstractor.PropertiesLoader;

public class UnitaryPropertyTests extends PropertiesLoader {

    protected final String unitaryUrlBaseTest = unitaryProp.getProperty("unitary.tests.base-url");
    protected final String unitaryUriBaseTest = unitaryProp.getProperty("unitary.tests.base-uri");
    protected final String unitaryAuthorizationBasic = unitaryProp.getProperty("unitary.tests.header.authorization-basic");
    protected final String unitaryAuthorizationBasicInvalid = unitaryProp.getProperty("unitary.tests.header.authorization-basic-invalid");
    protected final String unitaryAuthorizationBearer = unitaryProp.getProperty("unitary.tests.header.authorization-bearer");
    protected final String unitaryAuthorizationBearerInvalid = unitaryProp.getProperty("unitary.tests.header.authorization-bearer-invalid");
    protected final String unitaryAppNameAuthorization = unitaryProp.getProperty("unitary.tests.header.api-key.app-name");
    protected final String unitaryTokenAuthorization = unitaryProp.getProperty("unitary.tests.header.api-key.token");
    protected final String unitarySecretAuthorization = unitaryProp.getProperty("unitary.tests.header.api-key.secret");
    protected final String unitaryValueAuthorization = unitaryProp.getProperty("unitary.tests.header.api-key.value");
    protected final String unitaryGenericAuthorization = unitaryProp.getProperty("unitary.tests.header.api-key.generic");
    protected final String unitaryAdditionalHeaderName = unitaryProp.getProperty("unitary.tests.header.additional-name");
    protected final String unitaryAdditionalHeaderValue = unitaryProp.getProperty("unitary.tests.header.additional-value");

}
