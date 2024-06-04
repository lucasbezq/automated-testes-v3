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
}
