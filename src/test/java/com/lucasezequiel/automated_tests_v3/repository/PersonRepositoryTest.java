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
