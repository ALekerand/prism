package com.dcspa.prism.repository;

import com.dcspa.prism.entity.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonnelRepository extends JpaRepository<Personnel, Long> {

    List<Personnel> findByIdCentre_Id(Integer centreId);

    long countByIdCentre_Id(Integer centreId);
}
