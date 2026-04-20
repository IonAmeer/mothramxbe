package com.ionidea.mothramxbe.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ionidea.mothramxbe.exception.BadRequestException;
import com.ionidea.mothramxbe.exception.DuplicateResourceException;
import com.ionidea.mothramxbe.exception.ResourceNotFoundException;
import com.ionidea.mothramxbe.security.constants.AppConstants;
import com.ionidea.mothramxbe.security.dto.AuthorityDto;
import com.ionidea.mothramxbe.security.dto.RoleDTO;
import com.ionidea.mothramxbe.security.dto.UserDTO;
import com.ionidea.mothramxbe.security.model.Role;
import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.model.UserRole;
import com.ionidea.mothramxbe.security.repository.RoleRepository;
import com.ionidea.mothramxbe.security.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    // 🔹 Check if Developer
    private boolean isDeveloper(Set<Role> roles) {
        return roles.stream()
                .anyMatch(role -> role.getName().equals(AppConstants.ROLE_DEVELOPER));
    }

    // 🔹 Check if Lead
    private boolean isLead(User user) {
        return user.getUserRoles() != null &&
                user.getUserRoles().stream()
                        .anyMatch(ur -> ur.getRole().getName().equals(AppConstants.ROLE_LEAD));
    }

    // 🔹 Get roles from DB
    private Set<Role> getRolesFromDTO(Set<Long> roleIds) {

        if (roleIds == null || roleIds.isEmpty()) {
            throw new BadRequestException("At least one role must be assigned");
        }

        Set<Role> roles = new HashSet<>();

        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));

            roles.add(role);
        }

        return roles;
    }

    // 🔹 Assign roles to user via UserRole join entities
    private void assignRoles(User user, Set<Role> roles) {
        for (Role role : roles) {
            UserRole ur = new UserRole();
            ur.setUser(user);
            ur.setRole(role);
            user.getUserRoles().add(ur);
        }
    }

    @Transactional
    public UserDTO createUser(UserDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("User", "email", dto.getEmail());
        }

        Set<Role> roles = getRolesFromDTO(dto.getRoleIds());
        boolean isDeveloper = isDeveloper(roles);

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        assignRoles(user, roles);

        User saved = userRepository.save(user);
        return toResponseDTO(saved);
    }

    // ✅ GET ALL USERS
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // ✅ UPDATE USER
    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // ✅ Email uniqueness check (exclude current user)
        if (dto.getEmail() != null &&
                userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new DuplicateResourceException("User", "email", dto.getEmail());
        }

        // ✅ Update basic fields
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        // ✅ Optional password update
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // ✅ Handle roles ONLY if provided
        Set<Role> roles = null;
        boolean isDeveloper;

        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {

            roles = getRolesFromDTO(dto.getRoleIds());
            isDeveloper = isDeveloper(roles);

            // 🔥 Check if roles actually changed
            Set<Long> existingRoleIds = user.getUserRoles().stream()
                    .map(ur -> ur.getRole().getId())
                    .collect(java.util.stream.Collectors.toSet());

            if (!existingRoleIds.equals(dto.getRoleIds())) {
                user.getUserRoles().clear();
                assignRoles(user, roles);
            }

        } else {
            // keep existing roles
            isDeveloper = isDeveloper(
                    user.getUserRoles().stream()
                            .map(ur -> ur.getRole())
                            .collect(java.util.stream.Collectors.toSet())
            );
        }

        User saved = userRepository.save(user);
        return toResponseDTO(saved);
    }

    // ✅ DELETE USER
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        userRepository.delete(user);
    }

    private UserDTO toResponseDTO(User user) {
        List<RoleDTO> roles = user.getUserRoles().stream()
                .map(ur -> {
                    Role role = ur.getRole();
                    List<AuthorityDto> authorities = role.getRoleAuthorities().stream()
                            .map(ra -> new AuthorityDto(ra.getAuthority().getId(), ra.getAuthority().getName()))
                            .toList();
                    return new RoleDTO(role.getId(), role.getName(), null, authorities);
                })
                .toList();

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                null,
                null,
                roles
        );
    }

}