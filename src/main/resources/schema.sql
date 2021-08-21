--drop table person;
CREATE TABLE IF NOT EXISTS person (name VARCHAR ( 80 ) PRIMARY KEY NOT NULL, registered TIMESTAMP );
--delete from person;

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
        id serial not null primary key,
        nurse_name VARCHAR(80) REFERENCES nurse(name),
        slot_id integer REFERENCES slot(id),
        VACCINATION_CENTRE_name varchar(20) REFERENCES VACCINATION_CENTRE(name),
        UNIQUE (nurse_name, slot_id, VACCINATION_CENTRE_name, id)
);

create table if not exists BOOKING (
        id serial not null,
        NURSE_VACCINATION_CENTRE_TIMESLOT_ID integer references NURSE_VACCINATION_CENTRE_TIMESLOT ( id),
        person_name varchar(80) references person(name),
        last_update TIMESTAMP,
        vac_date date not null,
        PRIMARY KEY(NURSE_VACCINATION_CENTRE_TIMESLOT_ID, person_name, id, vac_date)
)