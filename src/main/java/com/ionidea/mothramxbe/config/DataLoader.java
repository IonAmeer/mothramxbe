package com.ionidea.mothramxbe.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ionidea.mothramxbe.security.constants.AppConstants;
import com.ionidea.mothramxbe.security.model.Authority;
import com.ionidea.mothramxbe.security.model.Role;
import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.repository.AuthorityRepository;
import com.ionidea.mothramxbe.security.repository.RoleRepository;
import com.ionidea.mothramxbe.security.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final RoleRepository roleRepository;

    private final AuthorityRepository authorityRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        boolean rolesCreated = seedRoles();
        boolean authoritiesCreated = seedAuthorities();
        boolean roleAuthoritiesCreated = seedRoleAuthorities();
        boolean usersCreated = seedUsers();

        log.info("========== Data Loader Summary ==========");
        log.info("Roles:            {}", rolesCreated ? "CREATED" : "ALREADY EXIST");
        log.info("Authorities:      {}", authoritiesCreated ? "CREATED" : "ALREADY EXIST");
        log.info("RoleDTO-Authorities: {}", roleAuthoritiesCreated ? "ASSIGNED" : "ALREADY ASSIGNED");
        log.info("Users:            {}", usersCreated ? "CREATED — default passwords in use, change them immediately!" : "ALREADY EXIST");
        log.info("==========================================");
    }

    private boolean seedRoles() {
        boolean created = false;
        for (String roleName : AppConstants.DEFAULT_ROLES) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
                log.info("Seeded role: {}", roleName);
                created = true;
            }
        }
        return created;
    }

    private boolean seedAuthorities() {
        boolean created = false;
        for (String authorityName : AppConstants.DEFAULT_AUTHORITIES) {
            if (authorityRepository.findByName(authorityName).isEmpty()) {
                authorityRepository.save(new Authority(null, authorityName));
                log.info("Seeded authority: {}", authorityName);
                created = true;
            }
        }
        return created;
    }

    private boolean seedRoleAuthorities() {
        boolean created = false;
        for (var entry : AppConstants.ROLE_AUTHORITIES.entrySet()) {
            Role role = roleRepository.findByName(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("RoleDTO not found: " + entry.getKey()));

            if (role.getAuthorities().isEmpty()) {
                Set<Authority> authorities = entry.getValue().stream()
                        .map(name -> authorityRepository.findByName(name)
                                .orElseThrow(() -> new RuntimeException("Authority not found: " + name)))
                        .collect(Collectors.toSet());

                role.setAuthorities(authorities);
                roleRepository.save(role);
                log.info("Assigned {} authorities to role: {}", authorities.size(), entry.getKey());
                created = true;
            }
        }
        return created;
    }

    private boolean seedUsers() {
        boolean created = false;
        for (AppConstants.DefaultUser defaultUser : AppConstants.DEFAULT_USERS) {
            if (userRepository.findByEmail(defaultUser.email()).isEmpty()) {

                Role role = roleRepository
                        .findByName(defaultUser.role())
                        .orElseThrow(() -> new RuntimeException("RoleDTO not found: " + defaultUser.role()));

                User user = new User();
                user.setName(defaultUser.name());
                user.setEmail(defaultUser.email());
                user.setPassword(passwordEncoder.encode(defaultUser.password()));
                user.setRoles(Set.of(role));

                userRepository.save(user);
                log.info("Seeded user: {} with role: {}", defaultUser.email(), defaultUser.role());
                created = true;
            }
        }
        return created;
    }

}