package com.lucasezequiel.automated_tests_v3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.ResultActions;

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
    void setup() {
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
}
