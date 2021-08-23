package com.sherman.covid19.reservationtool.controllers;

import com.sherman.covid19.reservationtool.BootstrapDatabase;
import com.sherman.covid19.reservationtool.managers.*;
import com.sherman.covid19.reservationtool.models.*;
import com.sherman.covid19.reservationtool.models.external.IncomingBooking;
import com.sherman.covid19.reservationtool.utils.DateRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class BookingController {
    private final BookingRepository bookingRepository;
    private final PersonRepository personRepository;
    private final NurseVaccinationCentreTimeslotRepository nurseVaccinationCentreTimeslotRepository;
    private final VaccinationCentreRepository vaccinationCentreRepository;
    private final SlotRepository slotRepository;

    public BookingController(BookingRepository bookingRepository, PersonRepository personRepository,
                             NurseVaccinationCentreTimeslotRepository nurseVaccinationCentreTimeslotRepository,
                             VaccinationCentreRepository vaccinationCentreRepository,
                             SlotRepository slotRepository) {
        this.bookingRepository = bookingRepository;
        this.personRepository = personRepository;
        this.nurseVaccinationCentreTimeslotRepository = nurseVaccinationCentreTimeslotRepository;
        this.vaccinationCentreRepository = vaccinationCentreRepository;
        this.slotRepository = slotRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(BootstrapDatabase.class);

    @GetMapping("/bookings")
    @CrossOrigin
    public List<Booking> all() {
        return bookingRepository.findAll();
    }

    @PostMapping(value="/createBooking" , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<String> createBooking(@RequestBody IncomingBooking incomingBooking) throws Exception {
        log.info("Create Booking request received");

        if(incomingBooking == null){
            throw new Exception("Incoming Booking object is null!");
        }
        log.info("IncomingBooking Object attributes: " + incomingBooking.toString());

        String incomingBookingValidationString = validateIncomingBooking(incomingBooking);
        if(incomingBookingValidationString != null){
            return ResponseEntity.internalServerError().body("\"status:\": \"failure\", \"reason\": \"" + incomingBookingValidationString + "\"");
        }

        LocalDate requestedDate = LocalDate.parse(incomingBooking.getVac_date(), DateTimeFormatter.ofPattern("yyyyMMdd"));
        Person requestingPerson = personRepository.findById(incomingBooking.getPersonName()).orElse(null);
        if(requestingPerson == null){
            log.info("No person with name "+ incomingBooking.getPersonName() + " exists. Proceeding to create person.");
            personRepository.save(new Person(incomingBooking.getPersonName()));
            requestingPerson = personRepository.findById(incomingBooking.getPersonName()).get();
        }
        Booking personBooking = bookingRepository.findBookingByPerson(incomingBooking.getPersonName());

        if(personBooking != null){
            String existingBookingError = "Person " + personBooking.getPerson().getName() + " already has an existing booking for date " + personBooking.getVac_date()
                    + " and time " + personBooking.getNurseVaccinationCentreTimeslot().getNurseVacCtrTimeSlotPK().getSlot().getTimeslot() + " and centre " + personBooking.getNurseVaccinationCentreTimeslot().getNurseVacCtrTimeSlotPK().getVaccinationCentre().getName() + ". Please update your appointment instead.";
            return ResponseEntity.internalServerError().body("\"status:\": \"failure\", \"reason\": \"" + existingBookingError + "\"");
        }

        List<Booking> allBookings = bookingRepository.findAll();
        VaccinationCentre requestedVacCtr = vaccinationCentreRepository.findById(incomingBooking.getVac_centre_name()).orElse(null);
        if (requestedVacCtr == null){
            return ResponseEntity.internalServerError().body("{\"status\": \"failure\", \"reason\": \"Vaccine center with name " + incomingBooking.getVac_centre_name() + " cannot be found!\"}");
        }

        Map<VaccinationCentre, Long> vacCtrBookingsMap = allBookings.stream()
                .filter(x -> x.getVac_date().equals(requestedDate))
                .collect(Collectors.groupingBy(x -> x.getNurseVaccinationCentreTimeslot().getNurseVacCtrTimeSlotPK().getVaccinationCentre(), Collectors.counting()));

        Long currentCapacity = vacCtrBookingsMap.get(requestedVacCtr);
        if(currentCapacity != null && currentCapacity.equals(requestedVacCtr.getMaxCapacity())){
            return ResponseEntity.internalServerError().body("{\"status\": \"failure\", \"reason\": \"Vaccination Centre is full!\"}");
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

            return ResponseEntity.ok("{\"status\": \"success\"}");
        }else{
            //No more timeslots
            return ResponseEntity.internalServerError().body("{\"status\": \"failure\", \"reason\": \"No more available timeslots\"}");
        }
    }

    private String validateIncomingBooking(IncomingBooking incomingBooking) {
        StringBuilder errorString = new StringBuilder();
        if(incomingBooking.getVac_centre_name() == null || incomingBooking.getVac_centre_name().isEmpty()){
            errorString.append("Vaccination Center is Empty! ");
        }
        if(incomingBooking.getSlot() == null || incomingBooking.getSlot().isEmpty()){
            errorString.append("Slot is Empty! ");
        }
        if(incomingBooking.getVac_date() == null || incomingBooking.getVac_date().isEmpty()){
            errorString.append("Vaccination Date is Empty! ");
        }
        if(incomingBooking.getPersonName() == null  || incomingBooking.getPersonName().isEmpty()){
            errorString.append("Person Name is Empty! ");
        }

        if (errorString.length() > 0){
            return errorString.toString();
        }else{
            return null;
        }
    }

    @CrossOrigin
    @PostMapping(value="updateBooking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateBooking(@RequestBody IncomingBooking incomingBooking){
        log.info("Update Booking request received");

        String incomingBookingValidationString = validateIncomingBooking(incomingBooking);
        if(incomingBookingValidationString != null){
            return ResponseEntity.internalServerError().body("\"status:\": \"failure\", \"reason\": \"" + incomingBookingValidationString + "\"");
        }

        Booking existingBooking = bookingRepository.findBookingByPerson(incomingBooking.getPersonName());

        if(existingBooking != null){
            LocalDate requestedDate = LocalDate.parse(incomingBooking.getVac_date(), DateTimeFormatter.ofPattern("yyyyMMdd"));
            Person existingPerson = existingBooking.getPerson();

            List<Booking> allBookings = bookingRepository.findAll();
            VaccinationCentre requestedVacCtr = vaccinationCentreRepository.findById(incomingBooking.getVac_centre_name()).orElse(null);
            if (requestedVacCtr == null){
                return ResponseEntity.internalServerError().body("{\"status\": \"failure\", \"reason\": \"Vaccine center with name \"" + incomingBooking.getVac_centre_name() + " cannot be found!\"}");
            }

            Map<VaccinationCentre, Long> vacCtrBookingsMap = allBookings.stream()
                    .filter(x -> x.getVac_date().equals(requestedDate))
                    .collect(Collectors.groupingBy(x -> x.getNurseVaccinationCentreTimeslot().getNurseVacCtrTimeSlotPK().getVaccinationCentre(), Collectors.counting()));

            Long currentCapacity = vacCtrBookingsMap.get(requestedVacCtr);
            if (currentCapacity != null &&currentCapacity.equals(requestedVacCtr.getMaxCapacity())) {
                return ResponseEntity.internalServerError().body("{\"status\": \"failure\", \"reason\": \"Vaccination Centre is full!\"}");
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

                return ResponseEntity.ok("{\"status\": \"success\"}");
            }else{
                return ResponseEntity.internalServerError().body("{\"status\": \"failure\", \"reason\": \"Unable to reschedule due to no available slots\"}");
            }
        }else{
            return ResponseEntity.internalServerError().body("{\"status\": \"failure\", \"reason\": \"Booking does not exist!\"}");
        }
    }

    @CrossOrigin
    @PostMapping(value="deleteBooking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteBooking(@RequestBody IncomingBooking incomingBooking){
        log.info("Delete Booking request received");

        Booking existingBooking = bookingRepository.findBookingByPerson(incomingBooking.getPersonName());
        if(existingBooking != null){
            bookingRepository.delete(existingBooking);
            return ResponseEntity.ok("{\"status\": \"success\"}");
        }else{
            return ResponseEntity.internalServerError().body("{\"status\": \"failure\", \"reason\": \"No such booking!\"}");
        }
    }

    @CrossOrigin
    @GetMapping(value="getPersonBooking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Booking> getPersonBooking(@RequestParam String personName){
        log.info("Get Booking request received");

        Booking existingBooking = bookingRepository.findBookingByPerson(personName);
        if(existingBooking != null){
            return ResponseEntity.ok(existingBooking);
        }else{
            return ResponseEntity.noContent().build();
        }
    }

//    @GetMapping(value="/getBookingAvailability", produces = MediaType.APPLICATION_JSON_VALUE)
//    String getBookingAvailability(@RequestParam String date, @RequestParam String timeslot, @RequestParam String vacCentre) {
//        log.info("Get Booking Availability request received");
//
//        List<Slot> allSlots = slotRepository.findAll();
//        List<Booking> allBookings = bookingRepository.findAll();
//        Map<LocalDate, List<Booking>> bookedDatesMap = allBookings.stream().collect(Collectors.groupingBy(Booking::getVac_date));
//
//        Map<LocalDate, NurseVaccinationCentreTimeslot> availabilityMap = new HashMap<>();
//
//        DateRange dateRange = new DateRange(LocalDate.now(), LocalDate.now().plusMonths(3));
//
//        dateRange.stream().forEach(x -> {
//            if(x.getDayOfWeek().equals(DayOfWeek.SATURDAY) || x.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
//                List<Booking> existingBookingsOnDate = bookedDatesMap.get(x);
//                if(existingBookingsOnDate == null){
//
//                }
//            }
//        });
//
//
//        return "";
//    }
}
