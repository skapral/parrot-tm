package com.skapral.parrot.auth.security;

import com.skapral.parrot.auth.common.RoleAuthority;
import com.skapral.parrot.auth.queries.UserByLogin;
import io.vavr.collection.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final JdbcTemplate jdbcTemplate;

    public UserDetailsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return new UserByLogin(jdbcTemplate, login)
                .get()
                .map(user ->  new User(user.getId().toString(), "", List.of(new RoleAuthority(user.getRole())).asJava()))
                .orElseThrow(() -> new UsernameNotFoundException(login + " not found"));
    }
}
