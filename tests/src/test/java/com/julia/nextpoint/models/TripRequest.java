package com.julia.nextpoint.models;

import java.util.Objects;

public class TripRequest {

    private String from;
    private String to;
    private int price;
    private String departureDate;
    private int availableSeats;

    public TripRequest(String from, String to, int price,
                       String departureDate, int availableSeats) {
        this.from = from;
        this.to = to;
        this.price = price;
        this.departureDate = departureDate;
        this.availableSeats = availableSeats;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getPrice() {
        return price;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TripRequest that = (TripRequest) o;
        return price == that.price && availableSeats == that.availableSeats && Objects.equals(from, that.from) && Objects.equals(to, that.to) && Objects.equals(departureDate, that.departureDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, price, departureDate, availableSeats);
    }

    @Override
    public String toString() {
        return "TripRequest{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", price=" + price +
                ", departureDate='" + departureDate + '\'' +
                ", availableSeats=" + availableSeats +
                '}';
    }
}