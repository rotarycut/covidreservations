package com.sherman.covid19.reservationtool.controllers;

import com.sherman.covid19.reservationtool.BootstrapDatabase;
import com.sherman.covid19.reservationtool.managers.BookingRepository;
import com.sherman.covid19.reservationtool.managers.NurseVaccinationCentreTimeslotRepository;
import com.sherman.covid19.reservationtool.managers.PersonRepository;
import com.sherman.covid19.reservationtool.managers.VaccinationCentreRepository;
import com.sherman.covid19.reservationtool.models.*;
import com.sherman.covid19.reservationtool.models.external.IncomingBooking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    private static final Logger log = LoggerFactory.getLogger(BootstrapDatabase.class);

    @GetMapping("/bookings")
    public List<Booking> all() {
        return bookingRepository.findAll();
    }

    @PostMapping(value="/createBooking" , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createBooking(@RequestBody IncomingBooking incomingBooking) throws Exception {
        log.info("Create Booking request received");

        if(incomingBooking == null){
            throw new Exception("Incoming Booking object is null!");
        }
        LocalDate requestedDate = LocalDate.parse(incomingBooking.getVac_date(), DateTimeFormatter.ofPattern("yyyyMMdd"));
        Person requestingPerson = personRepository.findById(incomingBooking.getPersonName()).get();
        Booking personBooking = bookingRepository.findBookingByPerson(incomingBooking.getPersonName());

        if(personBooking != null){
            return ResponseEntity.internalServerError().body("Person " + incomingBooking.getPersonName() + " already has an existing booking for date " + incomingBooking.getVac_date()
                    + " and time " + incomingBooking.getSlot() + " and centre " + incomingBooking.getVac_centre_name() + ". Please update your appointment instead.");
        }

        List<Booking> allBookings = bookingRepository.findAll();
        VaccinationCentre requestedVacCtr = vaccinationCentreRepository.findById(incomingBooking.getVac_centre_name()).get();
        Map<VaccinationCentre, Long> vacCtrBookingsMap = allBookings.stream()
                .filter(x -> x.getVac_date().equals(requestedDate))
                .collect(Collectors.groupingBy(x -> x.getNurseVaccinationCentreTimeslot().getNurseVacCtrTimeSlotPK().getVaccinationCentre(), Collectors.counting()));

        Long currentCapacity = vacCtrBookingsMap.get(requestedVacCtr);
        if(currentCapacity != null && currentCapacity.equals(requestedVacCtr.getMaxCapacity())){
            return ResponseEntity.badRequest().body("{'status': 'failure', 'reason': 'Vaccination Centre is full!'}");
        }

        Map<LocalTime, List<Booking>> bookedSlotsMap = allBookings.stream()
                .filter(x -> x.getVac_date().equals(requestedDate))
                .filter(x -> x.getNurseVaccinationCentreTimeslot().getNurseVacCtrTimeSlotPK().getVaccinationCentre().equals(requestedVacCtr))
                .collect(Collectors.groupingBy(x -> x.getNurseVaccinationCentreTimeslot().getNurseVacCtrTimeSlotPK().getSlot().getTimeslot()));

        List<NurseVaccinationCentreTimeslot> allAvailableSlots = nurseVaccinationCentreTimeslotRepository.findTimeslotsNurseSlotVacCtr(incomingBooking.getSlot(), incomingBooking.getVac_centre_name());
        List<Booking> bookedSlotsForRequestedDay = bookedSlotsMap.get(LocalTime.parse(incomingBooking.getSlot()));
        if(bookedSlotsForRequestedDay != null) {
            bookedSlotsForRequestedDay.forEach(x -> {
                allAvailableSlots.remove(x.getNurseVaccinationCentreTimeslot());
            });
        }

        if(allAvailableSlots.size() > 0){
            NurseVaccinationCentreTimeslot nurseVaccinationCentreTimeslot = allAvailableSlots.get(0);
            Booking b = new Booking(requestedDate, LocalDateTime.now(), nurseVaccinationCentreTimeslot, requestingPerson);
            bookingRepository.save(b);

            return ResponseEntity.ok("{'status': 'success'}");
        }else{
            //No more timeslots
            return ResponseEntity.badRequest().body("{'status': 'failure', 'reason': 'No more available timeslots'}");
        }
    }

    @PostMapping(value="updateBooking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateBooking(@RequestBody IncomingBooking incomingBooking){
        log.info("Update Booking request received");

        Booking existingBooking = bookingRepository.findBookingByPerson(incomingBooking.getPersonName());

        if(existingBooking != null){
            LocalDate requestedDate = LocalDate.parse(incomingBooking.getVac_date(), DateTimeFormatter.ofPattern("yyyyMMdd"));
            Person existingPerson = existingBooking.getPerson();

            List<Booking> allBookings = bookingRepository.findAll();
            VaccinationCentre requestedVacCtr = vaccinationCentreRepository.findById(incomingBooking.getVac_centre_name()).get();
            Map<VaccinationCentre, Long> vacCtrBookingsMap = allBookings.stream()
                    .filter(x -> x.getVac_date().equals(requestedDate))
                    .collect(Collectors.groupingBy(x -> x.getNurseVaccinationCentreTimeslot().getNurseVacCtrTimeSlotPK().getVaccinationCentre(), Collectors.counting()));

            Long currentCapacity = vacCtrBookingsMap.get(requestedVacCtr);
            if (currentCapacity != null &&currentCapacity.equals(requestedVacCtr.getMaxCapacity())) {
                return ResponseEntity.badRequest().body("{'status': 'failure', 'reason': 'Vaccination Centre is full!'}");
            }

            Map<LocalTime, List<Booking>> bookedSlotsMap = allBookings.stream()
                    .filter(x -> x.getVac_date().equals(requestedDate))
                    .filter(x -> x.getNurseVaccinationCentreTimeslot().getNurseVacCtrTimeSlotPK().getVaccinationCentre().equals(requestedVacCtr))
                    .collect(Collectors.groupingBy(x -> x.getNurseVaccinationCentreTimeslot().getNurseVacCtrTimeSlotPK().getSlot().getTimeslot()));

            List<NurseVaccinationCentreTimeslot> allAvailableSlots = nurseVaccinationCentreTimeslotRepository.findTimeslotsNurseSlotVacCtr(incomingBooking.getSlot(), incomingBooking.getVac_centre_name());
            List<Booking> bookedSlotsForRequestedDay = bookedSlotsMap.get(LocalTime.parse(incomingBooking.getSlot()));
            if(bookedSlotsForRequestedDay != null) {
                bookedSlotsForRequestedDay.forEach(x -> {
                    allAvailableSlots.remove(x.getNurseVaccinationCentreTimeslot());
                });
            }

            if(allAvailableSlots.size() > 0){
                existingBooking.setLastUpdate(LocalDateTime.now());

                //Set to first available slot. Persons cant choose nurses.
                existingBooking.setNurseVaccinationCentreTimeslot(allAvailableSlots.get(0));
                existingBooking.setVac_date(requestedDate);
                bookingRepository.save(existingBooking);

                return ResponseEntity.ok("{'status': 'success'}");
            }else{
                return ResponseEntity.badRequest().body("{'status': 'failure', 'reason': 'Unable to reschedule due to any available slots'}");
            }
        }else{
            return ResponseEntity.internalServerError().body("{'status': 'failure', 'reason': 'Booking does not exist!'}");
        }
    }

    @PostMapping(value="deleteBooking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteBooking(@RequestBody IncomingBooking incomingBooking){
        log.info("Delete Booking request received");

        Booking existingBooking = bookingRepository.findBookingByPerson(incomingBooking.getPersonName());
        if(existingBooking != null){
            bookingRepository.delete(existingBooking);
            return ResponseEntity.ok("{'status': 'success'}");
        }else{
            return ResponseEntity.badRequest().body("{'status': 'failure', 'reason': 'No such booking!'}");
        }
    }

    @GetMapping(value="getPersonBooking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Booking> getPersonBooking(@RequestParam String personName){
        log.info("Get Booking request received");

        Booking existingBooking = bookingRepository.findBookingByPerson(personName);
        if(existingBooking != null){
            return ResponseEntity.ok(existingBooking);
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value="/getBookingAvailability", produces = MediaType.APPLICATION_JSON_VALUE)
    String getBookingAvailability(@RequestParam String date, @RequestParam String timeslot, @RequestParam String vacCentre) {
        log.info("Get Booking Availability request received");

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
