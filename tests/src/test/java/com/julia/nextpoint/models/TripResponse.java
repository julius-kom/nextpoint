package com.julia.nextpoint.models;

import java.util.Objects;

public class TripResponse {

    private Long id;
    private String from;
    private String to;
    private int price;
    private String departureDate;
    private int availableSeats;

    public TripResponse() {
    }

    public TripResponse(Long id, String from, String to, int price,
                        String departureDate, int availableSeats) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.price = price;
        this.departureDate = departureDate;
        this.availableSeats = availableSeats;
    }

    public Long getId() {
        return id;
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
        TripResponse that = (TripResponse) o;
        return price == that.price && availableSeats == that.availableSeats
                && Objects.equals(id, that.id) &&
                Objects.equals(from, that.from) &&
                Objects.equals(to, that.to) &&
                Objects.equals(departureDate, that.departureDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, from, to, price, departureDate, availableSeats);
    }

    @Override
    public String toString() {
        return "TripResponse{" +
                "id=" + id +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", price=" + price +
                ", departureDate='" + departureDate + '\'' +
                ", availableSeats=" + availableSeats +
                '}';
    }
}