package com.dcspa.prism.controller;

import com.dcspa.prism.dto.PersonnelAdminRequest;
import com.dcspa.prism.dto.PersonnelAdminResponse;
import com.dcspa.prism.service.PersonnelAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/personnel")
@RequiredArgsConstructor
public class PersonnelAdminController {
    private final PersonnelAdminService personnelAdminService;

    @GetMapping
    public ResponseEntity<List<PersonnelAdminResponse>> listByCentre(@RequestParam Integer centreId) {
        return ResponseEntity.ok(personnelAdminService.listByCentre(centreId));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(@RequestParam Integer centreId) {
        long total = personnelAdminService.countByCentre(centreId);
        return ResponseEntity.ok(Map.of(
                "centreId", centreId,
                "total", total
        ));
    }

    @PostMapping
    public ResponseEntity<PersonnelAdminResponse> create(@RequestBody PersonnelAdminRequest request) {
        return ResponseEntity.status(201).body(personnelAdminService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonnelAdminResponse> update(
            @PathVariable Integer id,
            @RequestBody PersonnelAdminRequest request
    ) {
        return ResponseEntity.ok(personnelAdminService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        personnelAdminService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

