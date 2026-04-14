package com.ionidea.mothramxbe.security.repository;

import com.ionidea.mothramxbe.security.model.RoleAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleAuthorityRepository extends JpaRepository<RoleAuthority, Long> {

    List<RoleAuthority> findByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);

}
