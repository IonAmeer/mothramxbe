package com.ionidea.mothramxbe.security.service;

import com.ionidea.mothramxbe.exception.BadRequestException;
import com.ionidea.mothramxbe.exception.DuplicateResourceException;
import com.ionidea.mothramxbe.exception.ResourceNotFoundException;
import com.ionidea.mothramxbe.security.dto.AuthorityDto;
import com.ionidea.mothramxbe.security.dto.RoleDTO;
import com.ionidea.mothramxbe.security.dto.UserDTO;
import com.ionidea.mothramxbe.security.model.Role;
import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.model.UserRole;
import com.ionidea.mothramxbe.security.repository.LeadTeamRepository;
import com.ionidea.mothramxbe.security.repository.RoleRepository;
import com.ionidea.mothramxbe.security.repository.UserRepository;
import com.ionidea.mothramxbe.security.constants.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LeadTeamRepository leadTeamRepository;
    private final PasswordEncoder passwordEncoder;

    private boolean isDeveloper(Set<Role> roles) {
        return roles.stream()
                .anyMatch(role -> role.getName().equals(AppConstants.ROLE_DEVELOPER));
    }

    private boolean isLead(User user) {
        return user.getUserRoles() != null &&
                user.getUserRoles().stream()
                        .anyMatch(ur -> ur.getRole().getName().equals(AppConstants.ROLE_LEAD));
    }

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

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<UserDTO> getLeads() {
        return userRepository.findAll()
                .stream()
                .filter(this::isLead)
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new DuplicateResourceException("User", "email", dto.getEmail());
        }

        Set<Role> roles = getRolesFromDTO(dto.getRoleIds());
        boolean isDeveloper = isDeveloper(roles);

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        user.getUserRoles().clear();
        assignRoles(user, roles);

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        leadTeamRepository.deleteByDeveloper(user);

        User saved = userRepository.save(user);
        return toResponseDTO(saved);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        leadTeamRepository.deleteByDeveloper(user);
        userRepository.delete(user);
    }

    private UserDTO toResponseDTO(User user) {

        List<RoleDTO> roles = user.getUserRoles().stream()
                .map(ur -> {
                    Role role = ur.getRole();
                    List<AuthorityDto> authorities = role.getRoleAuthorities().stream()
                            .map(ra -> new AuthorityDto(
                                    ra.getAuthority().getId(),
                                    ra.getAuthority().getName()))
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