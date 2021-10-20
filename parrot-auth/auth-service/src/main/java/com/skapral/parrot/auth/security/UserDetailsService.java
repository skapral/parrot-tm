package com.skapral.parrot.auth.security;

import com.skapral.parrot.auth.data.UsersRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UsersRepository repository;

    public UserDetailsService(UsersRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var user = repository.findByLogin(login);
        return new User(user.getLogin(), "", Collections.emptyList());
    }
}
