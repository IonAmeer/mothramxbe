package com.ionidea.mothramxbe.constants;

import java.util.List;
import java.util.Map;

public final class AppConstants {

    private AppConstants() {
        // Prevent instantiation
    }

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
    public static final String AUTH_CREATE_REPORT = "CREATE_REPORT";
    public static final String AUTH_VIEW_REPORT = "VIEW_REPORT";
    public static final String AUTH_EDIT_REPORT = "EDIT_REPORT";
    public static final String AUTH_APPROVE_REPORT = "APPROVE_REPORT";
    public static final String AUTH_REJECT_REPORT = "REJECT_REPORT";
    public static final String AUTH_EXPORT_REPORT = "EXPORT_REPORT";

    public static final List<String> DEFAULT_AUTHORITIES = List.of(
            AUTH_CREATE_REPORT, AUTH_VIEW_REPORT, AUTH_EDIT_REPORT,
            AUTH_APPROVE_REPORT, AUTH_REJECT_REPORT, AUTH_EXPORT_REPORT
    );

    // ========================
    // ROLE → AUTHORITIES MAPPING
    // ========================
    public static final Map<String, List<String>> ROLE_AUTHORITIES = Map.of(
            ROLE_ADMIN, List.of(
                    AUTH_CREATE_REPORT, AUTH_VIEW_REPORT, AUTH_EDIT_REPORT,
                    AUTH_APPROVE_REPORT, AUTH_REJECT_REPORT, AUTH_EXPORT_REPORT
            ),
            ROLE_LEAD, List.of(
                    AUTH_CREATE_REPORT, AUTH_VIEW_REPORT, AUTH_EDIT_REPORT,
                    AUTH_APPROVE_REPORT, AUTH_REJECT_REPORT
            ),
            ROLE_DEVELOPER, List.of(
                    AUTH_CREATE_REPORT, AUTH_VIEW_REPORT, AUTH_EDIT_REPORT
            )
    );

    // ========================
    // DEFAULT USERS
    // ========================
    public record DefaultUser(String name, String email, String password, String role) {}

    public static final List<DefaultUser> DEFAULT_USERS = List.of(
            new DefaultUser("Admin User", "admin@mothramx.com", "Password@123", ROLE_ADMIN),
            new DefaultUser("Lead User", "lead@mothramx.com", "Password@123", ROLE_LEAD),
            new DefaultUser("Developer User", "developer@mothramx.com", "Password@123", ROLE_DEVELOPER)
    );

}

