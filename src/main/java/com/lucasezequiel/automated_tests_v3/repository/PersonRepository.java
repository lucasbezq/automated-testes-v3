package com.lucasezequiel.automated_tests_v3.repository;

import com.lucasezequiel.automated_tests_v3.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByEmail(String email);

    @Query("SELECT p FROM Person p WHERE p.firstName =:firstName AND p.lastName =:lastName")
    Person findByJPQLNamedParameters(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query(value = "select * from person p where p.first_name =:firstName and p.last_name =:lastName", nativeQuery = true)
    Person findByNativeSQL(@Param("firstName") String firstName, @Param("lastName") String lastName);

}
