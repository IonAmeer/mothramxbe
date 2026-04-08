package com.ionidea.mothramxbe.controller;

import org.springframework.web.bind.annotation.*;

import com.ionidea.mothramxbe.dto.HolidayRequestDTO;
import com.ionidea.mothramxbe.dto.HolidayResponseDTO;
import com.ionidea.mothramxbe.service.HolidayService;

import java.util.List;

@RestController
@RequestMapping("/holidays")
public class HolidayController {

    private final HolidayService service;

    public HolidayController(HolidayService service) {
        this.service = service;
    }

    // ✅ CREATE
    @PostMapping
    public HolidayResponseDTO create(@RequestBody HolidayRequestDTO dto) {
        return service.createHoliday(dto); // ✅ FIXED
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public HolidayResponseDTO update(@PathVariable Long id,
                                     @RequestBody HolidayRequestDTO dto) {
        return service.updateHoliday(id, dto);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteHoliday(id);
        return "Deleted successfully";
    }

    // ✅ GET ALL
    @GetMapping
    public List<HolidayResponseDTO> getAll() {
        return service.getAll();
    }

    // ✅ GET BY YEAR
    @GetMapping("/year/{year}")
    public List<HolidayResponseDTO> getByYear(@PathVariable int year) {
        return service.getByYear(year);
    }
}