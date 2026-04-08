package com.ionidea.mothramxbe.service;


import org.springframework.stereotype.Service;

import com.ionidea.mothramxbe.dto.RoleRequestDTO;
import com.ionidea.mothramxbe.entity.Authority;
import com.ionidea.mothramxbe.entity.Role;
import com.ionidea.mothramxbe.repository.AuthorityRepository;
import com.ionidea.mothramxbe.repository.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    public RoleService(RoleRepository roleRepository,
                       AuthorityRepository authorityRepository) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    // ✅ CREATE
    public Role createRole(RoleRequestDTO dto) {

        Set<Authority> authorities = new HashSet<>();

        for (Long id : dto.getAuthorityIds()) {
            Authority auth = authorityRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Authority not found"));
            authorities.add(auth);
        }

        Role role = new Role();
        role.setName(dto.getName());
        role.setAuthorities(authorities);

        return roleRepository.save(role);
    }

    // ✅ READ
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // ✅ UPDATE (FIXED)
    public Role updateRole(Long id, RoleRequestDTO dto) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Set<Authority> authorities = new HashSet<>();

        for (Long authId : dto.getAuthorityIds()) {
            Authority auth = authorityRepository.findById(authId)
                    .orElseThrow(() -> new RuntimeException("Authority not found"));
            authorities.add(auth);
        }

        role.setName(dto.getName());
        role.setAuthorities(authorities);

        return roleRepository.save(role);
    }

    // ✅ DELETE (SAFE VERSION)
//    public void deleteRole(Long id) {
//
//        Role role = roleRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Role not found"));
//
//        // 🔥 IMPORTANT: clear relations before delete
//        role.getAuthorities().clear();
//
//        roleRepository.delete(role);
//    }

    public void deleteRole(Long id) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // 🔥 STEP 1: Remove role from all users
        if (role.getUsers() != null) {
            role.getUsers().forEach(user -> user.getRoles().remove(role));
        }

        // 🔥 STEP 2: Clear role-authority mapping
        role.getAuthorities().clear();

        // 🔥 STEP 3: Delete role
        roleRepository.delete(role);
    }
}
