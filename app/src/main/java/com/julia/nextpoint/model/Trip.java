package com.julia.nextpoint.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "departure_city")
    private String from;

    @NotBlank
    @Column(name = "destination_city")
    private String to;

    @Positive
    private int price;

    @FutureOrPresent
    private LocalDate departureDate;

    @Min(0)
    private Integer availableSeats;

    public Trip() {
    }

    public Trip(Long id, String from, String to, int price,
                LocalDate departureDate, Integer availableSeats) {
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

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

}
