package com.federal.fedmobilesmecore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.ManagedAccount;
import com.federal.fedmobilesmecore.dto.User;

@Repository
public interface ManagedAccountRepo extends JpaRepository<ManagedAccount, Long> {

	List<ManagedAccount> findByUserOrderByUpdatedAtDesc(User user);

	List<ManagedAccount> findAllByOrderByUpdatedAtDesc();

}
