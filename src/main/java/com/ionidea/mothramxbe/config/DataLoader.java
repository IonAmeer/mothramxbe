package com.ionidea.mothramxbe.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import com.ionidea.mothramxbe.entity.Authority;
import com.ionidea.mothramxbe.entity.Role;
import com.ionidea.mothramxbe.repository.AuthorityRepository;
import com.ionidea.mothramxbe.repository.RoleRepository;

@Component
public class DataLoader {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    public DataLoader(RoleRepository roleRepository,
                      AuthorityRepository authorityRepository) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    @PostConstruct
    public void loadData() {

        // ✅ Load Roles
        if (roleRepository.count() == 0) {

            Role admin = new Role();
            admin.setName("Admin");

            Role lead = new Role();
            lead.setName("Lead");

            Role developer = new Role();
            developer.setName("Developer");

            roleRepository.save(admin);
            roleRepository.save(lead);
            roleRepository.save(developer);
        }

        // ✅ Load Authorities
        if (authorityRepository.count() == 0) {

            authorityRepository.save(new Authority(null, "CREATE_REPORT"));
            authorityRepository.save(new Authority(null, "VIEW_REPORT"));
            authorityRepository.save(new Authority(null, "EDIT_REPORT"));
            authorityRepository.save(new Authority(null, "APPROVE_REPORT"));
            authorityRepository.save(new Authority(null, "REJECT_REPORT"));
            authorityRepository.save(new Authority(null, "EXPORT_REPORT"));
        }
    }
}