# Covid Vaccination reservation System 

## Application link
https://homage-covid-client.herokuapp.com/
    
## Services codebase
https://github.com/rotarycut/covidreservations

## Client codebase
https://github.com/rotarycut/covidreservations-client

## Postgres Database login details
https://github.com/rotarycut/covidreservations/blob/main/src/main/resources/application.properties

## Stack
1. Spring boot 2.5 rest services in Java
2. ReactJS for rest services client on nginx container
3. PostgresDB for persistent storage

## Description of booking flow
1. User will be prompted to enter name
2. Upon clicking 'Next' button, checks will be done to see if user has an existing booking. 
    * If yes, populate appointment form with existing booking details
        * If user wants to update appointment details with desired new date/timeslot/centre, respective details need to be filled and upon clicking on 'Update' button, booking details will be updated in the database
        
2a1. If user wants to update appointment details with desired new date/timeslot/centre, respective details need to be filled and upon clicking on 'Update' button, booking details will be updated in the database
2a2. If user wants to delete appointment, clicking the 'Delete' button will remove the booking entry from the database
2b. If no, copy user entered name and user will have to choose desired appointment date, centre and timeslot
2b1. Upon clicking 'Create' button, a new booking will be created

## Data models
#### Person
Represents a person with attributes name and registered time
1. Name (Primary key)
2. Registered time

#### Nurse
Represents a nurse with attribute name
1. Name (Primary key)

#### Vaccination Centre
Represents a Vaccination Centre with attributes name and its max capacity 
1. Name (Primary Key)
2. Max Capacity

#### Slot
Represents a timeslot in a day
1. id (Primary Key)
2. timeslot

#### NurseVaccinationTimeslot
Represents the relationship between a Nurse, a timeslot, and a vaccination centre. The booking model below will reference this table and use it as a bridge to access slot, nurse and vaccination centre
1. id (Primary Key)
2. Nurse_name (Foreign key from Nurse Model)
3. Slot_id (Foreign key from slot Model)
4. Vaccination Centre Name (From Vaccination Centre Model)

#### Booking
Represents a booking, with a reference to the NurseVaccinationTimeslot table where 1 booking will be assigned a NurseVaccinationTimeslot
1. id (Primary key)
2. Nurse Vaccination centre timeslot id (from NurseVaccinationTimeslot model)
3. Person_name (foreign key from Person)
4. Last update time
5. vac_date (Vaccination date)
 

## Assumptions
1. Slots are standardized in 30 mins intervals from 9am - 3pm, and run everyday in this heightened phase
2. Nurses will be able to sign up for slot & vaccination centre (This potentially can be managed in another system, or manipulated in DB directly)
3. One person will only be able to attend 1 and only 1 booking only
4. Update booking functionality will be subject to availability constraints

## Improvements if given more time
1. Availability/forecast map to show future projected availability in the next x months
2. UI can leverage the data from #1 to display a heatmap of sorts to show which days are more available than others

## Rest Services project setup
1. Import project as maven project
2. mvn clean + mvn install
3. Run ReservationToolApplication.java to start up web services container

