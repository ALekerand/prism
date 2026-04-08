package com.dcspa.prism.repository;

import com.dcspa.prism.entity.Personnel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PersonnelRepository extends JpaRepository<Personnel, Long>, JpaSpecificationExecutor<Personnel> {

    List<Personnel> findByIdCentre_Id(Integer centreId);

    Page<Personnel> findByIdCentre_Id(Integer centreId, Pageable pageable);

    long countByIdCentre_Id(Integer centreId);
}
