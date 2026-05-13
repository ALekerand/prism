package com.dcspa.prism.service;

import com.dcspa.prism.dto.AppUserAdminResponse;
import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.repository.AppRoleRepository;
import com.dcspa.prism.repository.AppUserRepository;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.CiviliteRepository;
import com.dcspa.prism.repository.FonctionRepository;
import com.dcspa.prism.repository.NiveauPersonnelRepository;
import com.dcspa.prism.repository.PersonnelRepository;
import com.dcspa.prism.repository.PromoteurRepository;
import com.dcspa.prism.repository.StatutPersonnelRepository;
import com.dcspa.prism.repository.StructureFormationCertificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminListPaginationServiceTest {

	@Mock
	private AppUserRepository appUserRepository;

	@Mock
	private AppRoleRepository appRoleRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private AppUserAdminService appUserAdminService;

	@Mock
	private PersonnelRepository personnelRepository;

	@Mock
	private NiveauPersonnelRepository niveauPersonnelRepository;

	@Mock
	private FonctionRepository fonctionRepository;

	@Mock
	private CiviliteRepository civiliteRepository;

	@Mock
	private CentreRepository centreRepository;

	@Mock
	private StructureFormationCertificationRepository structureFormationCertificationRepository;

	@Mock
	private StatutPersonnelRepository statutPersonnelRepository;

	@InjectMocks
	private PersonnelAdminService personnelAdminService;

	@Mock
	private PromoteurRepository promoteurRepository;

	@InjectMocks
	private PromoteurService promoteurService;

	@Test
	void appUserAdminServiceReturnsPagedDtos() {
		AppUser u = new AppUser();
		u.setId(1);
		u.setUsername("alice");
		u.setActif(true);
		u.setRoles(Collections.emptySet());
		when(appUserRepository.searchForAdmin(isNull(), isNull(), isNull(), any(Pageable.class)))
				.thenReturn(new PageImpl<>(List.of(u)));

		Page<AppUserAdminResponse> page =
				appUserAdminService.findAllWithRoles(PageRequest.of(0, 10), null, null, null);

		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getContent().get(0).getUsername()).isEqualTo("alice");
	}

	@Test
	void deleteUserIfExists_whenAbsent_returnsFalse() {
		when(appUserRepository.findById(999)).thenReturn(Optional.empty());
		assertThat(appUserAdminService.deleteUserIfExists(999)).isFalse();
		verify(appUserRepository, never()).delete(any(AppUser.class));
	}

	@Test
	void deleteUserIfExists_whenPresent_clearsRolesThenDeletes() {
		AppUser u = new AppUser();
		u.setId(3);
		Set<AppRole> roles = new HashSet<>();
		AppRole role = new AppRole();
		role.setId(10);
		roles.add(role);
		u.setRoles(roles);
		when(appUserRepository.findById(3)).thenReturn(Optional.of(u));
		assertThat(appUserAdminService.deleteUserIfExists(3)).isTrue();
		assertThat(u.getRoles()).isEmpty();
		verify(appUserRepository).delete(u);
	}

	@Test
	void personnelAdminServiceUsesPagedRepositoryQuery() {
		when(personnelRepository.findAll(any(Specification.class), any(Pageable.class)))
				.thenReturn(new PageImpl<>(Collections.emptyList()));

		Page<?> page = personnelAdminService.listByCentre(5, null, PageRequest.of(0, 20));

		assertThat(page.getContent()).isEmpty();
		verify(personnelRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void promoteurServicePageableDelegatesToRepository() {
		Promoteur p = new Promoteur();
		p.setId(2);
		p.setLibellePromoteur("Org");
		when(promoteurRepository.findAll(any(Pageable.class)))
				.thenReturn(new PageImpl<>(List.of(p)));

		Page<Promoteur> page = promoteurService.findAll(PageRequest.of(0, 15));

		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getContent().get(0).getId()).isEqualTo(2);
	}
}
