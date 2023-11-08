package com.App.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.App.Model.Roles;

@Repository
public interface RolesRepo extends JpaRepository<Roles, Integer> {
	
	Roles getByRoleName(String roleName);
	
}
