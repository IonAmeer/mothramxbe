package com.ionidea.mothramxbe.tasks.controller;

import com.ionidea.mothramxbe.tasks.model.RefMonth;
import com.ionidea.mothramxbe.tasks.service.RefMonthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ref-month")
@CrossOrigin("*")
public class RefMonthController {

    @Autowired
    private RefMonthService refMonthService;

    @GetMapping
    public List<RefMonth> getAll() {
        return refMonthService.getAll();
    }

}
