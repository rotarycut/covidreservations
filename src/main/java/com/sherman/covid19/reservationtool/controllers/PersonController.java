package com.sherman.covid19.reservationtool.controllers;

import com.sherman.covid19.reservationtool.managers.PersonRepository;
import com.sherman.covid19.reservationtool.models.Person;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {
    private final PersonRepository repository;

    PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @CrossOrigin(origins = "https://homage-covid-client.herokuapp.com/")
    @GetMapping("/persons")
    List<Person> all() {
        return repository.findAll();
    }

    @CrossOrigin(origins = "https://homage-covid-client.herokuapp.com/")
    @PostMapping("/createPerson")
    Person newEmployee(@RequestBody Person newPerson) {
        return repository.save(newPerson);
    }
}
