package com.federal.fedmobilesmecore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.UsersAuthTokens;

@Repository
public interface UserTokensRepository extends JpaRepository<UsersAuthTokens, Long>{

}
