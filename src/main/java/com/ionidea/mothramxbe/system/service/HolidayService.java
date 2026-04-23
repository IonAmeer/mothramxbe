package com.ionidea.mothramxbe.system.service;

import com.ionidea.mothramxbe.exception.BadRequestException;
import com.ionidea.mothramxbe.exception.DuplicateResourceException;
import com.ionidea.mothramxbe.system.dto.HolidayDTO;
import com.ionidea.mothramxbe.system.dto.HolidayRequestDTO;
import com.ionidea.mothramxbe.system.dto.HolidayYearDTO;
import com.ionidea.mothramxbe.system.entity.Holiday;
import com.ionidea.mothramxbe.system.entity.HolidayYear;
import com.ionidea.mothramxbe.system.repository.HolidayRepository;
import com.ionidea.mothramxbe.system.repository.HolidayYearRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayYearRepository holidayYearRepo;

    private final HolidayRepository holidayRepo;

    // ✅ 1. LOAD YEAR (AUTO CREATE)
    public HolidayYearDTO getYear(Integer year) {

        HolidayYear holidayYear = holidayYearRepo.findByYear(year)
                .orElseThrow(() -> new RuntimeException("Year not found"));

        return mapToDTO(holidayYear);
    }

    // ✅ 2. ADD HOLIDAY
    public String addHoliday(HolidayRequestDTO dto) {

        LocalDate date = dto.getHolidayDate();
        String name = dto.getHolidayName();

        int year = date.getYear();

        HolidayYear holidayYear = holidayYearRepo.findByYear(year)
                .orElseThrow(() -> new RuntimeException("Year not found"));

        if (holidayYear.isFinalized()) {
            throw new BadRequestException("Year is finalized. Cannot add holiday");
        }

        if (holidayRepo.findByHolidayDateAndHolidayYear_Year(date, year).isPresent()) {
            throw new DuplicateResourceException("Holiday already exists for this date");
        }

        Holiday holiday = new Holiday();
        holiday.setHolidayName(name);
        holiday.setHolidayDate(date);
        holiday.setHolidayYear(holidayYear);

        holidayRepo.save(holiday);

        return "Holiday added successfully";
    }

    // ✅ 3. DELETE HOLIDAY
    public String deleteHoliday(Long id) {

        Holiday holiday = holidayRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Holiday not found"));

        if (holiday.getHolidayYear().isFinalized()) {
            throw new RuntimeException("Year finalized. Cannot delete");
        }

        holidayRepo.delete(holiday);

        return "Holiday deleted successfully";
    }

    // ✅ 4. FINALIZE YEAR
    public String finalizeYear(Integer year) {

        HolidayYear holidayYear = holidayYearRepo.findByYear(year)
                .orElseThrow(() -> new RuntimeException("Year not found"));

        holidayYear.setFinalized(true);
        holidayYearRepo.save(holidayYear);

        return "Year finalized successfully";
    }

    // ✅ MAPPER
    private HolidayYearDTO mapToDTO(HolidayYear year) {

        List<HolidayDTO> holidays = year.getHolidays() == null ? List.of() :
                year.getHolidays().stream()
                        .map(h -> new HolidayDTO(
                                h.getId(),
                                h.getHolidayName(),
                                h.getHolidayDate()
                        ))
                        .toList();

        return new HolidayYearDTO(
                year.getYear(),
                year.isFinalized(),
                holidays
        );
    }

}