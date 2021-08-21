package com.sherman.covid19.reservationtool.controllers;

import com.sherman.covid19.reservationtool.managers.BookingRepository;
import com.sherman.covid19.reservationtool.models.Booking;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookingController {
    private final BookingRepository repository;

    public BookingController(BookingRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/bookings")
    List<Booking> all() {
        return repository.findAll();
    }
}
