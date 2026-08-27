package com.dcspa.prism.service;

import com.dcspa.prism.dto.PromoteurListFilter;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.PromoteurRepository;
import com.dcspa.prism.repository.spec.CentreCirconscriptionSpecifications;
import com.dcspa.prism.repository.spec.PromoteurSpecifications;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.circonscription.CirconscriptionAttachement;
import com.dcspa.prism.service.pagination.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromoteurService {

	private final PromoteurRepository repository;
	private final CentreRepository centreRepository;
	private final CirconscriptionResolver circonscriptionResolver;

	@Transactional(readOnly = true)
	public List<Promoteur> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Page<Promoteur> findAll(Pageable pageable) {
		return repository.findAll(PageableUtils.cap(pageable));
	}

	@Transactional(readOnly = true)
	public Page<Promoteur> findAll(PromoteurListFilter filter, Pageable pageable, AuthUser authUser) {
		Specification<Promoteur> spec = PromoteurSpecifications.byFilter(filter);
		spec = andNullable(spec, scopeForPromoteurList(authUser));
		return repository.findAll(spec, PageableUtils.cap(pageable));
	}

	@Transactional(readOnly = true)
	public Page<Promoteur> findAll(PromoteurListFilter filter, Pageable pageable) {
		return findAll(filter, pageable, null);
	}

	@Transactional(readOnly = true)
	public Optional<Promoteur> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	@Transactional
	public Promoteur save(Promoteur entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id); }

	/**
	 * Conseiller : promoteurs rattachés aux centres de sa circonscription (périmètre IEP).
	 * Les rôles à vue nationale ne sont pas filtrés.
	 */
	private Specification<Promoteur> scopeForPromoteurList(AuthUser authUser) {
		if (authUser == null || !authUser.hasRole("CONSEILLER") || circonscriptionResolver.isNationalView(authUser)) {
			return null;
		}
		CirconscriptionAttachement att = circonscriptionResolver.resolve(authUser);
		Specification<Centre> centreScope = CentreCirconscriptionSpecifications.forCentreStats(att);
		if (centreScope == null) {
			return null;
		}
		List<Centre> centres = centreRepository.findAll(centreScope);
		Set<Integer> promoteurIds = centres.stream()
				.map(Centre::getIdPromoteur)
				.filter(Objects::nonNull)
				.map(Promoteur::getId)
				.collect(Collectors.toSet());
		if (promoteurIds.isEmpty()) {
			return PromoteurSpecifications.noMatch();
		}
		return PromoteurSpecifications.idIn(promoteurIds);
	}

	private static Specification<Promoteur> andNullable(Specification<Promoteur> base, Specification<Promoteur> extra) {
		if (extra == null) {
			return base;
		}
		return base == null ? extra : base.and(extra);
	}
}
