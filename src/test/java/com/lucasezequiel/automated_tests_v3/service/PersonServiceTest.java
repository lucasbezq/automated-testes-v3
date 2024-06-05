package com.lucasezequiel.automated_tests_v3.service;

import com.lucasezequiel.automated_tests_v3.exception.EmailAlreadyExistsException;
import com.lucasezequiel.automated_tests_v3.exception.ResourceNotFoundException;
import com.lucasezequiel.automated_tests_v3.model.Person;
import com.lucasezequiel.automated_tests_v3.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @InjectMocks
    private PersonService service;

    @Mock
    private PersonRepository repository;

    private Person person;

    @BeforeEach
    void setup() {
        person = new Person("Lucas", "Ezequiel",
                "ezequiel@gmail.com", "Rio de Janeiro - RJ", "Male");
    }

    @DisplayName("Given Person Object when save Person then return Person Object")
    @Test
    void testGivenPersonObject_WhenSavePerson_ThenReturnPersonObject() {
        var expectedEmail = person.getEmail();
        given(repository.findByEmail(expectedEmail)).willReturn(Optional.empty());
        given(repository.save(person)).willReturn(person);

        var savedPerson = service.create(person);

        assertNotNull(savedPerson);
        assertEquals(expectedEmail, savedPerson.getEmail());
        verify(repository, times(1)).save(person);
    }

    @DisplayName("Given existing e-mail when save Person then throws EmailAlreadyExistsException")
    @Test
    void testGivenExistingEmail_WhenSavePerson_ThenThrowsException() {
        given(repository.findByEmail(person.getEmail())).willReturn(Optional.of(person));
        var expectedMessage = "Person already exists with given e-mail: " + person.getEmail();

        var exception = assertThrows(EmailAlreadyExistsException.class, () -> {
           service.create(person);
        });

        verify(repository, never()).save(any(Person.class));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @DisplayName("Given Person list when findAll persons then Persons List")
    @Test
    void testGivenPersonsList_WhenFindAllPersons_ThenReturnPersonsList() {
        var person2 = new Person("Elis", "Bruna",
                "elis@gmail.com", "Rio de Janeiro - RJ", "Female");
        given(repository.findAll()).willReturn(List.of(person, person2));

        var personsList = service.findAll();

        assertNotNull(personsList);
        assertEquals(2, personsList.size());
    }

    @DisplayName("Given empty Person list when findAll persons then empty Persons List")
    @Test
    void testGivenEmptyPersonsList_WhenFindAllPersons_ThenReturnEmptyPersonsList() {
        var person2 = new Person("Elis", "Bruna",
                "elis@gmail.com", "Rio de Janeiro - RJ", "Female");
        given(repository.findAll()).willReturn(Collections.emptyList());

        var personsList = service.findAll();

        assertTrue(personsList.isEmpty());
        assertEquals(0, personsList.size());
    }

    @DisplayName("Given PersonId when findById then return Person Object")
    @Test
    void testGivenPersonId_WhenFindById_ThenReturnPersonObject() {
        given(repository.findById(anyLong())).willReturn(Optional.of(person));

        var savedPerson = service.findById(anyLong());

        assertNotNull(person);
        assertEquals(person, savedPerson);
    }

    @DisplayName("Given Person Object when update Person then return updated Person")
    @Test
    void testGivenPersonObject_WhenUpdatePerson_ThenReturnUpdatedPerson() {
        person.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(person));
        person.setFirstName("Zoro");
        person.setEmail("zoro@gmail.com");
        given(repository.save(person)).willReturn(person);

        var updatedPerson = service.update(person);

        assertNotNull(person);
        assertEquals("Zoro", updatedPerson.getFirstName());
        assertEquals("zoro@gmail.com", updatedPerson.getEmail());
    }

    @DisplayName("Given PersonId when delete Person then return do nothing")
    @Test
    void testGivenPersonId_WhenDeletePerson_ThenReturnDoNothing() {
        person.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(person));
        willDoNothing().given(repository).delete(person);

        service.delete(person.getId());

        verify(repository, times(1)).delete(person);
    }
}
