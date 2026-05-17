package com.dcspa.prism.controller;

import com.dcspa.prism.dto.PersonnelAdminRequest;
import com.dcspa.prism.dto.PersonnelAdminResponse;
import com.dcspa.prism.dto.PersonnelListFilter;
import com.dcspa.prism.service.PersonnelAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/personnel")
@RequiredArgsConstructor
public class PersonnelAdminController {
    private final PersonnelAdminService personnelAdminService;

    @GetMapping
    public ResponseEntity<Page<PersonnelAdminResponse>> listByCentre(
            @RequestParam Integer centreId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable,
            @ModelAttribute PersonnelListFilter filter) {
        return ResponseEntity.ok(personnelAdminService.listByCentre(centreId, filter, pageable));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(
            @RequestParam(required = false) Integer centreId,
            @RequestParam(required = false) String centreType) {
        if (centreId != null) {
            return ResponseEntity.ok(personnelAdminService.buildCentreDashboard(centreId));
        }
        if (centreType != null && !centreType.isBlank()) {
            return ResponseEntity.ok(personnelAdminService.buildTypeSummary(centreType));
        }
        return ResponseEntity.badRequest().body(Map.of(
                "message", "Indiquer centreId ou centreType (ALPHA, CEC, CP, SIE)."));
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

