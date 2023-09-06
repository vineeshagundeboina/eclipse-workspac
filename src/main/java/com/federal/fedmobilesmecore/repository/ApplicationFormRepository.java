package com.federal.fedmobilesmecore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.ApplicationForm;

@Repository
public interface ApplicationFormRepository extends JpaRepository<ApplicationForm, Long> {
	
	ApplicationForm findByRefNo(String refNo);

	@Query(value = "SELECT af FROM ApplicationForm af WHERE af.status != 'deleted' AND af.status NOT IN ('verified','initiated','rejected') AND af.id = ?1")
	List<ApplicationForm> findAllById(long fid);
	
	@Query(value = "SELECT af FROM ApplicationForm af WHERE af.status != 'deleted' AND af.status NOT IN ('verified','initiated','rejected') AND af.id = ?1")
	Optional<ApplicationForm> getValidApplicationFormById(long fid);

}
