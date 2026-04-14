package com.ionidea.mothramxbe.tasks.constants;

import java.util.List;

public final class TaskConstants {

    // ========================
    // LEAVE TYPES
    // ========================
    public static final List<DefaultLeaveType> DEFAULT_LEAVE_TYPES = List.of(
            new DefaultLeaveType("Casual Leave",      "Short-term leave for personal or unforeseen needs"),
            new DefaultLeaveType("Sick Leave",         "Leave due to illness or medical appointments"),
            new DefaultLeaveType("Earned Leave",       "Accrued paid leave based on tenure"),
            new DefaultLeaveType("Maternity Leave",    "Leave for childbirth and postnatal care"),
            new DefaultLeaveType("Paternity Leave",    "Leave for fathers around the birth of a child"),
            new DefaultLeaveType("Bereavement Leave",  "Leave due to the death of a close family member"),
            new DefaultLeaveType("Compensatory Off",   "Time off earned by working on holidays or weekends"),
            new DefaultLeaveType("Leave Without Pay",  "Unpaid leave when paid leave is exhausted"),
            new DefaultLeaveType("Marriage Leave",     "Leave granted for the employee's wedding")
    );

    private TaskConstants() {
        // Prevent instantiation
    }

    // ========================
    // DEFAULT LEAVE TYPES
    // ========================
    public record DefaultLeaveType(String name, String description) {
    }

}
