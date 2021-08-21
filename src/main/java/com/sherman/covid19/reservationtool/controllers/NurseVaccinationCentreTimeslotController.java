package com.sherman.covid19.reservationtool.controllers;

import com.sherman.covid19.reservationtool.managers.NurseVaccinationCentreTimeslotRepository;
import com.sherman.covid19.reservationtool.models.NurseVaccinationCentreTimeslot;
import com.sherman.covid19.reservationtool.models.VaccinationCentre;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NurseVaccinationCentreTimeslotController {
    private final NurseVaccinationCentreTimeslotRepository repository;

    public NurseVaccinationCentreTimeslotController(NurseVaccinationCentreTimeslotRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/nurseVaccinationCentreTimeslots")
    List<NurseVaccinationCentreTimeslot> all() {
        return repository.findAll();
    }
}
