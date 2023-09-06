package com.federal.fedmobilesmecore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.FundTransferLog;
@Repository
public interface FundTransferLogRepository extends JpaRepository<FundTransferLog, Long> {

}
