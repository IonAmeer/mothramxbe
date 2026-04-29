package com.ionidea.mothramxbe.security.repository;

import com.ionidea.mothramxbe.security.model.Role;
import com.ionidea.mothramxbe.security.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    void deleteByRole(Role role);

}
