package com.federal.fedmobilesmecore.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.UserPin;

@Repository
public interface UserPinRepo extends JpaRepository<UserPin, Long> {

	List<UserPin> findByUserIdAndTypeAndPin(BigDecimal user_id, String type, String pin);

	Optional<UserPin> findByActiveAndPinAndTypeInAndExpiredAtGreaterThan(BigDecimal isActive, String pin,
			List<String> type, Timestamp now);

}
