package com.dcspa.prism.repository;

import com.dcspa.prism.dto.CentreTypeListItem;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.repositorybase.BaseRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlphaRepository extends BaseRepository<Alpha, Integer>, JpaSpecificationExecutor<Alpha> {

	@EntityGraph(attributePaths = {"idCompagne", "idCategorieCentreAlpha", "idTypeAlpha", "idRegimeAlpha"})
	@Query("select a from Alpha a where a.id = :id")
	Optional<Alpha> findDetailWithRefsById(@Param("id") Integer id);

	/**
	 * Liste légère sans entité Hibernate complète (même DTO que {@code CentreTypeListItemMapper#fromAlpha}).
	 * Utilisé quand aucun filtre n’est appliqué, pour accélérer les écrans qui chargent beaucoup de centres.
	 */
	@Query("""
			select new com.dcspa.prism.dto.CentreTypeListItem(
			  a.id, a.codeCentre, a.codeAlpha, a.libelleAlpha,
			  a.idLocalite, a.idIep, a.idNaturecentre, a.idPeriodicite, a.idAutoriteAutorisation,
			  a.autorisation, a.estElectrifie, a.aDeLeau, a.nombreVisite,
			  a.totalApprenants, a.totalHommes, a.totalFemmes, a.latitudeGps, a.longitudeGps,
			  a.gpsValide, a.structurePartenaire, a.nomPartenaire,
			  a.localisationCentre, a.nomMilieuImplentation, a.encadreurNonMena, a.encadrerParMena, a.idPromoteur,
			  coalesce(a.centre.actif, true))
			from Alpha a
			""")
	Page<CentreTypeListItem> findAllAsListItems(Pageable pageable);
}
