package com.sherman.covid19.reservationtool.controllers;

import com.sherman.covid19.reservationtool.managers.BookingRepository;
import com.sherman.covid19.reservationtool.managers.NurseVaccinationCentreTimeslotRepository;
import com.sherman.covid19.reservationtool.managers.PersonRepository;
import com.sherman.covid19.reservationtool.managers.VaccinationCentreRepository;
import com.sherman.covid19.reservationtool.models.*;
import com.sherman.covid19.reservationtool.models.external.IncomingBooking;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class BookingController {
    private final BookingRepository bookingRepository;
    private final PersonRepository personRepository;
    private final NurseVaccinationCentreTimeslotRepository nurseVaccinationCentreTimeslotRepository;
    private final VaccinationCentreRepository vaccinationCentreRepository;

    public BookingController(BookingRepository bookingRepository, PersonRepository personRepository,
                             NurseVaccinationCentreTimeslotRepository nurseVaccinationCentreTimeslotRepository,
                             VaccinationCentreRepository vaccinationCentreRepository) {
        this.bookingRepository = bookingRepository;
        this.personRepository = personRepository;
        this.nurseVaccinationCentreTimeslotRepository = nurseVaccinationCentreTimeslotRepository;
        this.vaccinationCentreRepository = vaccinationCentreRepository;
    }

    @GetMapping("/bookings")
    List<Booking> all() {
        return bookingRepository.findAll();
    }

    @PostMapping(value="/createBooking" , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    String createBooking(@RequestBody IncomingBooking incomingBooking) throws Exception {
        if(incomingBooking == null){
            throw new Exception("Incoming Booking object is null!");
        }
        LocalDate requestedDate = LocalDate.parse(incomingBooking.getVac_date(), DateTimeFormatter.ofPattern("yyyyMMdd"));
        Person requestingPerson = personRepository.findById(incomingBooking.getPersonName()).get();
        Booking personBooking = bookingRepository.findBookingByPerson(incomingBooking.getPersonName());

        if(personBooking != null){
            throw new Exception("Person " + incomingBooking.getPersonName() + " already has an existing booking for date " + incomingBooking.getVac_date()
                    + " and time " + incomingBooking.getSlot() + " and centre " + incomingBooking.getVac_centre_name() + ". Please update your appointment instead.");
        }

        List<Booking> allBookings = bookingRepository.findAll();
        VaccinationCentre requestedVacCtr = vaccinationCentreRepository.findById(incomingBooking.getVac_centre_name()).get();
        Map<VaccinationCentre, Long> vacCtrBookingsMap = allBookings.stream()
                .filter(x -> x.getVac_date().equals(requestedDate))
                .collect(Collectors.groupingBy(x -> x.getNurseVaccinationCentreTimeslot().getNurseVacCtrTimeSlotPK().getVaccinationCentre(), Collectors.counting()));
        Map<LocalTime, List<Booking>> bookedSlotsMap = allBookings.stream()
                .filter(x -> x.getVac_date().equals(requestedDate))
                .filter(x -> x.getNurseVaccinationCentreTimeslot().getNurseVacCtrTimeSlotPK().getVaccinationCentre().equals(requestedVacCtr))
                .collect(Collectors.groupingBy(x -> x.getNurseVaccinationCentreTimeslot().getNurseVacCtrTimeSlotPK().getSlot().getTimeslot()));

        List<NurseVaccinationCentreTimeslot> allAvailableSlots = nurseVaccinationCentreTimeslotRepository.findTimeslotsNurseSlotVacCtr(incomingBooking.getSlot(), incomingBooking.getVac_centre_name());
        List<Booking> bookedSlotsForRequestedDay = bookedSlotsMap.get(LocalTime.parse(incomingBooking.getSlot()));
        bookedSlotsForRequestedDay.forEach(x -> {
            allAvailableSlots.remove(x.getNurseVaccinationCentreTimeslot());
        });

        if(allAvailableSlots.size() > 0){
            NurseVaccinationCentreTimeslot nurseVaccinationCentreTimeslot = allAvailableSlots.get(0);
            Booking b = new Booking(requestedDate, LocalDateTime.now(), nurseVaccinationCentreTimeslot, requestingPerson);
            bookingRepository.save(b);

            return "{'status': 'success'}";
        }else{
            //Slots are full
            return "{'status': 'failure', 'reason': 'Booking slots are full!'}";
        }
    }

    @GetMapping(value="updateBooking", produces = MediaType.APPLICATION_JSON_VALUE)
    String updateBooking(@RequestBody IncomingBooking incomingBooking){
        Booking existingBooking = bookingRepository.findBookingByPerson(incomingBooking.getPersonName());
        if(existingBooking != null){

        }else{

        }
        return "";
    }

    @GetMapping(value="/getBookingAvailability", produces = MediaType.APPLICATION_JSON_VALUE)
    String getBookingAvailability(@RequestParam String date, @RequestParam String timeslot, @RequestParam String vacCentre) {
        List<NurseVaccinationCentreTimeslot> nurseVaccinationCentreTimeslots = nurseVaccinationCentreTimeslotRepository.findTimeslotsNurseSlotVacCtr(timeslot, vacCentre);
        List<Long> nurseVaccinationCentreTimeslotsIds = nurseVaccinationCentreTimeslots.stream().map(NurseVaccinationCentreTimeslot::getId).collect(Collectors.toList());

        List<Booking> allBookings = bookingRepository.findAll();
        List<Booking> availableBookings = new ArrayList<>();
        allBookings.forEach(x -> {
            if (!nurseVaccinationCentreTimeslotsIds.contains(x.getNurseVaccinationCentreTimeslot().getId())){
                availableBookings.add(x);
            }
        });

        return "";
    }
}
