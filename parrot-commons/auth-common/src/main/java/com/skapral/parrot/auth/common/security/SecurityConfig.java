package com.skapral.parrot.auth.common.security;

import com.skapral.parrot.auth.common.jwt.JwtAuthFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public abstract class SecurityConfig extends WebSecurityConfigurerAdapter {
    protected final AuthenticationEntryPoint authEntryPoint;
    protected final JwtAuthFilter authFilter;

    public SecurityConfig(AuthenticationEntryPoint authEntryPoint, JwtAuthFilter authFilter) {
        this.authEntryPoint = authEntryPoint;
        this.authFilter = authFilter;
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
}
