package com.sherman.covid19.reservationtool.managers.impl;

import com.sherman.covid19.reservationtool.managers.CustomBookingRepository;
import com.sherman.covid19.reservationtool.models.Booking;
import com.sherman.covid19.reservationtool.models.NurseVaccinationCentreTimeslot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
@SuppressWarnings("unchecked")
public class CustomBookingRepositoryImpl implements CustomBookingRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger log = LoggerFactory.getLogger(CustomBookingRepositoryImpl.class);

    @Override
    public Booking findBookingByPerson(String personName) {
        Query query = entityManager
                .createNativeQuery("select * from booking where person_name=:personName"
                        , Booking.class);
        query.setParameter("personName", personName);

        try {
            return (Booking) query.getSingleResult();
        }catch (NoResultException e){
            log.info("No booking found for personName: " + personName);
            return null;
        }
    }
}
