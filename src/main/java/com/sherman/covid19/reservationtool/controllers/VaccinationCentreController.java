package com.sherman.covid19.reservationtool.controllers;

import com.sherman.covid19.reservationtool.managers.VaccinationCentreRepository;
import com.sherman.covid19.reservationtool.models.VaccinationCentre;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VaccinationCentreController {
    private final VaccinationCentreRepository repository;

    public VaccinationCentreController(VaccinationCentreRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/vaccinationCentres")
    List<VaccinationCentre> all() {
        return repository.findAll();
    }
}
