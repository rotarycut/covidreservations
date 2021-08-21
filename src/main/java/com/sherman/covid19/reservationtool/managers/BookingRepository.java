package com.sherman.covid19.reservationtool.managers;

import com.sherman.covid19.reservationtool.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {


}
