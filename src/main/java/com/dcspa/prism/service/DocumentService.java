package com.dcspa.prism.service;

import com.dcspa.prism.dto.DocumentListFilter;
import com.dcspa.prism.dto.DocumentListItem;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Document;
import com.dcspa.prism.repository.DocumentRepository;
import com.dcspa.prism.repository.spec.CentreCirconscriptionSpecifications;
import com.dcspa.prism.repository.spec.CentreListScopeSpecifications;
import com.dcspa.prism.repository.spec.DocumentSpecifications;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.pagination.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentService {

	private final DocumentRepository documentRepository;
	private final CirconscriptionResolver circonscriptionResolver;
	private final CirconscriptionWriteGuard circonscriptionWriteGuard;

	@Transactional(readOnly = true)
	public List<Document> findAllForUser(AuthUser user) {
		Specification<Document> spec = listScopeSpec(user);
		if (spec == null) {
			return documentRepository.findAll();
		}
		return documentRepository.findAll(spec);
	}

	/** Liste paginée pour le module Visites (même donnée que {@code Document}, forme aplatie). */
	@Transactional(readOnly = true)
	public Page<DocumentListItem> findAllListItems(Pageable pageable, DocumentListFilter filter, AuthUser authUser) {
		Pageable p = PageableUtils.cap(pageable);
		DocumentListFilter f = filter == null ? new DocumentListFilter() : filter;
		Specification<Document> scope = listScopeSpec(authUser);
		Specification<Document> filterSpec = DocumentSpecifications.fromFilter(f);
		Specification<Document> spec = scope == null
				? filterSpec
				: Specification.where(scope).and(filterSpec);
		return documentRepository.findAll(spec, p).map(DocumentListItemMapper::fromDocument);
	}

	@Transactional(readOnly = true)
	public Optional<Document> findByIdForUser(Integer id, AuthUser user) {
		if (id == null) {
			return Optional.empty();
		}
		Specification<Document> scope = listScopeSpec(user);
		if (scope == null) {
			return documentRepository.findById(id);
		}
		return documentRepository.findOne(
				Specification.where(scope).and((root, query, cb) -> cb.equal(root.get("id"), id)));
	}

	@Transactional
	public Document save(Document entity, AuthUser user) {
		validateRequiredFields(entity);
		assertCentreAccessible(entity.getIdCentre(), user);
		return documentRepository.save(entity);
	}

	@Transactional
	public void deleteByIdForUser(Integer id, AuthUser user) {
		Document existing = findByIdForUser(id, user)
				.orElseThrow(() -> new IllegalArgumentException("Document introuvable ou hors périmètre."));
		documentRepository.delete(existing);
	}

	public void assertCentreAccessible(Centre centre, AuthUser user) {
		if (user == null || centre == null) {
			return;
		}
		circonscriptionWriteGuard.assertCentreEntityMatchesUser(centre, user);
		if (user.hasRole("CONSEILLER")) {
			String me = user.getUsername();
			String owner = centre.getEnregistrePar();
			if (me == null || me.isBlank() || owner == null || !me.trim().equalsIgnoreCase(owner.trim())) {
				throw new IllegalArgumentException("Accès refusé : ce centre n'est pas dans votre périmètre.");
			}
		}
	}

	private Specification<Document> listScopeSpec(AuthUser user) {
		if (user == null) {
			return null;
		}
		Specification<Document> geographic = CentreCirconscriptionSpecifications.forDocument(
				circonscriptionResolver.resolve(user));
		return CentreListScopeSpecifications.forDocumentListScope(geographic, user);
	}

	private void validateRequiredFields(Document entity) {
		if (entity.getIdNatureDocument() == null) {
			throw new IllegalArgumentException("La nature du document est obligatoire.");
		}
		if (entity.getIdTypeDocument() == null) {
			throw new IllegalArgumentException("Le type de document est obligatoire.");
		}
		if (entity.getIdCentre() == null) {
			throw new IllegalArgumentException("Le centre est obligatoire pour un document.");
		}
	}
}
