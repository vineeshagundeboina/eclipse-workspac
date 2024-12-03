package com.sra.assetmanagement.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sra.assetmanagement.common.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Long>{

}
