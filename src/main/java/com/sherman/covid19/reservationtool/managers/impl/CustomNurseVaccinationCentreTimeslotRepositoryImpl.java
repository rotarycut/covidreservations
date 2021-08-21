package com.sherman.covid19.reservationtool.managers.impl;

import com.sherman.covid19.reservationtool.managers.CustomNurseVaccinationCentreTimeslotRepository;
import com.sherman.covid19.reservationtool.models.NurseVaccinationCentreTimeslot;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@SuppressWarnings("unchecked")
public class CustomNurseVaccinationCentreTimeslotRepositoryImpl implements CustomNurseVaccinationCentreTimeslotRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<NurseVaccinationCentreTimeslot> findTimeslotsNurseSlotVacCtr(String slotTime, String vacCtrName) {
        Query query = entityManager
                .createNativeQuery("select * from NURSE_VACCINATION_CENTRE_TIMESLOT where vaccination_centre_name=:ctr_name and slot_id in (select id from slot where timeslot = :slotTime)"
                , NurseVaccinationCentreTimeslot.class);
        query.setParameter("ctr_name", vacCtrName);
        query.setParameter("slotTime", LocalTime.parse(slotTime));

        return (List<NurseVaccinationCentreTimeslot>)query.getResultList();
    }
}
