package com.task.eod.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.task.eod.model.Task;

@Repository
public interface TaskRepo extends JpaRepository<Task, Integer>{
	List<Task> findByEodDate(String date);

}
