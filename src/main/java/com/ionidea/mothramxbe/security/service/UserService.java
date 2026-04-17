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

import org.springframework.transaction.annotation.Transactional;

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

        // 🔥 RULE: Developer must have Lead
        if (isDeveloper) {

            if (dto.getLeadId() == null) {
                throw new BadRequestException("Developer must have a Lead assigned");
            }

            User lead = userRepository.findById(dto.getLeadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lead", "id", dto.getLeadId()));

            if (!isLead(lead)) {
                throw new BadRequestException("Assigned user is not a valid Lead");
            }

            user.setLead(lead);

        } else {
            user.setLead(null);
        }

        User saved = userRepository.save(user);
        return toResponseDTO(saved);
    }

    // ✅ GET ALL USERS
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // ✅ GET LEADS
    public List<UserDTO> getLeads() {
        return userRepository.findAll()
                .stream()
                .filter(this::isLead)
                .map(this::toResponseDTO)
                .toList();
    }

    // ✅ UPDATE USER
    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // Email uniqueness
        if (dto.getEmail() != null &&
                userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new DuplicateResourceException("User", "email", dto.getEmail());
        }

        // Update fields
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

        // Handle roles
        boolean isDeveloper;

        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            Set<Role> roles = getRolesFromDTO(dto.getRoleIds());
            user.setRoles(roles);
            isDeveloper = isDeveloper(roles);
        } else {
            isDeveloper = isDeveloper(user.getRoles());
        }

        // ✅ Handle Lead logic
        if (isDeveloper) {

            if (dto.getLeadId() == null) {
                throw new BadRequestException("Developer must have a Lead assigned");
            }

            if (dto.getLeadId().equals(id)) {
                throw new BadRequestException("User cannot be their own lead");
            }

            User lead = userRepository.findById(dto.getLeadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lead", "id", dto.getLeadId()));

            if (!isLead(lead)) {
                throw new BadRequestException("Assigned user is not a valid Lead");
            }

            user.setLead(lead);

        } else {
            user.setLead(null);
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

    // 🔥 DTO CONVERTER (IMPORTANT)
    private UserDTO toResponseDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        dto.setRoles(
                user.getRoles().stream()
                        .map(role -> {
                            Role r = new Role();
                            r.setId(role.getId());
                            r.setName(role.getName());
                            return r;
                        })
                        .collect(java.util.stream.Collectors.toSet())
        );


        dto.setRoleIds(
                user.getRoles().stream()
                        .map(Role::getId)
                        .collect(java.util.stream.Collectors.toSet())
        );

        if (user.getLead() != null) {
            dto.setLeadId(user.getLead().getId());
            dto.setLeadName(user.getLead().getName());
        }


        return dto;
    }
}
