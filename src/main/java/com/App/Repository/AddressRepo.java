package com.App.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.App.Model.Address;

@Repository
public interface AddressRepo extends JpaRepository<Address, Integer> {
	// when you want to leverage all the features or methods of Spring Data JPA, use
	// JPA repository, rather than using specific CRUD or Pagenation repository
}
