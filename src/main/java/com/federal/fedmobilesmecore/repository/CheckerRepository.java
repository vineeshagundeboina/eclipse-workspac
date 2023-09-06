package com.federal.fedmobilesmecore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.Checker;

@Repository
public interface CheckerRepository extends JpaRepository<Checker, Long> {

	Optional<Checker> findBySignatureIdAndUserId(String signatureId, String userId);

}
