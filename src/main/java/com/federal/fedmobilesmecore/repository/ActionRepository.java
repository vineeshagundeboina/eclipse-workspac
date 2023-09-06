package com.federal.fedmobilesmecore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.Action;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

	Action findByOperationId(String operationId);

	List<Action> findByOperationIdOrderByIdAsc(String operationId);
}
