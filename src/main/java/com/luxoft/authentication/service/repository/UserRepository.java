package com.luxoft.authentication.service.repository;

import com.luxoft.authentication.service.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByusername(String name);

    Boolean existsByUsername(String username);
}
