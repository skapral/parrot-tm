package com.skapral.parrot.auth.common.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    @Autowired
    public JwtAuthFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        var jwtOpt = jwtUtils.parseJwt(request);
        jwtOpt
                .filter(jwtUtils::validateJwtToken)
                .ifPresent(jwt -> {
                    log.info("JWT {}", jwt);
                    var userDetails = new User(jwtUtils.getUserNameFromJwtToken(jwt), "", Collections.emptyList());
                    var authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, "", userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
        filterChain.doFilter(request, response);
    }
}
