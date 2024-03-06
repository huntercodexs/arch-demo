package codexstester.setup.datasource;

public class DataSourcePropertiesTests {

    /**
     * DO NOT REMOVE THIS ATTRIBUTES
     * Change these attributes if needed
     * The current localtion is src/test/resources
     * Examples:
     *      external.tests.properties = src/test/resources/external.tests.properties
     *      postalcode/external.tests.properties = src/test/resources/postalcode/external.tests.properties
     * */

    protected static String externalPropertiesFilepath = "address/external.tests.properties";
    protected static String internalPropertiesFilepath = "address/internal.tests.properties";
    protected static String unitaryPropertiesFilepath = "address/unitary.tests.properties";

    /**
     * OAUTH2 SETTINGS
     * Change it as needed
    * */

    protected static final boolean ignoreOAuth2Tests = true;
    protected static String urlOAuth2GetToken = "http://localhost:33001/huntercodexs/arch-demo/service-authorizator/api/rest/oauth/v1/oauth/token";
    protected static String urlOAuth2CheckToken = "http://localhost:32943/huntercodexs/arch-demo/service-authorizator/api/rest/oauth/v1/oauth/check_token";
    protected static String authorizationOAuth2GetToken = "Basic YXJjaF9kZW1vX2NsaWVudF8xOjExMTExMTExLTIyMjItMzMzMy00NDQ0LTU1NTU1NTU1NTU1NQ==";
    protected static String grantTypeOAuth2GetToken = "password";
    protected static String usernameOAuth2GetToken = "OAUTH2DEMO_USER";
    protected static String passwordOAuth2GetToken = "1234567890";

}
