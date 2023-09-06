package com.federal.fedmobilesmecore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.Maker;

@Repository
public interface MakerRepository extends JpaRepository<Maker, Long> {

}
