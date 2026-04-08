package com.ionidea.mothramxbe.security.constants;

import java.util.List;
import java.util.Map;

public final class AppConstants {

    // ========================
    // ROLES
    // ========================
    public static final String ROLE_ADMIN = "ADMIN";

    public static final String ROLE_LEAD = "LEAD";

    public static final String ROLE_DEVELOPER = "DEVELOPER";

    public static final List<String> DEFAULT_ROLES = List.of(ROLE_ADMIN, ROLE_LEAD, ROLE_DEVELOPER);

    // ========================
    // AUTHORITIES
    // ========================

    // Admin super-authority (acts as all permissions)
    public static final String AUTH_ADMIN = "AUTH_ADMIN";

    // Role module
    public static final String AUTH_ROLE_CREATE = "ROLE_CREATE";

    public static final String AUTH_ROLE_READ = "ROLE_READ";

    public static final String AUTH_ROLE_UPDATE = "ROLE_UPDATE";

    public static final String AUTH_ROLE_DELETE = "ROLE_DELETE";

    // User module
    public static final String AUTH_USER_CREATE = "USER_CREATE";

    public static final String AUTH_USER_READ = "USER_READ";

    public static final String AUTH_USER_UPDATE = "USER_UPDATE";

    public static final String AUTH_USER_DELETE = "USER_DELETE";

    // Holiday module
    public static final String AUTH_HOLIDAY_CREATE = "HOLIDAY_CREATE";

    public static final String AUTH_HOLIDAY_READ = "HOLIDAY_READ";

    public static final String AUTH_HOLIDAY_UPDATE = "HOLIDAY_UPDATE";

    public static final String AUTH_HOLIDAY_DELETE = "HOLIDAY_DELETE";

    // Task module
    public static final String AUTH_TASK_CREATE = "TASK_CREATE";

    public static final String AUTH_TASK_READ = "TASK_READ";

    public static final String AUTH_TASK_UPDATE = "TASK_UPDATE";

    public static final String AUTH_TASK_DELETE = "TASK_DELETE";

    // Team-tasks module
    public static final String AUTH_TEAM_TASKS_CREATE = "TEAM_TASKS_CREATE";

    public static final String AUTH_TEAM_TASKS_READ = "TEAM_TASKS_READ";

    public static final String AUTH_TEAM_TASKS_UPDATE = "TEAM_TASKS_UPDATE";

    public static final String AUTH_TEAM_TASKS_DELETE = "TEAM_TASKS_DELETE";

    public static final List<String> DEFAULT_AUTHORITIES = List.of(
            AUTH_ADMIN,
            AUTH_ROLE_CREATE, AUTH_ROLE_READ, AUTH_ROLE_UPDATE, AUTH_ROLE_DELETE,
            AUTH_USER_CREATE, AUTH_USER_READ, AUTH_USER_UPDATE, AUTH_USER_DELETE,
            AUTH_HOLIDAY_CREATE, AUTH_HOLIDAY_READ, AUTH_HOLIDAY_UPDATE, AUTH_HOLIDAY_DELETE,
            AUTH_TASK_CREATE, AUTH_TASK_READ, AUTH_TASK_UPDATE, AUTH_TASK_DELETE,
            AUTH_TEAM_TASKS_CREATE, AUTH_TEAM_TASKS_READ, AUTH_TEAM_TASKS_UPDATE, AUTH_TEAM_TASKS_DELETE
    );

    // ========================
    // ROLE → AUTHORITIES MAPPING
    // ========================
    public static final Map<String, List<String>> ROLE_AUTHORITIES = Map.of(
            ROLE_ADMIN, List.of(
                    AUTH_ADMIN
            ),
            ROLE_LEAD, List.of(
                    AUTH_TASK_CREATE, AUTH_TASK_READ, AUTH_TASK_UPDATE, AUTH_TASK_DELETE,
                    AUTH_TEAM_TASKS_CREATE, AUTH_TEAM_TASKS_READ, AUTH_TEAM_TASKS_UPDATE, AUTH_TEAM_TASKS_DELETE
            ),
            ROLE_DEVELOPER, List.of(
                    AUTH_TASK_CREATE, AUTH_TASK_READ, AUTH_TASK_UPDATE, AUTH_TASK_DELETE
            )
    );

    public static final List<DefaultUser> DEFAULT_USERS = List.of(
            new DefaultUser("Admin User", "admin@mothramx.com", "Password@123", ROLE_ADMIN),
            new DefaultUser("Lead User", "lead@mothramx.com", "Password@123", ROLE_LEAD),
            new DefaultUser("Developer User", "developer@mothramx.com", "Password@123", ROLE_DEVELOPER)
    );

    private AppConstants() {
        // Prevent instantiation
    }

    // ========================
    // DEFAULT USERS
    // ========================
    public record DefaultUser(String name,

                              String email,

                              String password,

                              String role) {

    }

}

