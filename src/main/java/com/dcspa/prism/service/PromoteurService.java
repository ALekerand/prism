package com.dcspa.prism.service;

import com.dcspa.prism.dto.PromoteurListFilter;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.PromoteurRepository;
import com.dcspa.prism.repository.spec.CentreCirconscriptionSpecifications;
import com.dcspa.prism.repository.spec.CentreListScopeSpecifications;
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
	public List<Promoteur> findAll() {
		return repository.findAll();
	}

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

	@Transactional(readOnly = true)
	public Optional<Promoteur> findByIdInListScope(Integer id, AuthUser authUser) {
		if (id == null) {
			return Optional.empty();
		}
		Optional<Promoteur> found = repository.findById(id);
		if (found.isEmpty()) {
			return found;
		}
		Specification<Promoteur> scope = scopeForPromoteurList(authUser);
		if (scope == null) {
			return found;
		}
		Specification<Promoteur> idEq = (root, query, cb) -> cb.equal(root.get("id"), id);
		return repository.count(Specification.where(scope).and(idEq)) > 0 ? found : Optional.empty();
	}

	@Transactional
	public Promoteur save(Promoteur entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id); }

	/**
	 * Conseiller : promoteurs liés aux centres de son périmètre (liste administration).
	 * Non appliqué à {@code GET /api/promoteur} (wizard : réutilisation inter-zones).
	 */
	private Specification<Promoteur> scopeForPromoteurList(AuthUser authUser) {
		if (authUser == null || !authUser.hasRole("CONSEILLER") || circonscriptionResolver.isNationalView(authUser)) {
			return null;
		}
		CirconscriptionAttachement att = circonscriptionResolver.resolve(authUser);
		Specification<Centre> centreScope = CentreCirconscriptionSpecifications.forCentreStats(att);
		Specification<Centre> ownScope = CentreListScopeSpecifications.forCentreOwnedBy(authUser);
		Specification<Centre> combinedCentreScope = ownScope == null
				? centreScope
				: centreScope == null
				? ownScope
				: Specification.where(centreScope).and(ownScope);
		if (combinedCentreScope == null) {
			return null;
		}
		List<Centre> centres = centreRepository.findAll(combinedCentreScope);
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
