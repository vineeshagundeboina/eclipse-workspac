package com.federal.fedmobilesmecore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.Signature;

/**
 * @author Sandeep Dash
 *
 */
@Repository
public interface SignatureRepository_V1 extends JpaRepository<Signature, Long> {
	public Optional<Signature> findByIdAndStatus(Long id, String status);

//	public Signature findByOperationId(String operationId);
	public List<Signature> findByOperationId(String operationId);

	public List<Signature> findByOperationIdAndStatus(String operationId, String status);

	public Long countByOperationId(String operationId);

	public Long countByOperationIdAndStatus(String operationId, String status);

	public List<Signature> findByOperationIdAndUserIdAndUserNameAndPrefNo(String operationId, String userid,
			String username, String pref_no);
}