package com.App.Audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

// this class will help JPA to understand which user is logged in who is performing certain operations. And based on this info it will populate createdBY and updatedBy fields and columns

@Component("audit")
public class AuditAwareImpl implements AuditorAware<String> {

	// this method will specifically help in identifying the auditor
	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName()); // securityCOntextHolder
																										// has the
																										// information
																										// about all
																										// logged in
																										// users
	}

}
