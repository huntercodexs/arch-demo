package com.huntercodexs.archdemo.router.config.filter;

import com.huntercodexs.archdemo.router.config.filter.model.AccessControlRouterEntity;
import com.huntercodexs.archdemo.router.config.filter.repository.AccessControlRouterRepository;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Objects;

import static com.huntercodexs.archdemo.router.config.setup.RouterMessagesSetup.*;
import static com.huntercodexs.archdemo.router.config.setup.RouterMessagesSetup.SERVICE_TOKEN_UNKNOWN_ERROR;
import static com.huntercodexs.archdemo.router.config.setup.ServiceStatusMessagesSetup.*;

@Slf4j
@Configuration
public class RouterFilter extends ZuulFilter {

    @Value("${authorizator.uri.token}")
    private String uriToken;

    @Value("${authorizator.uri.check-token}")
    private String uriCheckToken;

    @Value("${authorizator.url}")
    private String urlAuthorizator;

    @Value("${eureka.security.login}")
    private String basicAuthRequestCheckServiceStatus;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private AccessControlRouterRepository accessControlRouterRepository;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {

        RequestContext ctx = RequestContext.getCurrentContext();

        /*Check Service Status*/
        if (ctx.getRequest().getRequestURL().toString().contains("/arch-demo-status")) {
            /*forwarding to microservices*/
            ctx.setSendZuulResponse(true);
            return null;
        }

        int statusService = checkServiceStatus(ctx.getRequest().getRequestURL().toString());

        if (statusService != SERVICE_STATUS_OK.getCode()) {
            return invalidServiceStatus(ctx, statusService);
        }

        /*Get Token*/
        if (ctx.getRequest().getRequestURI().equals(uriToken)) {
            /*forwarding to microservices*/
            ctx.setSendZuulResponse(true);
            return null;
        }

        /*Access Control*/
        String accessCode = extractAccessCode(ctx.getRequest());
        String token = extractAcessToken(ctx.getRequest());
        String body = "?token="+token;
        AccessControlRouterEntity auth = accessControlRouterRepository.findByAccessCode(accessCode);

        if (auth == null) {
            return invalidAccessCode(ctx, accessCode);
        }

        /*Forward to Microservices after correctly authenticated/authorized*/
        if (!ctx.getRequest().getRequestURI().equals(uriCheckToken)) {

            int validToken = checkToken(token, uriCheckToken, auth.getBasicAuth(), body, accessCode);

            if (validToken != SERVICE_TOKEN_OK.getCode()) {
                return invalidAccessToken(ctx, token, validToken);
            }

            /*forwarding to microservices*/
            ctx.setSendZuulResponse(true);
            return null;
        }

        /*Check Token*/
        /*forwarding to microservices*/
        ctx.setSendZuulResponse(true);
        return null;

    }

    protected JSONObject setErrorResponse(int code, String msg) {
        JSONObject response = new JSONObject();
        response.appendField("code", code);
        response.appendField("message", msg);
        return response;
    }

