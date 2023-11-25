/*
 * BrightInsight CONFIDENTIAL
 * Copyright (c) 2019-2021 BrightInsight, All Rights Reserved.
 * NOTICE: These materials, together with all information, code, and other content contained herein (all of the
 * foregoing, collectively, this “Content”) is, and remains the property of BrightInsight, Inc. (“BrightInsight”), and
 * BrightInsight reserves all rights in and related to this Content. This Content is confidential and proprietary to
 * BrightInsight and may be covered by U.S. and/or foreign registered intellectual property or proprietary rights and/or
 * laws, including without limitation trade secret and copyright laws. Dissemination or reproduction of or access to
 * this Content, in whole or in part, is strictly forbidden unless prior written permission is obtained from
 * BrightInsight. The copyright notice above does not evidence any actual or intended publication or disclosure of this
 * Content, and this Content may be a trade secret of BrightInsight.
 * ANY USE, REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE, OR PUBLIC DISPLAY OF THIS CONTENT OR THROUGH
 * USE OF ANY SOFTWARE THAT IS PART OF THIS CONTENT (REGARDLESS OF WHETHER IN SOURCE OR OBJECT CODE), IN WHOLE OR IN
 * PART, IS STRICTLY PROHIBITED OTHER THAN AS EXPRESSLY AUTHORIZED BY BRIGHTINSIGHT IN WRITING, AND MAY BE IN VIOLATION
 * OF APPLICABLE LAWS AND INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION OF THIS CONTENT AND/OR RELATED INFORMATION
 * DOES NOT CONVEY OR IMPLY ANY RIGHT TO REPRODUCE, DISCLOSE, DISTRIBUTE OR OTHERWISE USE IT, OR TO MANUFACTURE, USE, OR
 * SELL ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 */
package com.cibersecurity.login.security;

import com.cibersecurity.login.constants.CodesError;
import com.cibersecurity.login.error.exception.UserError;
import com.cibersecurity.login.error.exception.UserException;
import com.cibersecurity.login.model.Permission;
import com.cibersecurity.login.model.Role;
import com.cibersecurity.login.repository.RoleRepository;
import com.cibersecurity.login.utils.JWTParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Order(1)
public class JWTAuthorizationTokenFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private static final String USER_ID_CLAIM_NAME = "username";
    private static final String ROLE_ID_CLAIM_NAME = "roleId";

    private static final String[] excludedPaths = {"OPTIONS /auth", "OPTIONS /auth/create", "POST /auth/create", "POST /auth"};

    @Autowired
    private RoleRepository roleRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (containsToken(request)) {
                String jwtToken = request.getHeader(AUTHORIZATION_HEADER).replace(TOKEN_PREFIX, StringUtils.EMPTY);
                Claims claims = JWTParser.decodeJWT(jwtToken);
                SecurityContext context = parseClaims(jwtToken, claims);
                if(isAuthorized(request,context,response)){
                    SecurityContextHolder.setUserContext(context);
                    filterChain.doFilter(request, response);
                }
            } else {
                createUnauthorizedFilter(new UserException(HttpStatus.UNAUTHORIZED, new UserError(CodesError.UNAUTHORIZED.getCode(), CodesError.UNAUTHORIZED.getMessage())), response);
            }
        } catch (JwtException e) {
            createUnauthorizedFilter(new UserException(HttpStatus.UNAUTHORIZED, new UserError(CodesError.UNAUTHORIZED.getCode(), CodesError.UNAUTHORIZED.getMessage())), response);
        }finally {
            SecurityContextHolder.clearContext();
        }
    }


    private boolean isAuthorized(HttpServletRequest request,SecurityContext context, HttpServletResponse response){
        UUID roleId = context.getRoleId();
        boolean authorizedFlag = false;

        if(roleRepository.findById(roleId).isPresent()){
            Role role = roleRepository.findById(roleId).orElseThrow();
            List<Permission> permissions = role.getRolePermission();
            authorizedFlag = searchPermission(permissions,request);
        }else{
            createUnauthorizedFilter(new UserException(HttpStatus.BAD_REQUEST,
                    new UserError(CodesError.NON_EXISTENT_ROLE.getCode(),CodesError.NON_EXISTENT_ROLE.getMessage())),response);
            return false;
        }
        if(!authorizedFlag){
            createUnauthorizedFilter(new UserException(HttpStatus.BAD_REQUEST,
                    new UserError(CodesError.UNAUTHORIZED_REQUEST.getCode(),CodesError.UNAUTHORIZED_REQUEST.getMessage())),response);
        }
        return authorizedFlag;
    }

    private boolean searchPermission(List<Permission> permissions, HttpServletRequest request){
        String currentRequest = request.getMethod() + " " + request.getRequestURI();

        for (Permission permission:permissions) {
            String permissionRequest = permission.getMethod() + " " + permission.getUri();

            if(permissionRequest.equals(currentRequest)){
                return true;
            }else if(permissionRequest.endsWith("*") && currentRequest.startsWith(permissionRequest.replace("*",""))){
                return true;
            }
        }

        return false;
    }

    @SneakyThrows
    private void createUnauthorizedFilter(UserException userDemoException, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();

        UserError userError = userDemoException.getError();

        String message = objectMapper.writeValueAsString(userError);

        response.setStatus(401);
        response.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        response.getWriter().write(message);
        response.getWriter().flush();
    }

    private SecurityContext parseClaims(String jwtToken, Claims claims) {
        String userId = claimKey(claims, USER_ID_CLAIM_NAME);
        String roleId = claimKey(claims, ROLE_ID_CLAIM_NAME);

        SecurityContext context = new SecurityContext();
        try {
            context.setUserId(userId);
            context.setRoleId(UUID.fromString(roleId));
            context.setToken(jwtToken);
        } catch (IllegalArgumentException e) {
            throw new MalformedJwtException("Error parsing jwt");
        }
        return context;
    }

    private String claimKey(Claims claims, String key) {
        String value = (String) claims.get(key);
        return Optional.ofNullable(value).orElseThrow();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if(request.getMethod().equalsIgnoreCase("OPTIONS")){
            return true;
        }
        String methodPlusPath = request.getMethod() + " " + request.getRequestURI();
        return Arrays.stream(excludedPaths).anyMatch(path -> path.equalsIgnoreCase(methodPlusPath));
    }

    private boolean containsToken(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(AUTHORIZATION_HEADER);
        return authenticationHeader != null && authenticationHeader.startsWith(TOKEN_PREFIX);
    }
}
