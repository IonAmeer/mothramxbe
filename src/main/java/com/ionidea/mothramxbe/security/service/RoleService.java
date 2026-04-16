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

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private final AuthorityRepository authorityRepository;

    @Transactional
    public RoleDTO createRole(RoleDTO dto) {

        Role role = new Role();
        role.setName(dto.getName());

        for (Long authId : dto.getAuthorityIds()) {
            Authority auth = authorityRepository.findById(authId).orElseThrow(() -> new ResourceNotFoundException("Authority", "id", authId));
            RoleAuthority ra = new RoleAuthority();
            ra.setRole(role);
            ra.setAuthority(auth);
            role.getRoleAuthorities().add(ra);
        }

        Role saved = roleRepository.save(role);
        return toResponseDTO(saved);
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public RoleDTO updateRole(Long id, RoleDTO dto) {

        Role role = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));

        role.setName(dto.getName());

        // orphanRemoval = true will DELETE the old RoleAuthority rows
        role.getRoleAuthorities().clear();

        for (Long authId : dto.getAuthorityIds()) {
            Authority auth = authorityRepository.findById(authId).orElseThrow(() -> new ResourceNotFoundException("Authority", "id", authId));
            RoleAuthority ra = new RoleAuthority();
            ra.setRole(role);
            ra.setAuthority(auth);
            role.getRoleAuthorities().add(ra);
        }

        Role saved = roleRepository.save(role);
        return toResponseDTO(saved);
    }

    @Transactional
    public void deleteRole(Long id) {

        Role role = roleRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));

        if (role.getUserRoles() != null) {
            for (UserRole ur : new HashSet<>(role.getUserRoles())) {
                ur.getUser().getUserRoles().remove(ur);
            }
            role.getUserRoles().clear();
        }

        role.getRoleAuthorities().clear();

        roleRepository.delete(role);
    }

    private RoleDTO toResponseDTO(Role role) {
        List<AuthorityDto> authorities = role.getRoleAuthorities()
                .stream()
                .map(ra -> new AuthorityDto(ra.getAuthority().getId(), ra.getAuthority().getName()))
                .toList();
        return new RoleDTO(role.getId(), role.getName(), null, authorities);
    }

}
