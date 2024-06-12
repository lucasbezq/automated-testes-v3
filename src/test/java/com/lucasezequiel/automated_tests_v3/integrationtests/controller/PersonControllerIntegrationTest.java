package com.lucasezequiel.automated_tests_v3.integrationtests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasezequiel.automated_tests_v3.config.TestConfig;
import com.lucasezequiel.automated_tests_v3.integrationtests.testcontainers.AbstractIntegrationTest;
import com.lucasezequiel.automated_tests_v3.model.Person;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static com.lucasezequiel.automated_tests_v3.config.TestConfig.CONTENT_TYPE_JSON;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PersonControllerIntegrationTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static Person person;

    @BeforeAll
    public static void setup() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/person")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .build();

        person = new Person("Lucas",
                "Ezequiel",
                "ezequiel@gmail.com",
                "Rio de Janeiro - RJ",
                "Male");
    }

    @Test
    @Order(1)
    @DisplayName("Integration Test: Given Person Object when create one Person Should Return a Person Object")
    void integrationTestGivenPersonObject_WhenCreateOnePerson_ShouldReturnPersonObject() throws JsonProcessingException {
        var content = given()
                .spec(specification)
                .contentType(CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        var createdPerson = mapper.readValue(content, Person.class);
        person = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getEmail());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);
        assertEquals("Lucas", createdPerson.getFirstName());
        assertEquals("Ezequiel", createdPerson.getLastName());
        assertEquals("ezequiel@gmail.com", createdPerson.getEmail());
        assertEquals("Rio de Janeiro - RJ", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }

    @Test
    @Order(2)
    @DisplayName("Integration Test: Given Person Object when update Person should return one updated Person")
    void integrationTestGivenPersonObject_WhenUpdateOnePerson_ShouldReturnUpdatedPerson() throws JsonProcessingException {
        person.setFirstName("Zoro");
        person.setLastName("Roronoa");

        var content = given()
                .spec(specification)
                .contentType(CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        var updatedPerson = mapper.readValue(content, Person.class);
        person = updatedPerson;

        assertNotNull(updatedPerson);
        assertNotNull(updatedPerson.getId());
        assertNotNull(updatedPerson.getFirstName());
        assertNotNull(updatedPerson.getLastName());
        assertNotNull(updatedPerson.getAddress());
        assertNotNull(updatedPerson.getGender());

        assertTrue(updatedPerson.getId() > 0);
        assertEquals("Zoro", updatedPerson.getFirstName());
        assertEquals("Roronoa", updatedPerson.getLastName());
        assertEquals("Rio de Janeiro - RJ", updatedPerson.getAddress());
        assertEquals("Male", updatedPerson.getGender());
    }

    @Test
    @Order(3)
    @DisplayName("Integration Test: Given Person Object when findById Person should Return one updated Person")
    void integrationTestGivenPersonObject_WhenFindById_ShouldReturnPersonObject() throws JsonProcessingException {


        var content = given()
                .spec(specification)
                .pathParams("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        var personFound = mapper.readValue(content, Person.class);

        assertNotNull(personFound);
        assertNotNull(personFound.getId());
        assertNotNull(personFound.getFirstName());
        assertNotNull(personFound.getLastName());
        assertNotNull(personFound.getAddress());
        assertNotNull(personFound.getGender());

        assertTrue(personFound.getId() > 0);
        assertEquals("Zoro", personFound.getFirstName());
        assertEquals("Roronoa", personFound.getLastName());
        assertEquals("Rio de Janeiro - RJ", personFound.getAddress());
        assertEquals("Male", personFound.getGender());
    }

    @Test
    @Order(4)
    @DisplayName("Integration Test: When findAll should Return Person list")
    void integrationTest_WhenFindAll_ShouldReturnPersonList() throws JsonProcessingException {

        var anotherPerson = new Person(
                "Lucas",
                "Ezequiel",
                "lucas@gmail.com",
                "Rio de Janeiro - RJ",
                "Male"
        );

        given().spec(specification)
                .contentType(CONTENT_TYPE_JSON)
                .body(anotherPerson)
                .when()
                .post()
                .then()
                .statusCode(200);

        var content = given()
                .spec(specification)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<Person> personList = Arrays.asList(mapper.readValue(content, Person[].class));


        assertNotNull(personList);
        assertEquals(2, personList.size());

        var foundPerson1 = personList.get(0);
        var foundPerson2 = personList.get(1);

        assertTrue(foundPerson1.getId() > 0);
        assertEquals("Zoro", foundPerson1.getFirstName());
        assertEquals("Roronoa", foundPerson1.getLastName());
        assertEquals("Rio de Janeiro - RJ", foundPerson1.getAddress());
        assertEquals("Male", foundPerson1.getGender());

        assertTrue(foundPerson2.getId() > 0);
        assertEquals("Lucas", foundPerson2.getFirstName());
        assertEquals("Ezequiel", foundPerson2.getLastName());
        assertEquals("Rio de Janeiro - RJ", foundPerson2.getAddress());
        assertEquals("Male", foundPerson2.getGender());
    }

    @Test
    @Order(5)
    @DisplayName("Integration Test: Given Person Object when findById Person should Return one updated Person")
    void integrationTestGivenPersonObject_WhenDelete_ShouldReturnNoContent() throws JsonProcessingException {
        given()
                .spec(specification)
                .pathParams("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }
}
