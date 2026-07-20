package com.dcspa.prism.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dcspa.prism.dto.LiaisonCatalogSyncRequest;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Difficulte;
import com.dcspa.prism.entity.DifficulteAlpha;
import com.dcspa.prism.entity.SupportDidactique;
import com.dcspa.prism.entity.SupportDidactiqueAlpha;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.CompetenceCentreRepository;
import com.dcspa.prism.repository.CompetenceRepository;
import com.dcspa.prism.repository.DesignationRepository;
import com.dcspa.prism.repository.DifficulteAlphaRepository;
import com.dcspa.prism.repository.DifficulteRepository;
import com.dcspa.prism.repository.ImpactAlphaRepository;
import com.dcspa.prism.repository.ImpactRepository;
import com.dcspa.prism.repository.InfrastructureCentreRepository;
import com.dcspa.prism.repository.InfrastructureRepository;
import com.dcspa.prism.repository.LangueApprentissageRepository;
import com.dcspa.prism.repository.MaterielAlphaRepository;
import com.dcspa.prism.repository.MaterielsPedagogiqueRepository;
import com.dcspa.prism.repository.RessourceFinanciereMaterielRepository;
import com.dcspa.prism.repository.SupportDidactiqueAlphaRepository;
import com.dcspa.prism.repository.SupportDidactiqueRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Métier dossier centre : les syncs acceptent tout centre (CEC/CP/SIE/Alpha),
 * plus le sync supports didactiques.
 */
@ExtendWith(MockitoExtension.class)
class CentreLiaisonSyncServiceAllTypesTest {

	@Mock private CentreRepository centreRepository;
	@Mock private DifficulteRepository difficulteRepository;
	@Mock private DifficulteAlphaRepository difficulteAlphaRepository;
	@Mock private ImpactRepository impactRepository;
	@Mock private ImpactAlphaRepository impactAlphaRepository;
	@Mock private CompetenceRepository competenceRepository;
	@Mock private CompetenceCentreRepository competenceCentreRepository;
	@Mock private InfrastructureRepository infrastructureRepository;
	@Mock private InfrastructureCentreRepository infrastructureCentreRepository;
	@Mock private DesignationRepository designationRepository;
	@Mock private RessourceFinanciereMaterielRepository ressourceFinanciereMaterielRepository;
	@Mock private MaterielsPedagogiqueRepository materielsPedagogiqueRepository;
	@Mock private MaterielAlphaRepository materielAlphaRepository;
	@Mock private SupportDidactiqueRepository supportDidactiqueRepository;
	@Mock private SupportDidactiqueAlphaRepository supportDidactiqueAlphaRepository;
	@Mock private LangueApprentissageRepository langueApprentissageRepository;

	@InjectMocks
	private CentreLiaisonSyncService service;

	private Centre cecCentre;
	private Difficulte difficulte;

	@BeforeEach
	void setUp() {
		cecCentre = new Centre();
		cecCentre.setId(42);
		cecCentre.setCodeCentre("CEC-42");
		difficulte = new Difficulte();
		difficulte.setId(7);
	}

	@Test
	void syncDifficulteAccepteCentreNonAlpha() {
		when(centreRepository.findById(42)).thenReturn(Optional.of(cecCentre));
		when(difficulteRepository.findById(7)).thenReturn(Optional.of(difficulte));
		when(difficulteAlphaRepository.save(any(DifficulteAlpha.class))).thenAnswer(inv -> inv.getArgument(0));

		LiaisonCatalogSyncRequest req = new LiaisonCatalogSyncRequest();
		req.setIdCentre(42);
		req.setCreateCatalogIds(List.of(7));

		assertDoesNotThrow(() -> service.syncDifficulteAlpha(req));
		verify(difficulteAlphaRepository).save(any(DifficulteAlpha.class));
		verify(centreRepository).findById(42);
	}

	@Test
	void syncDifficulteRejetteCentreInconnu() {
		when(centreRepository.findById(99)).thenReturn(Optional.empty());
		LiaisonCatalogSyncRequest req = new LiaisonCatalogSyncRequest();
		req.setIdCentre(99);
		req.setCreateCatalogIds(List.of(7));

		assertThrows(IllegalArgumentException.class, () -> service.syncDifficulteAlpha(req));
		verify(difficulteAlphaRepository, never()).save(any());
	}

	@Test
	void syncSupportDidactiqueCreeLiaison() {
		when(centreRepository.findById(42)).thenReturn(Optional.of(cecCentre));
		SupportDidactique catalog = new SupportDidactique();
		catalog.setId(3);
		when(supportDidactiqueRepository.findById(3)).thenReturn(Optional.of(catalog));
		when(supportDidactiqueAlphaRepository.save(any(SupportDidactiqueAlpha.class)))
				.thenAnswer(inv -> inv.getArgument(0));

		LiaisonCatalogSyncRequest req = new LiaisonCatalogSyncRequest();
		req.setIdCentre(42);
		req.setCreateCatalogIds(List.of(3));

		assertDoesNotThrow(() -> service.syncSupportDidactiqueAlpha(req));
		verify(supportDidactiqueAlphaRepository).save(any(SupportDidactiqueAlpha.class));
	}

	@Test
	void syncSansIdCentreEchoue() {
		LiaisonCatalogSyncRequest req = new LiaisonCatalogSyncRequest();
		req.setCreateCatalogIds(List.of(1));
		assertThrows(IllegalArgumentException.class, () -> service.syncSupportDidactiqueAlpha(req));
		verify(centreRepository, never()).findById(anyInt());
	}
}
