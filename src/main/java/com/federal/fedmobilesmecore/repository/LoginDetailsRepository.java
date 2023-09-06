package com.federal.fedmobilesmecore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.LoginDetails;

@Repository
public interface LoginDetailsRepository extends JpaRepository<LoginDetails, Long> {

}
