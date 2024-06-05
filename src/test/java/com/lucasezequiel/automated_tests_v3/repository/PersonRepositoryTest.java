package com.lucasezequiel.automated_tests_v3.repository;

import com.lucasezequiel.automated_tests_v3.model.Person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository repository;

    Person person;

    @BeforeEach
    void setup() {
        person = new Person("Lucas", "Ezequiel",
                "ezequiel@gmail.com", "Rio de Janeiro - RJ", "Male");
    }

    @DisplayName("Given Person Object when save then return saved Person")
    @Test
    void testGivenPersonObject_WhenSave_ThenReturnSavedPerson() {
        Person savedPerson = repository.save(person);

        assertNotNull(savedPerson);
        assertTrue(savedPerson.getId() > 0);
    }

    @DisplayName("Given Person List when findAll then return Person List")
    @Test
    void testGivenPersonList_WhenFindAll_ThenReturnPersonList() {
        Person person02 = new Person("Elis", "Brauna",
                "elis@gmail.com", "Rio de Janeiro - RJ", "Female");
        repository.save(person);
        repository.save(person02);

        var personList = repository.findAll();

        assertNotNull(personList);
        assertEquals(2, personList.size());
    }

    @DisplayName("Given Person Object when findById then return Person Object")
    @Test
    void testGivenPersonObject_WhenFindById_ThenReturnPersonObject() {
        repository.save(person);

        var savedPerson = repository.findById(person.getId()).get();

        assertNotNull(person);
        assertEquals(person.getId(), savedPerson.getId());
    }

    @DisplayName("Given Person Object when findByEmail then return Person Object")
    @Test
    void testGivenPersonObject_WhenFindByEmail_ThenReturnPersonObject() {
        repository.save(person);

        var savedPerson = repository.findByEmail(person.getEmail()).get();

        assertNotNull(savedPerson);
        assertEquals(person.getEmail(), savedPerson.getEmail());
    }

    @DisplayName("Given Person Object when update email then return updated Person Object")
    @Test
    void testGivenPersonList_WhenUpdatePerson_ThenReturnUpdatedPersonObject() {
        var expectedEmail = "lucas@gmail.com";
        repository.save(person);

        var savedPerson = repository.findById(person.getId()).get();
        savedPerson.setEmail(expectedEmail);
        repository.save(savedPerson);

        assertNotNull(savedPerson);
        assertEquals(expectedEmail, savedPerson.getEmail());
    }

    @DisplayName("Given Person Object when delete then remove Person")
    @Test
    void testGivenPersonObject_WhenDelete_ThenRemovePerson() {
        repository.save(person);

        repository.deleteById(person.getId());

        var personOptional = repository.findById(person.getId());

        assertTrue(personOptional.isEmpty());
    }

    @DisplayName("Given firstName and lastName when findByJPQLNamedParameters then return Person")
    @Test
    void testGivenFirstNameAndLastName_WhenFindByJPQLNamedParameters_ThenReturnPerson() {
        repository.save(person);

        var savedPerson = repository.findByJPQLNamedParameters(person.getFirstName(), person.getLastName());

        assertNotNull(savedPerson);
        assertEquals(person, savedPerson);
    }

    @DisplayName("Given firstName and lastName when findByNativeSQL then return Person")
    @Test
    void testGivenFirstNameAndLastName_WhenFindByJPQL_ThenReturnPerson() {
        repository.save(person);

        var savedPerson = repository.findByNativeSQL(person.getFirstName(), person.getLastName());

        assertNotNull(savedPerson);
        assertEquals(person, savedPerson);
    }
}
