package com.sherman.covid19.reservationtool;

import com.sherman.covid19.reservationtool.managers.PersonRepository;
import com.sherman.covid19.reservationtool.models.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@Configuration
public class BootstrapDatabase {
    private static final Logger log = LoggerFactory.getLogger(BootstrapDatabase.class);

    @Bean
    CommandLineRunner initDatabase(PersonRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Person("Sherman")));
            log.info("Preloading " + repository.save(new Person("Dan")));
        };
    }
}
