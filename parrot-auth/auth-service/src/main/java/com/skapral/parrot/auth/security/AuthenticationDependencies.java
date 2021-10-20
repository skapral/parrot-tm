package com.skapral.parrot.auth.security;

import com.skapral.parrot.auth.data.SpringDataJdbc;
import com.skapral.parrot.auth.data.UsersRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Import(SpringDataJdbc.class)
public class AuthenticationDependencies {
    private final UsersRepository usersRepository;

    public AuthenticationDependencies(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new com.skapral.parrot.auth.security.UserDetailsService(usersRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}