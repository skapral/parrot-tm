package com.skapral.parrot.auth.security;

import com.skapral.parrot.auth.data.Repositories;
import com.skapral.parrot.auth.data.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Import({Repositories.class})
public class AuthenticationDependencies {
    private final UsersRepository usersRepository;

    @Autowired
    public AuthenticationDependencies(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new com.skapral.parrot.auth.security.UserDetailsService(usersRepository);
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