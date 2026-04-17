package com.ionidea.mothramxbe.security.service;

import com.ionidea.mothramxbe.exception.ResourceNotFoundException;
import com.ionidea.mothramxbe.security.dto.AuthorityDto;
import com.ionidea.mothramxbe.security.dto.RoleDTO;
import com.ionidea.mothramxbe.security.model.Role;
import com.ionidea.mothramxbe.security.model.RoleAuthority;
import com.ionidea.mothramxbe.security.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ionidea.mothramxbe.security.model.Authority;
import com.ionidea.mothramxbe.security.repository.AuthorityRepository;
import com.ionidea.mothramxbe.security.repository.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    // =========================
    // CREATE ROLE
    // =========================
    @Transactional
    public RoleDTO createRole(RoleDTO dto) {

        Role role = new Role();
        role.setName(dto.getName());

        // ✅ SAFE NULL CHECK
        if (dto.getAuthorityIds() != null) {
            for (Long authId : dto.getAuthorityIds()) {
                Authority auth = authorityRepository.findById(authId)
                        .orElseThrow(() -> new ResourceNotFoundException("Authority", "id", authId));

                RoleAuthority ra = new RoleAuthority();
                ra.setRole(role);
                ra.setAuthority(auth);

                role.getRoleAuthorities().add(ra);
            }
        }

        Role saved = roleRepository.save(role);
        return toResponseDTO(saved);
    }

    // =========================
    // GET ALL ROLES
    // =========================
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // =========================
    // UPDATE ROLE (FIXED)
    // =========================
    @Transactional
    public RoleDTO updateRole(Long id, RoleDTO dto) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));

        // ✅ Update name ONLY if provided
        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            role.setName(dto.getName());
        }

        // ✅ Update authorities ONLY if provided
        if (dto.getAuthorityIds() != null) {

            // Step 1: clear existing
            role.getRoleAuthorities().clear();

            // Step 2: force delete in DB (VERY IMPORTANT)
            roleRepository.saveAndFlush(role);

            // Step 3: add new authorities (avoid duplicates)
            for (Long authId : new HashSet<>(dto.getAuthorityIds())) {

                Authority auth = authorityRepository.findById(authId)
                        .orElseThrow(() -> new ResourceNotFoundException("Authority", "id", authId));

                RoleAuthority ra = new RoleAuthority();
                ra.setRole(role);
                ra.setAuthority(auth);

                role.getRoleAuthorities().add(ra);
            }
        }

        Role saved = roleRepository.save(role);
        return toResponseDTO(saved);
    }
    // =========================
    // DELETE ROLE
    // =========================
    @Transactional
    public void deleteRole(Long id) {

        Role role = roleRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));

        // ✅ Remove user-role relations safely
        if (role.getUserRoles() != null) {
            for (UserRole ur : new HashSet<>(role.getUserRoles())) {
                ur.getUser().getUserRoles().remove(ur);
            }
            role.getUserRoles().clear();
        }

        // ✅ Remove role-authority relations
        role.getRoleAuthorities().clear();

        roleRepository.delete(role);
    }

    // =========================
    // DTO MAPPER
    // =========================
    private RoleDTO toResponseDTO(Role role) {

        List<AuthorityDto> authorities = role.getRoleAuthorities()
                .stream()
                .map(ra -> new AuthorityDto(
                        ra.getAuthority().getId(),
                        ra.getAuthority().getName()
                ))
                .toList();

        return new RoleDTO(
                role.getId(),
                role.getName(),
                null,           // request-only field
                authorities     // response field
        );
    }
}