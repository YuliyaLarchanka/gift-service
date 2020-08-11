package com.epam.esm.web.security;

import com.epam.esm.service.impl.AuthorizedAccountService;
import com.epam.esm.service.security.AuthorisedAccount;
import com.epam.esm.web.dto.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;

@Component
public class JwtFilter extends GenericFilterBean {
    public static final String AUTHORIZATION = "Authorization";

    private final JwtProvider jwtProvider;
    private final AuthorizedAccountService detailsService;
    private final ObjectMapper objectMapper;

    public JwtFilter(JwtProvider jwtProvider, AuthorizedAccountService detailsService, ObjectMapper objectMapper){
        this.jwtProvider = jwtProvider;
        this.detailsService = detailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);
        boolean tokenRequired = checkTokenRequired(servletRequest);

        if (token != null && jwtProvider.validateToken(token)) {
            String userLogin = jwtProvider.getLoginFromToken(token);
            AuthorisedAccount authorisedAccount;
            try {
                authorisedAccount = detailsService.loadUserByUsername(userLogin);
            } catch (UsernameNotFoundException e) {
                writeResponseError401(servletResponse, "No such user");
                return;
            }
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(authorisedAccount, null, authorisedAccount.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else if (tokenRequired) {
            writeResponseError401(servletResponse, "Invalid or empty token");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean checkTokenRequired(ServletRequest servletRequest){
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String path = (request).getRequestURI().substring((request).getContextPath().length());
        String method = request.getMethod();

        if (path.equals("/auth")) {
            return false;
        }

        if (path.startsWith("/tags") && method.equals("GET")) {
            return false;
        }

        if (path.equals("/accounts") && method.equals("POST")) {
            return false;
        }

        if (path.startsWith("/certificates") && method.equals("GET")) {
            return false;
        }

        return true;
    }

    private void writeResponseError401(ServletResponse servletResponse, String message) throws IOException {
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        ErrorDto errorDto = new ErrorDto(401, message);
        objectMapper.writeValue(response.getWriter(), errorDto);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
