package com.task.eod.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.task.eod.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer>{

}
