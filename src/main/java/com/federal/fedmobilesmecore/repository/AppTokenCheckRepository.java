package com.federal.fedmobilesmecore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.ApplicationForm;
import com.federal.fedmobilesmecore.dto.PasswordHistory;
import com.federal.fedmobilesmecore.dto.User;

@Repository
public interface AppTokenCheckRepository extends JpaRepository<PasswordHistory, String> {
	
	List<PasswordHistory> findFirst05ByUserIdOrderByIdDesc(long userid);
	
	//findFirst10ByUsernameOrderByIdDesc
	
}
