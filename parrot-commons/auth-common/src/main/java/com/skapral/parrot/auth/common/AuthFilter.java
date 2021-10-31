package com.skapral.parrot.auth.common;

import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
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

import java.util.Optional;


@Slf4j
public class AuthFilter extends OncePerRequestFilter {
    private final List<UserInfoExtractor> extractors;


    public AuthFilter(UserInfoExtractor... extractors) {
        this.extractors = List.of(extractors);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        extractors
                .map(e -> e.userInfo(request))
                .find(e -> e.isPresent())
                .toJavaOptional()
                .map(Optional::get)
                .ifPresent(userInfo -> {
                    var user = userInfo.getLogin();
                    var role = userInfo.getRole();
                    log.info("USER {} {}", user, role);
                    var userDetails = new User(user, "", Collections.singleton(new RoleAuthority(role)));
                    var authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, "", userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
        filterChain.doFilter(request, response);
    }
}
