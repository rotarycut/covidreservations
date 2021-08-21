package com.sherman.covid19.reservationtool.models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Booking {
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalDateTime lastUpdate;

    @EmbeddedId
    private BookingPK bookingPK;

    public BookingPK getBookingPK() {
        return bookingPK;
    }

    public void setBookingPK(BookingPK bookingPK) {
        this.bookingPK = bookingPK;
    }

    public Booking(LocalDate date, LocalDateTime lastUpdate, BookingPK bookingPK) {
        this.date = date;
        this.lastUpdate = lastUpdate;
        this.bookingPK = bookingPK;
    }

    public Booking(){}

    @Embeddable
    public static class BookingPK implements Serializable {
        @OneToOne
        private Person person;

        @OneToOne
        private NurseVaccinationCentreTimeslot nurseVaccinationCentreTimeslot;

        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
        }

        public NurseVaccinationCentreTimeslot getNurseVaccinationCentreTimeslot() {
            return nurseVaccinationCentreTimeslot;
        }

        public void setNurseVaccinationCentreTimeslot(NurseVaccinationCentreTimeslot nurseVaccinationCentreTimeslot) {
            this.nurseVaccinationCentreTimeslot = nurseVaccinationCentreTimeslot;
        }
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
