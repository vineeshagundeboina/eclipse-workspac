package com.federal.fedmobilesmecore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.Signature;

@Repository
public interface SignatureRepository extends JpaRepository<Signature, Long> {
	public Signature findByIdAndStatus(Long id, String status);

//	public Signature findByOperationId(String operationId);

	public List<Signature> findByOperationId(String operationId);

	public List<Signature> findByOperationIdAndStatus(String operationId, String status);

	public Long countByOperationId(String operationId);

	public Long countByOperationIdAndStatus(String operationId, String status);

	public List<Signature> findByOperationIdAndUserIdAndUserNameAndPrefNo(String operationId, String userid,
			String username, String pref_no);

}