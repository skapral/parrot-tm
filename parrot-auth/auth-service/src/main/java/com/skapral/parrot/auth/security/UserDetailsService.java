package com.skapral.parrot.auth.security;

import com.skapral.parrot.auth.data.Role;
import com.skapral.parrot.auth.data.UsersRepository;
import io.vavr.collection.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;


public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UsersRepository repository;

    public UserDetailsService(UsersRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return Optional.ofNullable(repository.findByLogin(login))
                .map(user ->  new User(user.getLogin(), "", List.of(new RoleAuthority(user.getRole())).asJava()))
                .orElseGet(() -> {
                    var newUser = new com.skapral.parrot.auth.data.User();
                    newUser.setLogin(login);
                    newUser.setRole(Role.PARROT);
                    repository.save(newUser);
                    return new User(login, "", List.of(new RoleAuthority(newUser.getRole())).asJava());
                });
    }
}