    protected HttpHeaders httpRequestHeaders(String auth, String accessCode) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (auth != null) {
            httpHeaders.set("Authorization", "Basic " + auth.replaceFirst("Basic ", ""));
        }
        if (accessCode != null) {
            httpHeaders.set("Access-Code", accessCode);
        }
        return httpHeaders;
    }

    protected String extractAccessCode(HttpServletRequest accessCode) {
        try {
            return accessCode
                    .getHeader("Access-Code")
                    .replaceAll(" ", "");
        } catch (RuntimeException re) {
            log.error("EXCEPTION[EXTRACT-ACCESS-CODE]: " + re.getMessage());
            System.out.println("EXCEPTION[EXTRACT-ACCESS-CODE]: " + re.getMessage());
            return null;
        }
    }

    protected String extractAcessToken(HttpServletRequest bearerToken) {
        try {
            return bearerToken
                    .getHeader("Authorization")
                    .replaceFirst("Bearer", "")
                    .replaceAll(" ", "");
        } catch (RuntimeException re) {
            log.error("EXCEPTION[EXTRACT-ACCESS-TOKEN]" + re.getMessage());
            System.out.println("EXCEPTION[EXTRACT-ACCESS-TOKEN]" + re.getMessage());
            return null;
        }
    }

    public int checkToken(String token, String uriCheckToken, String auth, String body, String accessCode) {
        HttpEntity<String> httpEntity = new HttpEntity<>(body, httpRequestHeaders(auth, accessCode));
        try {
            ResponseEntity<JSONObject> result = restTemplate.postForEntity(urlAuthorizator + uriCheckToken + "?token=" + token, httpEntity, JSONObject.class);
            if (result.getBody() != null) {
                return SERVICE_TOKEN_OK.getCode();
            }
            log.error(SERVICE_TOKEN_ACCESS_TOKEN_ERROR.getMessage());
            return SERVICE_TOKEN_ACCESS_TOKEN_ERROR.getCode();
        } catch (RuntimeException re) {

            log.error("EXCEPTION[CHECK-TOKEN]: " + re.getMessage());
            System.out.println("EXCEPTION[CHECK-TOKEN]: " + re.getMessage());

            if (re.getMessage().contains("400 null")) {
                return SERVICE_TOKEN_ACCESS_TOKEN_ERROR.getCode();
            } else if (re.getMessage().contains("404 null")) {
                return SERVICE_TOKEN_NOT_FOUND_ERROR.getCode();
            } else if (re.getMessage().contains("500 null")) {
                return SERVICE_TOKEN_INTERNAL_ERROR.getCode();
            } else if (re.getMessage().contains("502 null")) {
                return SERVICE_TOKEN_BAD_GATEWAY_ERROR.getCode();
            }
            return SERVICE_TOKEN_UNKNOWN_ERROR.getCode();
        }
    }

    public int checkServiceStatus(String url) {
        String basicAuthRequest = new String(Base64.getEncoder().encode(basicAuthRequestCheckServiceStatus.getBytes()));
        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpRequestHeaders(basicAuthRequest, null));
        try {
            ResponseEntity<String> result = restTemplate.exchange(url+"/arch-demo-status", HttpMethod.GET, httpEntity, String.class);
            if (Objects.equals(result.getBody(), "alive")) {
                return SERVICE_STATUS_OK.getCode();
            }
            log.error(SERVICE_STATUS_BASIC_AUTH_ERROR.getMessage());
            return SERVICE_STATUS_BASIC_AUTH_ERROR.getCode();
        } catch (RuntimeException re) {

            log.error("EXCEPTION[CHECK-STATUS]: " + re.getMessage());
            System.out.println(" EXCEPTION[CHECK-STATUS]: " + re.getMessage());

            if (re.getMessage().contains("400 null")) {
                return SERVICE_STATUS_BAD_REQUEST_ERROR.getCode();
            } else if (re.getMessage().contains("404 null")) {
                return SERVICE_STATUS_NOT_FOUND_ERROR.getCode();
            } else if (re.getMessage().contains("500 null")) {
                return SERVICE_STATUS_INTERNAL_ERROR.getCode();
            } else if (re.getMessage().contains("502 null")) {
                return SERVICE_STATUS_BAD_GATEWAY_ERROR.getCode();
            }
            return SERVICE_STATUS_UNKNOWN_ERROR.getCode();
        }
    }

    public Object invalidServiceStatus(RequestContext ctx, int statusService) {

        JSONObject response = setErrorResponse(
                SERVICE_STATUS_UNKNOWN_ERROR.getCode(),
                SERVICE_STATUS_UNKNOWN_ERROR.getMessage());
        HttpStatus status = SERVICE_STATUS_UNKNOWN_ERROR.getHttpStatus();

        switch (statusService) {
            case 50001:
                response = setErrorResponse(
                        SERVICE_STATUS_BASIC_AUTH_ERROR.getCode(),
                        SERVICE_STATUS_BASIC_AUTH_ERROR.getMessage());
                status = SERVICE_STATUS_BASIC_AUTH_ERROR.getHttpStatus();
                break;
            case 50002:
                response = setErrorResponse(
                        SERVICE_STATUS_BAD_REQUEST_ERROR.getCode(),
                        SERVICE_STATUS_BAD_REQUEST_ERROR.getMessage());
                status = SERVICE_STATUS_BAD_REQUEST_ERROR.getHttpStatus();
                break;
            case 50003:
                response = setErrorResponse(
                        SERVICE_STATUS_NOT_FOUND_ERROR.getCode(),
                        SERVICE_STATUS_NOT_FOUND_ERROR.getMessage());
                status = SERVICE_STATUS_NOT_FOUND_ERROR.getHttpStatus();
                break;
            case 50004:
                response = setErrorResponse(
                        SERVICE_STATUS_INTERNAL_ERROR.getCode(),
                        SERVICE_STATUS_INTERNAL_ERROR.getMessage());
                status = SERVICE_STATUS_INTERNAL_ERROR.getHttpStatus();
                break;
            case 50005:
                response = setErrorResponse(
                        SERVICE_STATUS_BAD_GATEWAY_ERROR.getCode(),
                        SERVICE_STATUS_BAD_GATEWAY_ERROR.getMessage());
                status = SERVICE_STATUS_BAD_GATEWAY_ERROR.getHttpStatus();
                break;
            case 50006:
                response = setErrorResponse(
                        SERVICE_STATUS_UNKNOWN_ERROR.getCode(),
                        SERVICE_STATUS_UNKNOWN_ERROR.getMessage());
                status = SERVICE_STATUS_UNKNOWN_ERROR.getHttpStatus();
                break;

        }

        /*not forwarding to microservices*/
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode((Integer.parseInt(String.valueOf(status))));
        ctx.setResponseBody(response.toJSONString());
        ctx.getResponse().setContentType("application/json");

        return ctx;
    }

    public Object invalidAccessCode(RequestContext ctx, String accessCode) {
        JSONObject response = setErrorResponse(
                SERVICE_TOKEN_ACCESS_CODE_ERROR.getCode(),
                SERVICE_TOKEN_ACCESS_CODE_ERROR.getMessage() + " " + accessCode);

        /*not forwarding to the microservices*/
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        ctx.setResponseBody(response.toJSONString());
        ctx.getResponse().setContentType("application/json");

        return ctx;
    }

    public Object invalidAccessToken(RequestContext ctx, String token, int validToken) {

        JSONObject response = setErrorResponse(
                SERVICE_TOKEN_UNKNOWN_ERROR.getCode(),
                SERVICE_TOKEN_UNKNOWN_ERROR.getMessage());
        HttpStatus status = SERVICE_TOKEN_UNKNOWN_ERROR.getHttpStatus();

        switch (validToken) {
            case 50002:
                response = setErrorResponse(
                        SERVICE_TOKEN_ACCESS_TOKEN_ERROR.getCode(),
                        SERVICE_TOKEN_ACCESS_TOKEN_ERROR.getMessage() + " " + token);
                status = SERVICE_TOKEN_ACCESS_TOKEN_ERROR.getHttpStatus();
                break;
            case 50003:
                response = setErrorResponse(
                        SERVICE_TOKEN_BAD_REQUEST_ERROR.getCode(),
                        SERVICE_TOKEN_BAD_REQUEST_ERROR.getMessage());
                status = SERVICE_TOKEN_BAD_REQUEST_ERROR.getHttpStatus();
                break;
            case 50004:
                response = setErrorResponse(
                        SERVICE_TOKEN_NOT_FOUND_ERROR.getCode(),
                        SERVICE_TOKEN_NOT_FOUND_ERROR.getMessage());
                status = SERVICE_TOKEN_NOT_FOUND_ERROR.getHttpStatus();
                break;
            case 50005:
                response = setErrorResponse(
                        SERVICE_TOKEN_INTERNAL_ERROR.getCode(),
                        SERVICE_TOKEN_INTERNAL_ERROR.getMessage());
                status = SERVICE_TOKEN_INTERNAL_ERROR.getHttpStatus();
                break;
            case 50006:
                response = setErrorResponse(
                        SERVICE_TOKEN_BAD_GATEWAY_ERROR.getCode(),
                        SERVICE_TOKEN_BAD_GATEWAY_ERROR.getMessage());
                status = SERVICE_TOKEN_BAD_GATEWAY_ERROR.getHttpStatus();
                break;
            case 50007:
                response = setErrorResponse(
                        SERVICE_TOKEN_UNKNOWN_ERROR.getCode(),
                        SERVICE_TOKEN_UNKNOWN_ERROR.getMessage());
                status = SERVICE_TOKEN_UNKNOWN_ERROR.getHttpStatus();
                break;

        }

        /*not forwarding to the microservices*/
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode((Integer.parseInt(String.valueOf(status))));
        ctx.setResponseBody(response.toJSONString());
        ctx.getResponse().setContentType("application/json");

        return ctx;
    }

}
