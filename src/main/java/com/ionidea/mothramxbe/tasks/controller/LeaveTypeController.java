package com.ionidea.mothramxbe.tasks.controller;

import com.ionidea.mothramxbe.tasks.model.LeaveType;
import com.ionidea.mothramxbe.tasks.repository.LeaveTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-types")
@CrossOrigin("*")
public class LeaveTypeController {

    @Autowired
    private LeaveTypeRepository repo;

    @GetMapping
    public List<LeaveType> getAll() {
        return repo.findAll();
    }

}