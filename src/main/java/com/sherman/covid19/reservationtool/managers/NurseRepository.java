package com.sherman.covid19.reservationtool.managers;

import com.sherman.covid19.reservationtool.models.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NurseRepository extends JpaRepository<Nurse, String> {
}
