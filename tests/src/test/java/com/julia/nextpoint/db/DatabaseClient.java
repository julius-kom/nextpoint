package com.julia.nextpoint.db;

import com.julia.nextpoint.models.BookingResponse;
import com.julia.nextpoint.models.TripResponse;
import io.qameta.allure.Allure;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseClient {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/nextpoint";

    private static final String USER = "nextpoint";
    private static final String PASSWORD = "nextpoint";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public TripResponse getTripById(Long tripId) throws SQLException {

        String sql = """
            SELECT id, departure_city, destination_city, price,
                   departure_date, available_seats
            FROM trip
            WHERE id = ?
            """;

        Allure.addAttachment(
                "SQL query",
                "text/plain",
                sql.replace("?", String.valueOf(tripId))
        );

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, tripId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return new TripResponse(
                            resultSet.getLong("id"),
                            resultSet.getString("departure_city"),
                            resultSet.getString("destination_city"),
                            resultSet.getInt("price"),
                            resultSet.getString("departure_date"),
                            resultSet.getInt("available_seats")
                    );
                }

                return null;
            }
        }
    }

    public BookingResponse getBookingById(Long bookingId) throws SQLException {

        String sql = """
            SELECT id, trip_id, travelers, total_price, status
            FROM booking
            WHERE id = ?
            """;

        Allure.addAttachment(
                "SQL query",
                "text/plain",
                sql.replace("?", String.valueOf(bookingId))
        );

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, bookingId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return new BookingResponse(
                            resultSet.getLong("id"),
                            resultSet.getLong("trip_id"),
                            resultSet.getInt("travelers"),
                            resultSet.getInt("total_price"),
                            resultSet.getString("status")
                    );
                }

                return null;
            }
        }
    }

    public void updateTripDepartureDate(Long tripId, LocalDate departureDate)
            throws SQLException {

        String sql = """
            UPDATE trip
            SET departure_date = ?
            WHERE id = ?
            """;

        String sqlForAllure = sql
                .replaceFirst("\\?", "'" + departureDate + "'")
                .replaceFirst("\\?", String.valueOf(tripId));

        Allure.addAttachment(
                "SQL query",
                "text/plain",
                sqlForAllure
        );

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, departureDate);
            statement.setLong(2, tripId);

            statement.executeUpdate();
        }
    }
}