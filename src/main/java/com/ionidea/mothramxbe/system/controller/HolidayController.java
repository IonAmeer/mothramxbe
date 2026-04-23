package com.ionidea.mothramxbe.system.controller;

import com.ionidea.mothramxbe.system.dto.HolidayRequestDTO;
import com.ionidea.mothramxbe.system.dto.HolidayYearDTO;
import com.ionidea.mothramxbe.system.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/holidays")
@RequiredArgsConstructor
@CrossOrigin
public class HolidayController {

    private final HolidayService service;

    // ✅ LOAD YEAR (AUTO CREATE)
    @GetMapping("/{year}")
    public HolidayYearDTO getYear(@PathVariable Integer year) {
        return service.getYear(year);
    }

    // ✅ ADD HOLIDAY
    @PostMapping("/add")
    public String addHoliday(@RequestBody HolidayRequestDTO dto) {
        return service.addHoliday(dto);
    }

    // ✅ DELETE HOLIDAY
    @DeleteMapping("/delete/{id}")
    public String deleteHoliday(@PathVariable Long id) {
        return service.deleteHoliday(id);
    }

    // ✅ FINALIZE YEAR
    @PutMapping("/finalize/{year}")
    public String finalizeYear(@PathVariable Integer year) {
        return service.finalizeYear(year);
    }

}