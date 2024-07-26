package ru.springcource.project_2_boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.springcource.project_2_boot.models.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
}
