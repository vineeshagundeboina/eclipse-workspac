package com.federal.fedmobilesmecore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.AccountNoIdentifier;

@Repository
public interface AccountNoIdentifierRepository extends JpaRepository<AccountNoIdentifier, Long> {
	public AccountNoIdentifier findByAccNo(String accNo);
}
