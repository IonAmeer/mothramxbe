package com.ionidea.mothramxbe.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ionidea.mothramxbe.system.entity.RefMonth;
import com.ionidea.mothramxbe.system.repository.RefMonthRepository;

import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.Locale;

@Slf4j
@Component
@Order(3)
@RequiredArgsConstructor
public class RefMonthDataLoader implements ApplicationRunner {

    private final RefMonthRepository refMonthRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        boolean refMonthsCreated = seedRefMonths();

        log.info("========== Ref Month Data Loader Summary ==========");
        log.info("Ref Months: {}", refMonthsCreated ? "CREATED" : "ALREADY EXIST");
        log.info("====================================================");
    }

    private boolean seedRefMonths() {
        boolean created = false;
        int currentYear = Year.now().getValue();

        for (int year = currentYear; year <= currentYear + 1; year++) {
            for (Month month : Month.values()) {
                String shortName = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

                if (refMonthRepository.findByMonthAndYear(shortName, year).isEmpty()) {
                    RefMonth refMonth = new RefMonth();
                    refMonth.setMonth(shortName);
                    refMonth.setYear(year);
                    refMonth.setLabel(shortName + " " + year);
                    refMonthRepository.save(refMonth);
                    log.info("Seeded ref month: {} {}", shortName, year);
                    created = true;
                }
            }
        }
        return created;
    }

}
