package com.federal.fedmobilesmecore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.SimBinding;

/**
 * @author Debasish_Splenta
 *
 */
@Repository
public interface SimBindingRepository extends JpaRepository<SimBinding, Long> {
	public SimBinding findByMobileAndSimHash(String mobileNumber, String simHash);

	public List<SimBinding> findByMobileAndSimHashOrderByUpdatedAt(String mobileNumber, String simHash);

	public SimBinding findByMobile(String mobileNumber);
}
