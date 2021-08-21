package com.sherman.covid19.reservationtool.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class NurseVaccinationCentreTimeslot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private NurseVacCtrTimeSlotPK nurseVacCtrTimeSlotPK;

    public NurseVaccinationCentreTimeslot(NurseVacCtrTimeSlotPK nurseVacCtrTimeSlotPK) {
        this.nurseVacCtrTimeSlotPK = nurseVacCtrTimeSlotPK;
    }

    public NurseVaccinationCentreTimeslot (){

    }

    public NurseVacCtrTimeSlotPK getNurseVacCtrTimeSlotPK() {
        return nurseVacCtrTimeSlotPK;
    }

    public void setNurseVacCtrTimeSlotPK(NurseVacCtrTimeSlotPK nurseVacCtrTimeSlotPK) {
        this.nurseVacCtrTimeSlotPK = nurseVacCtrTimeSlotPK;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Embeddable
    public static class NurseVacCtrTimeSlotPK implements Serializable {
        @OneToOne
        private Nurse nurse;
        @OneToOne
        private Slot slot;
        @OneToOne
        private VaccinationCentre vaccinationCentre;

        public NurseVacCtrTimeSlotPK() {}

        public NurseVacCtrTimeSlotPK(Nurse nurse, Slot slot, VaccinationCentre vaccinationCentre) {
            this.nurse = nurse;
            this.slot = slot;
            this.vaccinationCentre = vaccinationCentre;
        }

        public Nurse getNurse() {
            return nurse;
        }

        public void setNurse(Nurse nurse) {
            this.nurse = nurse;
        }

        public Slot getSlot() {
            return slot;
        }

        public void setSlot(Slot slot) {
            this.slot = slot;
        }

        public VaccinationCentre getVaccinationCentre() {
            return vaccinationCentre;
        }

        public void setVaccinationCentre(VaccinationCentre vaccinationCentre) {
            this.vaccinationCentre = vaccinationCentre;
        }
    }
}
