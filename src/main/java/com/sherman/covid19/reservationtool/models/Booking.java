package com.sherman.covid19.reservationtool.models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"nurse_vaccination_centre_timeslot_id", "person_name","id","vac_date"})
})
public class Booking {
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "vac_date")
    private LocalDate vac_date;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "nurse_vaccination_centre_timeslot_id", referencedColumnName = "id" )
    private NurseVaccinationCentreTimeslot nurseVaccinationCentreTimeslot;

    @OneToOne
    @JoinColumn(name = "person_name", referencedColumnName = "name")
    private Person person;

    public Booking(LocalDate vac_date, LocalDateTime lastUpdate, NurseVaccinationCentreTimeslot nurseVaccinationCentreTimeslot, Person person) {
        this.vac_date = vac_date;
        this.lastUpdate = lastUpdate;
        this.nurseVaccinationCentreTimeslot = nurseVaccinationCentreTimeslot;
        this.person = person;
    }

    public  Booking() {};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getVac_date() {
        return vac_date;
    }

    public void setVac_date(LocalDate vac_date) {
        this.vac_date = vac_date;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public NurseVaccinationCentreTimeslot getNurseVaccinationCentreTimeslot() {
        return nurseVaccinationCentreTimeslot;
    }

    public void setNurseVaccinationCentreTimeslot(NurseVaccinationCentreTimeslot nurseVaccinationCentreTimeslot) {
        this.nurseVaccinationCentreTimeslot = nurseVaccinationCentreTimeslot;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }


}
