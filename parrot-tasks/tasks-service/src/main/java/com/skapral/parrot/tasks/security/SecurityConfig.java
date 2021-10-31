package com.skapral.parrot.tasks.security;

import com.skapral.parrot.auth.common.AuthFilter;
import com.skapral.parrot.auth.common.security.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@Import(JwtConfig.class)
@EnableWebSecurity
public class SecurityConfig extends com.skapral.parrot.auth.common.security.SecurityConfig {
    @Autowired
    public SecurityConfig(AuthenticationEntryPoint authEntryPoint, AuthFilter authFilter) {
        super(authEntryPoint, authFilter);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests()
                .anyRequest().authenticated();
    }
}
