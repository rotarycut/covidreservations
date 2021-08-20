package com.sherman.covid19.reservationtool.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Person {
    @Id
    private String name;

    public LocalDateTime registered;

    public Person(){
        this.name = "dummy";
        this.registered = LocalDateTime.now();
    }

    public Person(String name) {
        this.name = name;
        this.registered = LocalDateTime.now();
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
    }
}
