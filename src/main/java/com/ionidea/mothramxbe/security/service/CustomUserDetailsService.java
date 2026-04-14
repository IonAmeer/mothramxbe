package com.ionidea.mothramxbe.security.service;

import com.ionidea.mothramxbe.security.model.Role;
import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ionidea.mothramxbe.security.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();

        for (UserRole userRole : user.getUserRoles()) {
            Role role = userRole.getRole();
            // Add the role itself (e.g. ROLE_ADMIN)
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

            // Add each authority belonging to this role (e.g. ROLE_CREATE, TASK_READ)
            role.getRoleAuthorities().forEach(ra ->
                    grantedAuthorities.add(new SimpleGrantedAuthority(ra.getAuthority().getName()))
            );
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                grantedAuthorities
        );
    }

}