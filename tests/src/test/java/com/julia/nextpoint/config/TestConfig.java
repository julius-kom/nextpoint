package com.julia.nextpoint.config;

public class TestConfig {

    public static final String BASE_URL =
            System.getProperty("base.url", "http://localhost:8080");

    public static final String KEYCLOAK_URL =
            System.getProperty("keycloak.url", "http://localhost:8082");

    public static final String KEYCLOAK_REALM = "nextpoint";
    public static final String KEYCLOAK_CLIENT = "nextpoint-api";

    public static final String ADMIN_USERNAME =
            System.getProperty("admin.username", "admin-user");

    public static final String ADMIN_PASSWORD =
            System.getProperty("admin.password", "admin123");

    public static final String TRAVELER_USERNAME =
            System.getProperty("traveler.username", "traveler-user");

    public static final String TRAVELER_PASSWORD =
            System.getProperty("traveler.password", "traveler123");

    private TestConfig() {
    }
}