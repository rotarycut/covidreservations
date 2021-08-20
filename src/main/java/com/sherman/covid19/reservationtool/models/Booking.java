package com.sherman.covid19.reservationtool.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Booking {
    @Id
    private Long id;
    @OneToOne
    private Person person;
    @OneToOne
    private Nurse nurse;
    @OneToOne
    private VaccinationCentre vaccinationCentre;
    @OneToOne
    private Slot slot;

    private LocalDate date;

    private LocalDateTime lastUpdate;

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

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Nurse getNurse() {
        return nurse;
    }

    public void setNurse(Nurse nurse) {
        this.nurse = nurse;
    }

    public VaccinationCentre getVaccinationCentre() {
        return vaccinationCentre;
    }

    public void setVaccinationCentre(VaccinationCentre vaccinationCentre) {
        this.vaccinationCentre = vaccinationCentre;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
