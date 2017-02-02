package com.obs.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ongbo on 2/2/2017.
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Value("${application.config.header.custom-challenge}")
    private String challenge;
    @Value("${application.config.header.realm}")
    private String realm;


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // code trigger when accessing a protected url without authentication

        // based on RFC A server generating a 401 (Unauthorized) response MUST send a
        // WWW-Authenticate header field containing at least one challenge
        response.addHeader(HttpHeaders.WWW_AUTHENTICATE, challenge + " realm=\"" + realm + "\"");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
