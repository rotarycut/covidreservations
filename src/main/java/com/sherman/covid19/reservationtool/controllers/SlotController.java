package com.sherman.covid19.reservationtool.controllers;

import com.sherman.covid19.reservationtool.managers.SlotRepository;
import com.sherman.covid19.reservationtool.models.Slot;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SlotController {
    private final SlotRepository repository;

    public SlotController(SlotRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/slots")
    @CrossOrigin
    List<Slot> all() {
        return repository.findAll();
    }
}
