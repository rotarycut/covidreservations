package com.sherman.covid19.reservationtool.managers;

import com.sherman.covid19.reservationtool.models.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotRepository extends JpaRepository<Slot, Long> {

}
