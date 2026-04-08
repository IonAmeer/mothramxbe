package com.ionidea.mothramxbe.system.controller;

import com.ionidea.mothramxbe.system.dto.HolidayRequestDTO;
import com.ionidea.mothramxbe.system.dto.HolidayResponseDTO;
import com.ionidea.mothramxbe.system.service.HolidayService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/holidays")
public class HolidayController { // all controllers must return response entity

    private final HolidayService service;

    public HolidayController(HolidayService service) {
        this.service = service;
    }

    @PostMapping
    public HolidayResponseDTO create(@RequestBody HolidayRequestDTO dto) {
        return service.createHoliday(dto);
    }

    @PutMapping("/{id}")
    public HolidayResponseDTO update(@PathVariable Long id, @RequestBody HolidayRequestDTO dto) {
        return service.updateHoliday(id, dto);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteHoliday(id);
        return "Deleted successfully";
    }

    @GetMapping
    public List<HolidayResponseDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/year/{year}") // replace with query param
    public List<HolidayResponseDTO> getByYear(@PathVariable int year) {
        return service.getByYear(year);
    }

}