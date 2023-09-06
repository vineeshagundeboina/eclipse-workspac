/**
 * 
 */
package com.federal.fedmobilesmecore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.Ivr;

/**
 * @author Vikas
 *
 */
@Repository
public interface IVRRepo extends JpaRepository<Ivr, Long> {

	List<Ivr> findByMobileAndOtpAndAppHash(String mobile, String otp, String apphash);
	
	

}
