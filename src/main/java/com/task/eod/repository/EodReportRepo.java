package com.task.eod.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.task.eod.model.EodReport;

@Repository
public interface EodReportRepo extends JpaRepository<EodReport, Integer> {

}
