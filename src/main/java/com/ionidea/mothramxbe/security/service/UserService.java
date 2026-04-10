package com.ionidea.mothramxbe.security.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ionidea.mothramxbe.exception.BadRequestException;
import com.ionidea.mothramxbe.exception.DuplicateResourceException;
import com.ionidea.mothramxbe.exception.ResourceNotFoundException;
import com.ionidea.mothramxbe.security.constants.AppConstants;
import com.ionidea.mothramxbe.security.dto.UserRequestDTO;
import com.ionidea.mothramxbe.security.model.Role;
import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.repository.RoleRepository;
import com.ionidea.mothramxbe.security.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 🔹 Check if Developer
    private boolean isDeveloper(Set<Role> roles) {
        return roles.stream()
                .anyMatch(role -> role.getName().equals(AppConstants.ROLE_DEVELOPER));
    }

    // 🔹 Check if Lead
    private boolean isLead(User user) {
        return user.getRoles() != null &&
                user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals(AppConstants.ROLE_LEAD));
    }

    // 🔹 Get roles from DB
    private Set<Role> getRolesFromDTO(Set<Long> roleIds) {

        if (roleIds == null || roleIds.isEmpty()) {
            throw new BadRequestException("At least one role must be assigned");
        }

        Set<Role> roles = new HashSet<>();

        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new ResourceNotFoundException("RoleDTO", "id", roleId));

            roles.add(role);
        }

        return roles;
    }

    // ✅ CREATE USER
    public User createUser(UserRequestDTO dto) {

        // ✅ Email uniqueness check
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("User", "email", dto.getEmail());
        }

        Set<Role> roles = getRolesFromDTO(dto.getRoleIds());
        boolean isDeveloper = isDeveloper(roles);

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(roles);

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

        return userRepository.save(user);
    }

    // ✅ GET ALL USERS
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ GET LEADS
    public List<User> getLeads() {
        return userRepository.findAll()
                .stream()
                .filter(this::isLead)
                .toList();
    }

    // ✅ UPDATE USER
    public User updateUser(Long id, UserRequestDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // ✅ Email uniqueness check (exclude current user)
        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new DuplicateResourceException("User", "email", dto.getEmail());
        }

        Set<Role> roles = getRolesFromDTO(dto.getRoleIds());
        boolean isDeveloper = isDeveloper(roles);

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRoles(roles);

        // ✅ Optional password update
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // 🔥 RULE: Developer must have Lead
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

        return userRepository.save(user);
    }

    // ✅ DELETE USER
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        userRepository.delete(user);
    }

}