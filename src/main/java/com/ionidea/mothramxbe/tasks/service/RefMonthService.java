package com.ionidea.mothramxbe.tasks.service;

import com.ionidea.mothramxbe.tasks.dto.RefMonthDTO;
import com.ionidea.mothramxbe.system.entity.RefMonth;
import com.ionidea.mothramxbe.system.repository.RefMonthRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RefMonthService {

    private final RefMonthRepository refMonthRepo;

    public RefMonthService(RefMonthRepository refMonthRepo) {
        this.refMonthRepo = refMonthRepo;
    }

    // ✅ GET ALL
    public List<RefMonthDTO> getAll() {
        return refMonthRepo.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ✅ SAVE
    public RefMonthDTO save(RefMonthDTO dto) {
        RefMonth entity = new RefMonth();

        //entity.setName(dto.getName());
        entity.setMonth(dto.getMonth());
        entity.setYear(dto.getYear());

        return mapToDTO(refMonthRepo.save(entity));
    }

    // ✅ GET BY ID
    public RefMonthDTO getById(Long id) {
        RefMonth rm = refMonthRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("RefMonth not found"));

        return mapToDTO(rm);
    }

    // 🔁 MAPPER
    private RefMonthDTO mapToDTO(RefMonth rm) {
        RefMonthDTO dto = new RefMonthDTO();
        dto.setId(rm.getId());
        //dto.setName(rm.getName());
        dto.setMonth(rm.getMonth());
        dto.setYear(rm.getYear());
        return dto;
    }

}