package com.App.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.App.Model.Person;

@Repository
public interface PersonRepo extends JpaRepository<Person, Integer> {
	// its a derived query method
	Person readByEmail(String email);
}
