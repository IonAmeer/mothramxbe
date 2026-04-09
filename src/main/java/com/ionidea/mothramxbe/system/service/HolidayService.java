package com.ionidea.mothramxbe.system.service;

import org.springframework.stereotype.Service;

import com.ionidea.mothramxbe.exception.DuplicateResourceException;
import com.ionidea.mothramxbe.exception.ResourceNotFoundException;
import com.ionidea.mothramxbe.system.dto.HolidayRequestDTO;
import com.ionidea.mothramxbe.system.dto.HolidayResponseDTO;
import com.ionidea.mothramxbe.system.entity.Holiday;
import com.ionidea.mothramxbe.system.entity.RefMonth;
import com.ionidea.mothramxbe.system.repository.HolidayRepository;
import com.ionidea.mothramxbe.system.repository.RefMonthRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HolidayService {

    private final HolidayRepository holidayRepository;

    private final RefMonthRepository monthRepository;

    public HolidayService(HolidayRepository holidayRepository,
                          RefMonthRepository monthRepository) {
        this.holidayRepository = holidayRepository;
        this.monthRepository = monthRepository;
    }

    // ✅ ADD
    public HolidayResponseDTO createHoliday(HolidayRequestDTO dto) {

        Optional<Holiday> existing = holidayRepository
                .findByYearAndMonth_IdAndDay(dto.getYear(), dto.getMonthId(), dto.getDay());

        if (existing.isPresent()) {
            throw new DuplicateResourceException("Holiday already exists for this date");
        }

        RefMonth month = monthRepository.findById(dto.getMonthId())
                .orElseThrow(() -> new ResourceNotFoundException("Month", "id", dto.getMonthId()));

        Holiday holiday = new Holiday();
        holiday.setName(dto.getName());
        holiday.setDay(dto.getDay());
        holiday.setYear(dto.getYear());
        holiday.setMonth(month);

        holidayRepository.save(holiday);

        return mapToDTO(holiday);
    }

    // ✅ UPDATE
    public HolidayResponseDTO updateHoliday(Long id, HolidayRequestDTO dto) {

        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday", "id", id));

        Optional<Holiday> existing = holidayRepository
                .findByYearAndMonth_IdAndDay(dto.getYear(), dto.getMonthId(), dto.getDay());

        if (existing.isPresent() && !existing.get().getId().equals(id)) {
            throw new DuplicateResourceException("Holiday already exists for this date");
        }

        RefMonth month = monthRepository.findById(dto.getMonthId())
                .orElseThrow(() -> new ResourceNotFoundException("Month", "id", dto.getMonthId()));

        holiday.setName(dto.getName());
        holiday.setDay(dto.getDay());
        holiday.setYear(dto.getYear());
        holiday.setMonth(month);

        holidayRepository.save(holiday);

        return mapToDTO(holiday);
    }

    // ✅ DELETE
    public void deleteHoliday(Long id) {
        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday", "id", id));

        holidayRepository.delete(holiday);
    }

    // ✅ GET ALL
    public List<HolidayResponseDTO> getAll() {
        return holidayRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ✅ GET BY YEAR
    public List<HolidayResponseDTO> getByYear(int year) {
        return holidayRepository.findByYear(year)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private HolidayResponseDTO mapToDTO(Holiday h) {
        return HolidayResponseDTO.builder()
                .id(h.getId())
                .name(h.getName())
                .day(h.getDay())
                .year(h.getYear())
                .month(h.getMonth().getName())
                .build();
    }

}