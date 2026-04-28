package com.ionidea.mothramxbe.config;

import com.ionidea.mothramxbe.system.entity.HolidayYear;
import com.ionidea.mothramxbe.system.repository.HolidayYearRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.Year;

@Configuration
@RequiredArgsConstructor
public class HolidayDataLoader implements CommandLineRunner {

    private final HolidayYearRepository holidayYearRepo;

    @Override
    public void run(String... args) {

        int currentYear = Year.now().getValue();

        holidayYearRepo.findByYear(currentYear)
                .orElseGet(() -> {
                    HolidayYear year = new HolidayYear();
                    year.setYear(currentYear);
                    year.setFinalized(false);

                    return holidayYearRepo.save(year);
                });

        System.out.println("HolidayYear initialized for: " + currentYear);
    }

}
