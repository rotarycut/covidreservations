package com.sherman.covid19.reservationtool.managers;

import com.sherman.covid19.reservationtool.models.Booking;

public interface CustomBookingRepository {
    Booking findBookingByPerson(String personName);
}
