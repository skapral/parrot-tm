package com.skapral.parrot.auth.security;

import com.skapral.parrot.auth.common.RoleAuthority;
import com.skapral.parrot.auth.ops.CreateUserIfDoesntExist;
import com.skapral.parrot.auth.queries.UserByLogin;
import com.skapral.parrot.auth.rest.Role;
import io.vavr.collection.List;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.UUID;


public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final JdbcTemplate jdbcTemplate;
    private final RabbitTemplate rabbitTemplate;

    public UserDetailsService(JdbcTemplate jdbcTemplate, RabbitTemplate rabbitTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var op = new CreateUserIfDoesntExist(
                jdbcTemplate,
                rabbitTemplate,
                UUID.randomUUID(),
                login,
                Role.PARROT
        );
        op.execute();

        return new UserByLogin(jdbcTemplate, login)
                .get()
                .map(user ->  new User(user.getId().toString(), "", List.of(new RoleAuthority(user.getRole().name())).asJava()))
                .orElseThrow(() -> new UsernameNotFoundException(login + " not found"));
    }
}
