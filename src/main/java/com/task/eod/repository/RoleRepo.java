package com.task.eod.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.task.eod.model.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer>{

}