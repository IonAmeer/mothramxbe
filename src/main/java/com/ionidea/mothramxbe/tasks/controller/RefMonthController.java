package com.ionidea.mothramxbe.tasks.controller;

import com.ionidea.mothramxbe.tasks.dto.RefMonthDTO;
import com.ionidea.mothramxbe.tasks.service.RefMonthService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ref-month")
@PreAuthorize("hasRole('DEVELOPER')")
public class RefMonthController {

    private final RefMonthService refMonthService;

    public RefMonthController(RefMonthService refMonthService) {
        this.refMonthService = refMonthService;
    }

    @GetMapping
    public List<RefMonthDTO> getAll() {
        return refMonthService.getAll();
    }

    @GetMapping("/{id}")
    public RefMonthDTO getById(@PathVariable Long id) {
        return refMonthService.getById(id);
    }

}