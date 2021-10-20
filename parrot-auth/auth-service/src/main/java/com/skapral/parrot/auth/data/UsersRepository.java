package com.skapral.parrot.auth.data;

import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<User, Long> {
    User findByLogin(String login);
}
