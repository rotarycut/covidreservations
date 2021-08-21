drop table person;
CREATE TABLE IF NOT EXISTS person (name VARCHAR ( 80 ) PRIMARY KEY NOT NULL, registered TIMESTAMP );
delete from person;

--

--drop table slot;
CREATE TABLE IF NOT EXISTS slot (id serial PRIMARY KEY, timeslot TIME ( 80 ) NOT NULL );
--delete from slot;

--
--drop table nurse;
CREATE TABLE IF NOT EXISTS nurse (name VARCHAR(80) PRIMARY KEY not null);
--delete from nurse;

--drop table VACCINATION_CENTRE;
CREATE TABLE IF NOT EXISTS VACCINATION_CENTRE (name VARCHAR(20) PRIMARY KEY not null,  max_capacity INTEGER);
--delete from VACCINATION_CENTRE;

CREATE TABLE IF NOT EXISTS NURSE_VACCINATION_CENTRE_TIMESLOT (
        nurse_name VARCHAR(80) REFERENCES nurse(name),
        slot_id integer REFERENCES slot(id),
        VACCINATION_CENTRE_name varchar(20) REFERENCES VACCINATION_CENTRE(name),
        PRIMARY KEY(nurse_name, slot_id, VACCINATION_CENTRE_name)
)