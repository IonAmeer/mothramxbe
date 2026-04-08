package com.ionidea.mothramxbe.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ionidea.mothramxbe.security.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
