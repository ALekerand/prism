package com.dcspa.prism.repository;

import com.dcspa.prism.entity.LocaliteDImplantation;
import com.dcspa.prism.repositorybase.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocaliteDImplantationRepository extends BaseRepository<LocaliteDImplantation, Integer> {

	@Query("""
			select distinct l
			from LocaliteDImplantation l
			left join fetch l.idSousPrefecture
			left join fetch l.idMilieuImplentation
			left join fetch l.idCommune
			""")
	List<LocaliteDImplantation> findAllWithReferentials();
}
