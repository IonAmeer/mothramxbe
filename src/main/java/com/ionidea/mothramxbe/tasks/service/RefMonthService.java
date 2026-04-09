package com.ionidea.mothramxbe.tasks.service;

import com.ionidea.mothramxbe.tasks.model.RefMonth;
import com.ionidea.mothramxbe.tasks.repository.RefMonthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefMonthService {

    @Autowired
    private RefMonthRepository refMonthRepo;

    public List<RefMonth> getAll() {
        return refMonthRepo.findAll();
    }

}
