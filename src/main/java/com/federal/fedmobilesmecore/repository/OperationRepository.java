package com.federal.fedmobilesmecore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
}
