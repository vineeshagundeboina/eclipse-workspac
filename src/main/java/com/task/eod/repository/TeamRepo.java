package com.task.eod.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.task.eod.model.Team;

@Repository
public interface TeamRepo extends JpaRepository<Team, Integer> {

}