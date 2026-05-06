package com.dcspa.prism.service;

import com.dcspa.prism.entity.Personnemorale;
import com.dcspa.prism.repository.PersonnemoraleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonnemoraleService {

	private final PersonnemoraleRepository repository;

	@Transactional(readOnly = true)
	public List<Personnemorale> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Personnemorale> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	@Transactional
	public Personnemorale save(Personnemorale entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id); }
}
