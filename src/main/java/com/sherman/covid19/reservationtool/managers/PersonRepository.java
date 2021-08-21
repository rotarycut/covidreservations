package com.sherman.covid19.reservationtool.managers;

import com.sherman.covid19.reservationtool.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, String> {
}
