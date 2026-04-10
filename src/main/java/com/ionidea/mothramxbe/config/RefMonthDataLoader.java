package com.ionidea.mothramxbe.config;

import com.ionidea.mothramxbe.system.repository.RefMonthRepository;
//import com.ionidea.mothramxbe.tasks.model.RefMonth;
import com.ionidea.mothramxbe.system.entity.RefMonth;
import com.ionidea.mothramxbe.tasks.repository.TaskRefMonthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RefMonthDataLoader implements CommandLineRunner {

    @Autowired
    private RefMonthRepository repo;

    @Override
    public void run(String... args) {

        if (repo.count() > 0) {
            return;
        }

        String[] months = {
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };

        //Long id = 1L;

        for (int year = 2025; year <= 2026; year++) {
            for (String m : months) {

                RefMonth rm = new RefMonth();
                //rm.setId(id++);
                rm.setMonth(m);
                rm.setYear(year);

                repo.save(rm);
            }
        }
    }

}
