package com.federal.fedmobilesmecore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.UserMobileChanges;

@Repository
public interface UserMobileChangesRepo extends JpaRepository<UserMobileChanges, Long>{

}
