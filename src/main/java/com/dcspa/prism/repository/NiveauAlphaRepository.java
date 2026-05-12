package com.dcspa.prism.repository;

import com.dcspa.prism.entity.NiveauAlpha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NiveauAlphaRepository extends JpaRepository<NiveauAlpha, Long> {

	List<NiveauAlpha> findByIdCentre_Id(Integer centreId);
}
