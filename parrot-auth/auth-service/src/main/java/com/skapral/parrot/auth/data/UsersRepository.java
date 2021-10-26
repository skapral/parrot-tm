package com.skapral.parrot.auth.data;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UsersRepository extends CrudRepository<User, UUID> {
    User findByLogin(String login);
}
