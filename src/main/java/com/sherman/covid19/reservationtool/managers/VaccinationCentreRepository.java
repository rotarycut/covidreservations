package com.sherman.covid19.reservationtool.managers;

import com.sherman.covid19.reservationtool.models.VaccinationCentre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccinationCentreRepository extends JpaRepository<VaccinationCentre, Long> {

}
