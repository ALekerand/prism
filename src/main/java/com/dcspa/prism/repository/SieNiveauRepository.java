package com.dcspa.prism.repository;

import com.dcspa.prism.entity.SieNiveau;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SieNiveauRepository extends JpaRepository<SieNiveau, Long> {

	List<SieNiveau> findByIdCentre_Id(Integer centreId);
}
