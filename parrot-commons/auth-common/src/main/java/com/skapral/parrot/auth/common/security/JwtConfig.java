package com.skapral.parrot.auth.common.security;

import com.skapral.parrot.auth.common.AuthEntryPoint;
import com.skapral.parrot.auth.common.AuthFilter;
import com.skapral.parrot.auth.common.jwt.JwtUserInfoExtractor;
import com.skapral.parrot.auth.common.jwt.JwtUtils;
import com.skapral.parrot.auth.common.mock.MockUserInfoExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@PropertySource("classpath:security.properties")
public class JwtConfig {
    private final Boolean testEnv;

    @Autowired
    public JwtConfig(@Value("${test.environment}") Boolean testEnv) {
        this.testEnv = testEnv;
    }

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

    @Bean
    public AuthenticationEntryPoint jwtAuthEntryPoint() {
        return new AuthEntryPoint();
    }

    @Bean
    public AuthFilter jwtAuthFilter(JwtUtils jwtUtils) {
        if (testEnv) {
            return new AuthFilter(
                    new MockUserInfoExtractor(),
                    new JwtUserInfoExtractor(jwtUtils)
            );
        } else {
            return new AuthFilter(
                    new JwtUserInfoExtractor(jwtUtils)
            );
        }
    }
}
