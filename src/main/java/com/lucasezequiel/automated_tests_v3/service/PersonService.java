package com.lucasezequiel.automated_tests_v3.service;

import com.lucasezequiel.automated_tests_v3.exception.EmailAlreadyExistsException;
import com.lucasezequiel.automated_tests_v3.exception.ResourceNotFoundException;
import com.lucasezequiel.automated_tests_v3.model.Person;
import com.lucasezequiel.automated_tests_v3.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonService {

    private Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    private PersonRepository repository;

    public List<Person> findAll() {
        logger.info("Finding all people!");
        return repository.findAll();
    }

    public Person findById(Long id) {
        logger.info("Finding one person!");
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
    }

    public Person create(Person person) {
        logger.info("Creating one person!");
        var personFound = repository.findByEmail(person.getEmail());
        if (personFound.isPresent()) {
            throw new EmailAlreadyExistsException("Person already exists with given e-mail: " + person.getEmail());
        }

        return repository.save(person);
    }

    public Person update(Person person) {

        logger.info("Updating one person!");

        var personFound = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        personFound.setFirstName(person.getFirstName());
        personFound.setLastName(person.getLastName());
        personFound.setAddress(person.getAddress());
        personFound.setGender(person.getGender());

        return repository.save(person);
    }

    public void delete(Long id) {

        logger.info("Deleting one person!");

        var person = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        repository.delete(person);
    }
}
