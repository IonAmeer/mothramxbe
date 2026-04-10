package com.ionidea.mothramxbe.security.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ionidea.mothramxbe.security.constants.AppConstants;
import com.ionidea.mothramxbe.security.dto.UserDTO;
import com.ionidea.mothramxbe.security.model.Role;
import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.repository.RoleRepository;
import com.ionidea.mothramxbe.security.repository.UserRepository;

import org.springframework.http.HttpStatus;

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
            throw new RuntimeException("At least one role must be assigned");
        }

        Set<Role> roles = new HashSet<>();

        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Role not found with id: " + roleId
                    ));

            roles.add(role);
        }

        return roles;
    }

    // ✅ CREATE USER
    public User createUser(UserDTO dto) {

        // ✅ Email uniqueness check
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email already exists"
            );
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
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Developer must have a Lead assigned"
                );
            }

            User lead = userRepository.findById(dto.getLeadId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Lead not found with id: " + dto.getLeadId()
                    ));

            if (!isLead(lead)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Assigned user is not a valid Lead"
                );
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
    public User updateUser(Long id, UserDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with id: " + id
                ));

        // ✅ Email uniqueness check (exclude current user)
        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Email already exists "
            );
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
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Developer must have a Lead assigned "
                );

            }

            if (dto.getLeadId().equals(id)) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User cannot be their own lead "
                );

            }

            User lead = userRepository.findById(dto.getLeadId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found with id: " + dto.getLeadId()));

            if (!isLead(lead)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Assigned user is not a valid Lead");
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));

        userRepository.delete(user);
    }

}