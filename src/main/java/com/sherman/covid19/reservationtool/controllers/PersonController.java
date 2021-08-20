package com.sherman.covid19.reservationtool.controllers;

import com.sherman.covid19.reservationtool.managers.PersonRepository;
import com.sherman.covid19.reservationtool.models.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {
    private final PersonRepository repository;

    PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/persons")
    List<Person> all() {
        return repository.findAll();
    }

    @PostMapping("/createPerson")
    Person newEmployee(@RequestBody Person newPerson) {
        return repository.save(newPerson);
    }
}
