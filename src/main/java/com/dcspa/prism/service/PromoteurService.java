package com.dcspa.prism.service;

import com.dcspa.prism.dto.PromoteurListFilter;
import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.repository.PromoteurRepository;
import com.dcspa.prism.repository.spec.PromoteurSpecifications;
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
public class PromoteurService {

	private final PromoteurRepository repository;

	@Transactional(readOnly = true)
	public List<Promoteur> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Page<Promoteur> findAll(Pageable pageable) {
		return repository.findAll(PageableUtils.cap(pageable));
	}

	@Transactional(readOnly = true)
	public Page<Promoteur> findAll(PromoteurListFilter filter, Pageable pageable) {
		Specification<Promoteur> spec = PromoteurSpecifications.byFilter(filter);
		return repository.findAll(spec, PageableUtils.cap(pageable));
	}

	@Transactional(readOnly = true)
	public Optional<Promoteur> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	@Transactional
	public Promoteur save(Promoteur entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id); }
}
