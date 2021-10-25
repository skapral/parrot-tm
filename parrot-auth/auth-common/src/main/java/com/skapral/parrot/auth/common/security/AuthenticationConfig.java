package com.skapral.parrot.auth.common.security;

import com.skapral.parrot.auth.common.jwt.JwtAuthFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public abstract class AuthenticationConfig extends SecurityConfig {
    protected final PasswordEncoder passwordEncoder;
    protected final UserDetailsService userDetailsService;

    public AuthenticationConfig(
            AuthenticationEntryPoint authEntryPoint,
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService,
            JwtAuthFilter authFilter
    ) {
        super(authEntryPoint, authFilter);
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .httpBasic().disable()
                .exceptionHandling().authenticationEntryPoint(authEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}