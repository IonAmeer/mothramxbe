package com.ionidea.mothramxbe.tasks.controller;

import com.ionidea.mothramxbe.tasks.dto.RefMonthDTO;
import com.ionidea.mothramxbe.tasks.service.RefMonthService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ref-month")
@PreAuthorize("hasRole('DEVELOPER')")
@CrossOrigin("*")
public class RefMonthController {

    private final RefMonthService refMonthService;

    public RefMonthController(RefMonthService refMonthService) {
        this.refMonthService = refMonthService;
    }

    // ✅ GET ALL
    @GetMapping
    public List<RefMonthDTO> getAll() {
        return refMonthService.getAll();
    }

    // ✅ CREATE
    @PostMapping
    public RefMonthDTO create(@RequestBody RefMonthDTO dto) {
        return refMonthService.save(dto);
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public RefMonthDTO getById(@PathVariable Long id) {
        return refMonthService.getById(id);
    }

}