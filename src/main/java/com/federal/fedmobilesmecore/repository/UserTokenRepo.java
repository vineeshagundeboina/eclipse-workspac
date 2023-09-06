package com.federal.fedmobilesmecore.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.dto.UserToken;

@Repository
public interface UserTokenRepo extends JpaRepository<UserToken, Long> {
	Optional<UserToken> findByTypeAndIdAndActiveAndTokenAndExpiredAtGreaterThan(String type, long id, BigDecimal active,
			String token, Timestamp timestamp);

	boolean existsByTypeAndUserAndActiveAndTokenAndExpiredAtGreaterThan(String string, User user, BigDecimal one,
			String activation_token, Timestamp timestamp);

	Optional<UserToken> findByTokenAndActiveAndTypeIn(String token, BigDecimal active, List<String> values);

	Optional<UserToken> findByTypeInAndActiveAndTokenAndExpiredAtGreaterThan(List<String> values, BigDecimal active,
			String token, Timestamp timestamp);
	
	Optional<UserToken> findByTypeAndUserAndActiveAndTokenAndExpiredAtGreaterThan(String string, User user, BigDecimal one,
			String activation_token, Timestamp timestamp);
	
	List<UserToken> findByTypeAndUserAndActiveAndExpiredAtGreaterThan(String string, User user, BigDecimal one, Timestamp timestamp);
}
