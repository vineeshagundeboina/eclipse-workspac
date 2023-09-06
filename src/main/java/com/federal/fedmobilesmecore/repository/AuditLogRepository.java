package com.federal.fedmobilesmecore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>{

}
