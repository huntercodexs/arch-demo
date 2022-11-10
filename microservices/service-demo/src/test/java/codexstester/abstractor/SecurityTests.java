package codexstester.abstractor;

import codexstester.abstractor.dto.Oauth2RequestCheckTokenDto;
import codexstester.abstractor.dto.Oauth2RequestTokenDto;
import codexstester.setup.datasource.DataSourcePropertiesTests;

public abstract class SecurityTests extends DataSourcePropertiesTests {

    /**
     * DO NOT REMOVE THIS METHOD
     * Change this method function before use it
     * */

    public static Oauth2RequestTokenDto codexsTesterSecurityOAuth2Token() {
        Oauth2RequestTokenDto oauth2RequestTokenDto = new Oauth2RequestTokenDto();
        oauth2RequestTokenDto.setUrl(urlOAuth2GetToken);
        oauth2RequestTokenDto.setAuth(authorizationOAuth2GetToken);
        oauth2RequestTokenDto.setGrant(grantTypeOAuth2GetToken);
        oauth2RequestTokenDto.setUser(usernameOAuth2GetToken);
        oauth2RequestTokenDto.setPass(passwordOAuth2GetToken);
        return oauth2RequestTokenDto;
    }

    public static Oauth2RequestCheckTokenDto codexsTesterSecurityOAuth2CheckToken(String token) {
        Oauth2RequestCheckTokenDto oauth2RequestCheckTokenDto = new Oauth2RequestCheckTokenDto();
        oauth2RequestCheckTokenDto.setUrl(urlOAuth2CheckToken);
        oauth2RequestCheckTokenDto.setAuthorization(authorizationOAuth2GetToken);
        oauth2RequestCheckTokenDto.setToken(token);
        return oauth2RequestCheckTokenDto;
    }

}
