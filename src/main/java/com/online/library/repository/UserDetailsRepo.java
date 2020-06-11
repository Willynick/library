package com.online.library.repository;

import com.online.library.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailsRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findByGoogleId(String id);
    Optional<User> findByEmail(String email);
}
