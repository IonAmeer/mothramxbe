package com.ionidea.mothramxbe.config;

import com.ionidea.mothramxbe.tasks.model.LeaveType;
import com.ionidea.mothramxbe.tasks.repository.LeaveTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LeaveTypeDataLoader implements CommandLineRunner {

    @Autowired
    private LeaveTypeRepository repo;

    @Override
    public void run(String... args) {

        if (repo.count() > 0) return;

        LeaveType l1 = new LeaveType();
        l1.setName("Annual");
        l1.setDescription("Annual Leave");

        LeaveType l2 = new LeaveType();
        l2.setName("Sick");
        l2.setDescription("Sick Leave");

        repo.save(l1);
        repo.save(l2);
    }

}
