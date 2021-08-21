package com.sherman.covid19.reservationtool.managers;

import com.sherman.covid19.reservationtool.models.NurseVaccinationCentreTimeslot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public interface CustomNurseVaccinationCentreTimeslotRepository {
    List<NurseVaccinationCentreTimeslot> findTimeslotsNurseSlotVacCtr(String slotTime, String vacCtrName);
}
