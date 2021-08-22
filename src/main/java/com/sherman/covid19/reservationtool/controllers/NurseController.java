package com.sherman.covid19.reservationtool.controllers;

import com.sherman.covid19.reservationtool.managers.NurseRepository;
import com.sherman.covid19.reservationtool.models.Nurse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NurseController {
    private final NurseRepository repository;

    public NurseController(NurseRepository repository) {
        this.repository = repository;
    }

    @CrossOrigin
    @GetMapping("/nurses")
    List<Nurse> all() {
        return repository.findAll();
    }
}
