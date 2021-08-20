drop table person;
CREATE TABLE IF NOT EXISTS person (name VARCHAR ( 80 ) PRIMARY KEY NOT NULL, registered TIMESTAMP );
delete from person;

--

drop table slot;
CREATE TABLE IF NOT EXISTS slot (id serial PRIMARY KEY, timeslot TIME ( 80 ) NOT NULL );
delete from slot;

--
--drop table nurse;
CREATE TABLE IF NOT EXISTS nurse (name VARCHAR(80) PRIMARY KEY not null);
delete from nurse;
