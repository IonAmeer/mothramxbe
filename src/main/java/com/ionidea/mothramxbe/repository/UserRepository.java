package com.ionidea.mothramxbe.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ionidea.mothramxbe.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
    SELECT u FROM User u
    LEFT JOIN FETCH u.roles r
    LEFT JOIN FETCH r.authorities
    WHERE u.email = :email
""")
    Optional<User> findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
}