# Covid Vaccination reservation System 

## Client codebase
https://github.com/rotarycut/covidreservations-client

## Data models
#### Person
Represents a person with attributes:
1. Name (Primary key)
2. Registered time

#### Nurse
Represents a nurse with attributes:
1. Name (Primary key)

#### Vaccination Centre
1. Name (Primary Key)
2. Max Capacity

#### Slot
1. id (Primary Key)
2. timeslot

#### NurseVaccinationTimeslot
1. id (Primary Key)
2. Nurse_name (Foreign key from Nurse Model)
3. Slot_id (Foreign key from slot Model)
4. Vaccination Centre Name (From Vaccination Centre Model)

#### Booking
1. id (Primary key)
2. Nurse Vaccination centre timeslot id (from NurseVaccinationTimeslot model)
3. Person_name (foreign key from Person)
4. Last update time
5. vac_date (Vaccination date)

## Description of booking flow
When a booking is made, 

