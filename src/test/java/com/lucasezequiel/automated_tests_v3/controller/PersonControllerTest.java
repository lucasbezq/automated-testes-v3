package com.lucasezequiel.automated_tests_v3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasezequiel.automated_tests_v3.exception.ResourceNotFoundException;
import com.lucasezequiel.automated_tests_v3.model.Person;
import com.lucasezequiel.automated_tests_v3.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PersonService service;

    private Person person;

    @BeforeEach
    public void setup() {
        person = new Person("Lucas", "Ezequiel",
                "ezequiel@gmail.com", "Rio de Janeiro - RJ", "Male");
    }

    @DisplayName("Given Person Object when create Person then return saved Person")
    @Test
    void testGivenPersonObject_WhenCreatePerson_ThenReturnSavedPerson() throws Exception {
        given(service.create(any(Person.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        var response = mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(person)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }

    @DisplayName("Given list of Persons when findAll Persons then return Person list")
    @Test
    void testGivenListOfPersons_WhenFindAllPersons_ThenReturnPersonList() throws Exception {
        var listPersons = new ArrayList<Person>();
        listPersons.add(person);
        listPersons.add(new Person("Zoro", "Roronoa",
                "zoro@gmail.com", "Rio de Janeiro - RJ", "Male"));
        given(service.findAll()).willReturn(listPersons);

        var response = mockMvc.perform(get("/person"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listPersons.size())));
    }

    @DisplayName("Given personId when findById then return Person Object")
    @Test
    void Given_PersonId_WhenFindById_ThenReturnPersonObject() throws Exception {
        var personId = 1L;
        given(service.findById(personId)).willReturn(person);

        var response = mockMvc.perform(get("/person/{id}", personId));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }

    @DisplayName("Given invalid personId when findById then return not found")
    @Test
    void Given_InvalidPersonId_WhenFindById_ThenReturnNotFound() throws Exception {
        var personId = 1L;
        given(service.findById(personId)).willThrow(ResourceNotFoundException.class);

        var response = mockMvc.perform(get("/person/{id}", personId));

        response.andExpect(status().isNotFound()).andDo(print());
    }

    @DisplayName("Given updated Person when update Person then return updated Person")
    @Test
    void Given_UpdatedPerson_WhenUpdate_ThenReturnUpdatedPerson() throws Exception {
        var personId = 1L;
        var updatedPerson = new Person("Zoro", "Roronoa",
                "zoro@gmail.com", "Rio de Janeiro - RJ", "Male");
        given(service.findById(personId)).willReturn(person);
        given(service.update(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));

        var response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedPerson)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedPerson.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedPerson.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedPerson.getEmail())));
    }

    @DisplayName("Given Unexistent Person when update Person then return updated Person")
    @Test
    void testGivenUnexistentPerson_WhenUpdate_ThenReturnUpdatedPerson() throws Exception {
        var personId = 1L;
        given(service.findById(personId)).willThrow(ResourceNotFoundException.class);
        given(service.update(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(1));

        var updatedPerson = new Person("Zoro", "Roronoa",
                "zoro@gmail.com", "Rio de Janeiro - RJ", "Male");

        var response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedPerson)));

        response.andExpect(status().isNotFound()).andDo(print());
    }

    @DisplayName("Given personId when Delete then Return NotContent")
    @Test
    void testGivenPersonId_WhenDelete_thenReturnNotContent() throws Exception {
        var personId = 1L;
        willDoNothing().given(service).delete(personId);

        var response = mockMvc.perform(delete("/person/{id}", personId));

        response.andExpect(status().isNoContent()).andDo(print());
    }

}
