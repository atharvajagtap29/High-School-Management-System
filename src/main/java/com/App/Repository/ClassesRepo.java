package com.App.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.App.Model.Classes;

@Repository
public interface ClassesRepo extends JpaRepository<Classes, Integer> {
	
}
