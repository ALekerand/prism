package com.dcspa.prism.service;

import com.dcspa.prism.dto.PersonnelAdminRequest;
import com.dcspa.prism.dto.PersonnelAdminResponse;
import com.dcspa.prism.dto.PersonnelListFilter;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Civilite;
import com.dcspa.prism.entity.Diplome;
import com.dcspa.prism.entity.DiplomePersonnel;
import com.dcspa.prism.entity.Fonction;
import com.dcspa.prism.entity.NiveauPersonnel;
import com.dcspa.prism.entity.Personnel;
import com.dcspa.prism.entity.StatutPersonnel;
import com.dcspa.prism.entity.StructureFormationCertification;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.CiviliteRepository;
import com.dcspa.prism.repository.DiplomePersonnelRepository;
import com.dcspa.prism.repository.DiplomeRepository;
import com.dcspa.prism.repository.FonctionRepository;
import com.dcspa.prism.repository.NiveauPersonnelRepository;
import com.dcspa.prism.repository.PersonnelRepository;
import com.dcspa.prism.repository.StatutPersonnelRepository;
import com.dcspa.prism.repository.StructureFormationCertificationRepository;
import com.dcspa.prism.repository.spec.CentreCirconscriptionSpecifications;
import com.dcspa.prism.repository.spec.PersonnelSpecifications;
import com.dcspa.prism.service.circonscription.CirconscriptionAttachement;
import com.dcspa.prism.service.pagination.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonnelAdminService {
    private final PersonnelRepository personnelRepository;
    private final NiveauPersonnelRepository niveauPersonnelRepository;
    private final FonctionRepository fonctionRepository;
    private final CiviliteRepository civiliteRepository;
    private final CentreRepository centreRepository;
    private final StructureFormationCertificationRepository structureFormationCertificationRepository;
    private final DiplomePersonnelRepository diplomePersonnelRepository;
    private final DiplomeRepository diplomeRepository;
    private final StatutPersonnelRepository statutPersonnelRepository;

    @Transactional(readOnly = true)
    public Page<PersonnelAdminResponse> listByCentre(Integer centreId, PersonnelListFilter filter, Pageable pageable) {
        Pageable p = PageableUtils.cap(pageable);
        Specification<Personnel> spec = PersonnelSpecifications.byCentreAndFilter(centreId, filter);
        return personnelRepository.findAll(spec, p).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public long countByCentre(Integer centreId) {
        return personnelRepository.countByIdCentre_Id(centreId);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> buildCentreDashboard(Integer centreId) {
        Centre centre = centreRepository.findById(centreId)
                .orElseThrow(() -> new IllegalArgumentException("Centre introuvable: " + centreId));
        List<Personnel> personnel = personnelRepository.findByIdCentre_Id(centreId);

        long certified = personnel.stream().filter(p -> Boolean.TRUE.equals(p.getCertifierPersonnel())).count();
        long hommes = personnel.stream().filter(p -> "M".equalsIgnoreCase(String.valueOf(p.getSexePersonnel()).trim())).count();
        long femmes = personnel.stream()
                .filter(p -> "F".equalsIgnoreCase(String.valueOf(p.getSexePersonnel()).trim()))
                .count();

        Map<String, Long> parFonction = personnel.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getIdFonction() != null && p.getIdFonction().getLibelleFonction() != null
                                ? p.getIdFonction().getLibelleFonction().trim()
                                : (p.getIdFonction() != null ? "Fonction #" + p.getIdFonction().getId() : "—"),
                        Collectors.counting()));

        String topFonction = parFonction.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("—");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("scope", "CENTRE");
        payload.put("centreId", centreId);
        payload.put("centreCode", centre.getCodeCentre());
        payload.put("centreType", centreTypeFromCode(centre.getCodeCentre()));
        payload.put("total", personnel.size());
        payload.put("certifiedTotal", certified);
        payload.put("hommesTotal", hommes);
        payload.put("femmesTotal", femmes);
        payload.put("fonctionsDistinctes", parFonction.size());
        payload.put("topFonctionLabel", topFonction);
        payload.put("topFonctionCount", parFonction.getOrDefault(topFonction, 0L));
        return payload;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> buildTypeSummary(String centreType) {
        return buildTypeSummary(centreType, null);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> buildTypeSummary(String centreType, CirconscriptionAttachement att) {
        String normalized = centreType == null ? "" : centreType.trim().toUpperCase();
        Specification<Centre> scope = CentreCirconscriptionSpecifications.forCentreStats(att);
        List<Centre> centres = centreRepository.findAll(scope);
        List<Integer> centreIds = centres.stream()
                .filter(c -> matchesCentreType(c.getCodeCentre(), normalized))
                .filter(CentreCirconscriptionSpecifications::isActif)
                .map(Centre::getId)
                .toList();

        long personnelTotal = 0L;
        if (!centreIds.isEmpty()) {
            personnelTotal = personnelRepository.count((root, query, cb) -> root.get("idCentre").get("id").in(centreIds));
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("scope", "TYPE");
        payload.put("centreType", normalized);
        payload.put("centreTypeLabel", centreTypeLabel(normalized));
        payload.put("centresCount", centreIds.size());
        payload.put("personnelTotal", personnelTotal);
        return payload;
    }

    private static boolean matchesCentreType(String codeCentre, String centreType) {
        if (centreType == null || centreType.isBlank()) {
            return true;
        }
        return centreTypeFromCode(codeCentre).equals(centreType);
    }

    private static String centreTypeFromCode(String code) {
        if (code == null || code.isBlank()) {
            return "AUTRE";
        }
        String c = code.toUpperCase();
        if (c.contains("ALP") || c.contains("ALPHA")) {
            return "ALPHA";
        }
        if (c.contains("CEC")) {
            return "CEC";
        }
        if (c.contains("SIE")) {
            return "SIE";
        }
        if (c.contains("CP")) {
            return "CP";
        }
        return "AUTRE";
    }

    private static String centreTypeLabel(String centreType) {
        return switch (centreType) {
            case "ALPHA" -> "Centre Alpha";
            case "CEC" -> "Centre CEC";
            case "CP" -> "Centre CP";
            case "SIE" -> "Centre SIE";
            default -> "Tous les types";
        };
    }

    @Transactional
    public PersonnelAdminResponse create(PersonnelAdminRequest r) {
        Personnel p = new Personnel();
        applyRequestToEntity(p, r);
        Personnel saved = personnelRepository.save(p);
        syncDiplome(saved, r.getIdDiplomeId(), r.getLibelleAutreDiplome());
        return toDto(saved);
    }

    @Transactional
    public PersonnelAdminResponse update(Integer id, PersonnelAdminRequest r) {
        Personnel p = personnelRepository.findById(id.longValue())
                .orElseThrow(() -> new IllegalArgumentException("Personnel introuvable: " + id));
        applyRequestToEntity(p, r);
        Personnel saved = personnelRepository.save(p);
        syncDiplome(saved, r.getIdDiplomeId(), r.getLibelleAutreDiplome());
        return toDto(saved);
    }

    @Transactional
    public void delete(Integer id) {
        if (id == null) return;
        personnelRepository.deleteById(id.longValue());
    }

    private void applyRequestToEntity(Personnel p, PersonnelAdminRequest r) {
        NiveauPersonnel niveau = niveauPersonnelRepository.findById(toLongRequired(r.getIdNiveauPersonnelId(), "niveauPersonnel"))
                .orElseThrow(() -> new IllegalArgumentException("NiveauPersonnel introuvable: " + r.getIdNiveauPersonnelId()));
        Fonction fonction = fonctionRepository.findById(r.getIdFonctionId())
                .orElseThrow(() -> new IllegalArgumentException("Fonction introuvable: " + r.getIdFonctionId()));
        Civilite civilite = civiliteRepository.findById(r.getIdCiviliteId())
                .orElseThrow(() -> new IllegalArgumentException("Civilite introuvable: " + r.getIdCiviliteId()));
        Centre centre = centreRepository.findById(r.getIdCentreId())
                .orElseThrow(() -> new IllegalArgumentException("Centre introuvable: " + r.getIdCentreId()));
        StatutPersonnel statut = statutPersonnelRepository.findById(toLongRequired(r.getIdStatutPersonnelId(), "statutPersonnel"))
                .orElseThrow(() -> new IllegalArgumentException("StatutPersonnel introuvable: " + r.getIdStatutPersonnelId()));

        p.setIdNiveauPersonnel(niveau);
        p.setIdFonction(fonction);
        p.setIdCivilite(civilite);
        p.setIdCentre(centre);
        p.setIdStatutPersonnel(statut);

        if (r.getIdStructureFormationCertificationId() != null) {
            StructureFormationCertification sfc = structureFormationCertificationRepository
                    .findById(r.getIdStructureFormationCertificationId().longValue())
                    .orElseThrow(() -> new IllegalArgumentException("StructureFormationCertification introuvable: " + r.getIdStructureFormationCertificationId()));
            p.setIdStructureFormationCertification(sfc);
        } else {
            p.setIdStructureFormationCertification(null);
        }

        p.setCodePersonnel(r.getCodePersonnel());
        p.setCertifierPersonnel(r.getCertifierPersonnel());
        p.setNomPersonnel(r.getNomPersonnel());
        p.setPrenomsPersonnel(r.getPrenomsPersonnel());
        p.setAnneExpePersonnel(r.getAnneExpePersonnel());
        p.setSexePersonnel(r.getSexePersonnel());
        p.setDateNaissance(r.getDateNaissance());
        p.setAncienneFonctPromoPesonnel(r.getAncienneFonctPromoPesonnel());
        p.setContactPersonnel(r.getContactPersonnel());
        p.setBoitePostalePersonnel(r.getBoitePostalePersonnel());
        p.setEmailPersonnel(r.getEmailPersonnel());
        p.setDenominationPersonnel(r.getDenominationPersonnel());
        p.setNomDuPrgramme(r.getNomDuPrgramme());
        p.setNomRepresentantLegalSturcture(r.getNomRepresentantLegalSturcture());
    }

    private void syncDiplome(Personnel personnel, Integer idDiplomeId, String libelleAutreDiplome) {
        if (personnel == null || personnel.getId() == null) {
            return;
        }
        diplomePersonnelRepository.deleteByIdPersonnel_Id(personnel.getId());
        if (idDiplomeId == null) {
            return;
        }
        Diplome diplome = diplomeRepository.findById(idDiplomeId)
                .orElseThrow(() -> new IllegalArgumentException("Diplome introuvable: " + idDiplomeId));
        DiplomePersonnel link = new DiplomePersonnel();
        link.setIdPersonnel(personnel);
        link.setIdDiplome(diplome);
        link.setLibelleAutreDiplome(trimToNull(libelleAutreDiplome));
        diplomePersonnelRepository.save(link);
    }

    private Integer resolveDiplomeId(Personnel p) {
        if (p == null || p.getId() == null) {
            return null;
        }
        return diplomePersonnelRepository.findByIdPersonnel_Id(p.getId()).stream()
                .findFirst()
                .map(dp -> dp.getIdDiplome() != null ? dp.getIdDiplome().getId() : null)
                .orElse(null);
    }

    private String resolveLibelleAutreDiplome(Personnel p) {
        if (p == null || p.getId() == null) {
            return null;
        }
        return diplomePersonnelRepository.findByIdPersonnel_Id(p.getId()).stream()
                .findFirst()
                .map(dp -> dp.getLibelleAutreDiplome())
                .orElse(null);
    }

    private PersonnelAdminResponse toDto(Personnel p) {
        return PersonnelAdminResponse.builder()
                .id(p.getId())
                .diplomeId(resolveDiplomeId(p))
                .libelleAutreDiplome(resolveLibelleAutreDiplome(p))
                .niveauPersonnelId(p.getIdNiveauPersonnel() != null ? p.getIdNiveauPersonnel().getId() : null)
                .fonctionId(p.getIdFonction() != null ? p.getIdFonction().getId() : null)
                .civiliteId(p.getIdCivilite() != null ? p.getIdCivilite().getId() : null)
                .centreId(p.getIdCentre() != null ? p.getIdCentre().getId() : null)
                .structureFormationCertificationId(p.getIdStructureFormationCertification() != null ? p.getIdStructureFormationCertification().getId() : null)
                .statutPersonnelId(p.getIdStatutPersonnel() != null ? p.getIdStatutPersonnel().getId() : null)
                .codePersonnel(p.getCodePersonnel())
                .certifierPersonnel(p.getCertifierPersonnel())
                .nomPersonnel(p.getNomPersonnel())
                .prenomsPersonnel(p.getPrenomsPersonnel())
                .anneExpePersonnel(p.getAnneExpePersonnel())
                .sexePersonnel(p.getSexePersonnel())
                .dateNaissance(p.getDateNaissance())
                .ancienneFonctPromoPesonnel(p.getAncienneFonctPromoPesonnel())
                .contactPersonnel(p.getContactPersonnel())
                .boitePostalePersonnel(p.getBoitePostalePersonnel())
                .emailPersonnel(p.getEmailPersonnel())
                .denominationPersonnel(p.getDenominationPersonnel())
                .nomDuPrgramme(p.getNomDuPrgramme())
                .nomRepresentantLegalSturcture(p.getNomRepresentantLegalSturcture())
                .build();
    }

    private static Long toLongRequired(Integer v, String field) {
        if (v == null) {
            throw new IllegalArgumentException("Champ requis: " + field);
        }
        return v.longValue();
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}

