package com.huntercodexs.archdemo.router.config.filter;

import com.huntercodexs.archdemo.router.config.filter.model.AccessControlRouterEntity;
import com.huntercodexs.archdemo.router.config.filter.repository.AccessControlRouterRepository;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class AccessControlRouterConfig extends ZuulFilter {

    @Value("${authorizator.url}")
    private String urlAuthorizator;

    @Value("${authorizator.uri.token}")
    private String uriToken;

    @Value("${authorizator.uri.check-token}")
    private String uriCheckToken;

    @Autowired
    AccessControlRouterRepository accessControlRouterRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HttpServletRequest httpServletRequest;

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

        System.out.println("=========================================================");
        System.out.println("REQUEST CONTEXT");
        System.out.println(RequestContext.getCurrentContext());
        System.out.println(ctx.getRequest());
        System.out.println(ctx.getRequest().getRequestURI());
        System.out.println(ctx.getRequest().getHeaderNames());
        System.out.println(ctx.getRequestQueryParams());
        System.out.println(ctx.getRouteHost());
        System.out.println("=========================================================");

        System.out.println("=========================================================");
        System.out.println("SERVLET REQUESTS");
        System.out.println(httpServletRequest.getHeader("Authorization"));
        System.out.println(httpServletRequest.getHeader("Access-Code"));
        System.out.println("=========================================================");

        /*Get Token*/
        if (ctx.getRequest().getRequestURI().equals(uriToken)) {

            System.out.println("=========================================================");
            System.out.println("GET TOKEN");
            System.out.println(ctx.getRequest().getRequestURI());
            System.out.println("=========================================================");

            /*forwarding to microservices*/
            ctx.setSendZuulResponse(true);
            return null;
        }

        /*Access Control*/
        String accessCode = getAccessCode(ctx.getRequest());
        String token = extractToken(ctx.getRequest());
        String body = "?token="+token;
        AccessControlRouterEntity auth = accessControlRouterRepository.findByAccessCode(accessCode);

        System.out.println("=========================================================");
        System.out.println("AUTH");
        System.out.println(accessCode);
        System.out.println(auth);
        System.out.println("=========================================================");

        if (auth == null) {
            JSONObject response = setErrorResponse("invalid access code " + accessCode);

            /*not forwarding to microservices*/
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            ctx.setResponseBody(response.toJSONString());
            ctx.getResponse().setContentType("application/json");

            System.out.println("=========================================================");
            System.out.println("INVALID ACCESS CODE");
            System.out.println(ctx);
            System.out.println("=========================================================");

            return ctx;
        }

        /*Forward to Microservices if authorized*/
        if (!ctx.getRequest().getRequestURI().equals(uriCheckToken)) {

            boolean validToken = checkToken(token, uriCheckToken, auth.getBasicAuth(), body, accessCode);

            if (!validToken) {
                JSONObject response = setErrorResponse("invalid access token " + token);

                /*not forwarding to microservices*/
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
                ctx.setResponseBody(response.toJSONString());
                ctx.getResponse().setContentType("application/json");

                System.out.println("=========================================================");
                System.out.println("INVALID ACCESS TOKEN");
                System.out.println(ctx);
                System.out.println("=========================================================");

                return ctx;
            }

            System.out.println("=========================================================");
            System.out.println(" FORWARDING <<<<<<< << << < < < < ");
            System.out.println(ctx.getRequest().getRequestURI());
            System.out.println("=========================================================");

            /*forwarding to microservices*/
            ctx.setSendZuulResponse(true);
            return null;

        }

        /*Check Token*/
        /*forwarding to microservices*/
        ctx.setSendZuulResponse(true);
        return null;

    }

    private JSONObject setErrorResponse(String msg) {
        JSONObject response = new JSONObject();
        response.appendField("success", false);
        response.appendField("message", msg);
        return response;
    }

    private HttpHeaders httpRequestHeaders(String auth, String accessCode) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Basic " + auth.replaceFirst("Basic ", ""));
        httpHeaders.set("Access-Code", accessCode);
        return httpHeaders;
    }

    private String getAccessCode(HttpServletRequest accessCode) {
        System.out.println("=============================================================");
        System.out.println("HEADER");
        System.out.println(accessCode.toString());
        System.out.println(accessCode.getHeader("Access-Code"));
        System.out.println("=============================================================");
        try {
            return accessCode
                    .getHeader("Access-Code")
                    .replaceAll(" ", "");
        } catch (RuntimeException re) {
            System.out.println(re.getMessage());
            return null;
        }
    }

    private String extractToken(HttpServletRequest bearerToken) {
        try {
            return bearerToken
                    .getHeader("Authorization")
                    .replaceFirst("Bearer", "")
                    .replaceAll(" ", "");
        } catch (RuntimeException re) {
            System.out.println(re.getMessage());
            return null;
        }
    }

    private boolean checkToken(String token, String uriCheckToken, String auth, String body, String accessCode) {
        HttpEntity<String> httpEntity = new HttpEntity<>(body, httpRequestHeaders(auth, accessCode));
        System.out.println("RESULT[CHECK-TOKEN]");
        try {
            ResponseEntity<JSONObject> result = restTemplate.postForEntity(urlAuthorizator + uriCheckToken + "?token=" + token, httpEntity, JSONObject.class);
            System.out.println(result);
            return true;
        } catch (RuntimeException re) {
            System.out.println("EXCEPTION[CHECK-TOKEN]: " + re.getMessage());
            return false;
        }
    }
}
