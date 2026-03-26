package com.dcspa.prism.service;

import com.dcspa.prism.dto.PersonnelAdminRequest;
import com.dcspa.prism.dto.PersonnelAdminResponse;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Civilite;
import com.dcspa.prism.entity.Fonction;
import com.dcspa.prism.entity.NiveauPersonnel;
import com.dcspa.prism.entity.Personnel;
import com.dcspa.prism.entity.StatutPersonnel;
import com.dcspa.prism.entity.StructureFormationCertification;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.CiviliteRepository;
import com.dcspa.prism.repository.FonctionRepository;
import com.dcspa.prism.repository.NiveauPersonnelRepository;
import com.dcspa.prism.repository.PersonnelRepository;
import com.dcspa.prism.repository.StatutPersonnelRepository;
import com.dcspa.prism.repository.StructureFormationCertificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonnelAdminService {
    private final PersonnelRepository personnelRepository;
    private final NiveauPersonnelRepository niveauPersonnelRepository;
    private final FonctionRepository fonctionRepository;
    private final CiviliteRepository civiliteRepository;
    private final CentreRepository centreRepository;
    private final StructureFormationCertificationRepository structureFormationCertificationRepository;
    private final StatutPersonnelRepository statutPersonnelRepository;

    @Transactional(readOnly = true)
    public List<PersonnelAdminResponse> listByCentre(Integer centreId) {
        return personnelRepository.findByIdCentre_Id(centreId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public long countByCentre(Integer centreId) {
        return personnelRepository.countByIdCentre_Id(centreId);
    }

    @Transactional
    public PersonnelAdminResponse create(PersonnelAdminRequest r) {
        Personnel p = new Personnel();
        applyRequestToEntity(p, r);
        return toDto(personnelRepository.save(p));
    }

    @Transactional
    public PersonnelAdminResponse update(Integer id, PersonnelAdminRequest r) {
        Personnel p = personnelRepository.findById(id.longValue())
                .orElseThrow(() -> new IllegalArgumentException("Personnel introuvable: " + id));
        applyRequestToEntity(p, r);
        return toDto(personnelRepository.save(p));
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

    private PersonnelAdminResponse toDto(Personnel p) {
        return PersonnelAdminResponse.builder()
                .id(p.getId())
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
}

