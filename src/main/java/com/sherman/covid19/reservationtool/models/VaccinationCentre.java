package com.sherman.covid19.reservationtool.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class VaccinationCentre {
    @Id
    private String name;
    private Long maxCapacity;

    public Long getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Long maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
