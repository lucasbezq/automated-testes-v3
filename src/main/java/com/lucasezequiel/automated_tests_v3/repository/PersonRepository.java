package com.lucasezequiel.automated_tests_v3.repository;

import com.lucasezequiel.automated_tests_v3.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
