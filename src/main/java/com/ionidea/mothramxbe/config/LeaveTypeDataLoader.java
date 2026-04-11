package com.ionidea.mothramxbe.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ionidea.mothramxbe.tasks.constants.TaskConstants;
import com.ionidea.mothramxbe.tasks.model.LeaveType;
import com.ionidea.mothramxbe.tasks.repository.LeaveTypeRepository;

@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class LeaveTypeDataLoader implements ApplicationRunner {

    private final LeaveTypeRepository leaveTypeRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        boolean leaveTypesCreated = seedLeaveTypes();

        log.info("========== Leave Type Data Loader Summary ==========");
        log.info("Leave Types: {}", leaveTypesCreated ? "CREATED" : "ALREADY EXIST");
        log.info("====================================================");
    }

    private boolean seedLeaveTypes() {
        boolean created = false;
        for (TaskConstants.DefaultLeaveType defaultLeaveType : TaskConstants.DEFAULT_LEAVE_TYPES) {
            if (leaveTypeRepository.findByName(defaultLeaveType.name()).isEmpty()) {
                LeaveType leaveType = new LeaveType();
                leaveType.setName(defaultLeaveType.name());
                leaveType.setDescription(defaultLeaveType.description());
                leaveTypeRepository.save(leaveType);
                log.info("Seeded leave type: {}", defaultLeaveType.name());
                created = true;
            }
        }
        return created;
    }

}
