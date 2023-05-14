package com.blog.blograss.commons.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private JwtTokenProvider tokenProvider;
    public JwtFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(
        ServletRequest servletRequest, 
        ServletResponse servletResponse, 
        FilterChain filterChain
        ) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;

        String requestURI = req.getRequestURI();
        String accessToken = tokenProvider.extractAccessToken(req);

        if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken, req)) {

            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.debug("Successfully saved authentication info '{}' at Security Context, uri: {}", authentication.getName(), requestURI);
        } else {
            logger.debug("Not exist valid Jwt token, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

}