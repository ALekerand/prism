package com.dcspa.prism.service;

import com.dcspa.prism.entity.Region;
import com.dcspa.prism.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository repository;

    @Transactional(readOnly = true)
    public List<Region> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Region> findById(Integer id) {
        return id == null ? Optional.empty() : repository.findById(id);
    }

    @Transactional
    public Region save(Region entity) {
        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (id != null) {
            repository.deleteById(id);
        }
    }
}
