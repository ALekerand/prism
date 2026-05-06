package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.ReferentialPutHelper;
import com.dcspa.prism.entity.TypePersonneMorale;
import com.dcspa.prism.service.TypePersonneMoraleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/type-personne-morale")
@RequiredArgsConstructor
public class TypePersonneMoraleController {

    private final TypePersonneMoraleService service;

    @GetMapping
    public ResponseEntity<List<TypePersonneMorale>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypePersonneMorale> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TypePersonneMorale> create(@RequestBody TypePersonneMorale body) {
        return ResponseEntity.status(201).body(service.save(body));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypePersonneMorale> update(@PathVariable Integer id, @RequestBody TypePersonneMorale body) {
        return ReferentialPutHelper.putPreservingAutoCode(id, body, service::findById, service::save);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
