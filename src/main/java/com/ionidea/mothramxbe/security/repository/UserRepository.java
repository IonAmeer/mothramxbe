package com.ionidea.mothramxbe.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ionidea.mothramxbe.security.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    @Query("""
                SELECT u
                FROM User u
                JOIN u.userRoles ur
                JOIN ur.role r
                WHERE r.name = 'DEVELOPER'
                AND u.id NOT IN (
                    SELECT lt.developer.id FROM LeadTeam lt
                )
            """)
    List<User> findDevelopersNotInAnyTeam();

}