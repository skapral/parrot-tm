package com.skapral.parrot.auth.security;

import com.skapral.parrot.common.data.SpringDataJdbc;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Import({SpringDataJdbc.class})
public class AuthenticationDependencies {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthenticationDependencies(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new com.skapral.parrot.auth.security.UserDetailsService(jdbcTemplate);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Parrot's exclusive password encoder (tm)
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return true;
            }
        };
    }
}