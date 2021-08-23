# Covid Vaccination reservation System 

## Application link
https://homage-covid-client.herokuapp.com/

## Client codebase
https://github.com/rotarycut/covidreservations-client

## Stack
1. Spring boot rest services in Java
2. React native for rest services client
3. PostgresDB

## Postgres Database login details
https://github.com/rotarycut/covidreservations/blob/main/src/main/resources/application.properties

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
When a booking is made, checks for vaccination centre capacity and timeslots are checked. New Person is created on
first page if person does not exist in the system.

## Assumptions
1. Slots are standardized in 30 mins intervals from 9am - 3pm.
2. Nurses will be able to sign up for slot & vaccination centre (This can be managed in another system, or manipulated in DB directly)
3. One person will only be able to attend 1 and only 1 booking only
4. 

## Improvements
1. Availability/forecast map to show future projected availability in the next x months
2. 

