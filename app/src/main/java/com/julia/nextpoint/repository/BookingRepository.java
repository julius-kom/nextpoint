package com.julia.nextpoint.repository;

import com.julia.nextpoint.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}