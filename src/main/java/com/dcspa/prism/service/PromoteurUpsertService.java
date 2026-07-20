package com.dcspa.prism.service;

import com.dcspa.prism.dto.PromoteurUpsertRequest;
import com.dcspa.prism.entity.Personnephysique;
import com.dcspa.prism.entity.Personnemorale;
import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.entity.TypePersonneMorale;
import com.dcspa.prism.entity.TypePromoteur;
import com.dcspa.prism.repository.PersonnephysiqueRepository;
import com.dcspa.prism.repository.PersonnemoraleRepository;
import com.dcspa.prism.repository.PromoteurRepository;
import com.dcspa.prism.repository.TypePersonneMoraleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PromoteurUpsertService {

    private final PromoteurRepository promoteurRepository;
    private final PersonnephysiqueRepository personnephysiqueRepository;
    private final PersonnemoraleRepository personnemoraleRepository;
    private final TypePersonneMoraleRepository typePersonneMoraleRepository;

    @Transactional
    public Promoteur resolveOrCreate(PromoteurUpsertRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("promoteur est obligatoire");
        }
        if (request.getId() != null) {
            return promoteurRepository.findById(request.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Promoteur introuvable: " + request.getId()));
        }
        if (request.getTypePromoteur() == null) {
            throw new IllegalArgumentException("typePromoteur est obligatoire");
        }

        Promoteur promoteur = new Promoteur();
        // Le code est généré côté backend (AutoCodeEntityListener).
        promoteur.setCodePromoteur(null);
        // Le libellé devient optionnel ; on garde la valeur seulement si elle est vraiment fournie.
        promoteur.setLibellePromoteur(normalize(request.getLibellePromoteur()));
        promoteur.setTypePromoteur(request.getTypePromoteur());
        promoteur = promoteurRepository.save(promoteur);

        if (request.getTypePromoteur() == TypePromoteur.PHYSIQUE) {
            createPhysique(promoteur, request);
        } else {
            createMorale(promoteur, request);
        }
        return promoteur;
    }

    private void createPhysique(Promoteur promoteur, PromoteurUpsertRequest request) {
        PromoteurUpsertRequest.PersonnePhysiquePayload payload = request.getPersonnePhysique();
        if (payload == null) {
            throw new IllegalArgumentException("personnePhysique est obligatoire si typePromoteur = PHYSIQUE");
        }
        Personnephysique physique = new Personnephysique();
        physique.setPromoteur(promoteur);
        physique.setCodePromoteur(promoteur.getCodePromoteur());
        physique.setLibellePromoteur(promoteur.getLibellePromoteur());
        physique.setLibellePersonnePhysique(trim(payload.getLibellePersonnePhysique()));
        physique.setNom(trim(payload.getNom()));
        physique.setPrenom(trim(payload.getPrenom()));
        physique.setContact(trim(payload.getContact()));
        physique.setFonction(trim(payload.getFonction()));
        physique.setSexe(trim(payload.getSexe()));
        physique.setDateNaissance(payload.getDateNaissance());
        physique.setAnciennete(trim(payload.getAnciennete()));
        physique.setBoitePostale(trim(payload.getBoitePostale()));
        physique.setNiveauEtudes(trim(payload.getNiveauEtudes()));
        physique.setCivilite(trim(payload.getCivilite()));
        physique.setMail(trim(payload.getMail()));
        physique.setOrganisationFaitiere(trim(payload.getOrganisationFaitiere()));
        personnephysiqueRepository.save(physique);
    }

    private void createMorale(Promoteur promoteur, PromoteurUpsertRequest request) {
        PromoteurUpsertRequest.PersonneMoralePayload payload = request.getPersonneMorale();
        if (payload == null) {
            throw new IllegalArgumentException("personneMorale est obligatoire si typePromoteur = MORALE");
        }
        if (payload.getIdTypePersonneMorale() == null) {
            throw new IllegalArgumentException("idTypePersonneMorale est obligatoire si typePromoteur = MORALE");
        }
        TypePersonneMorale type = typePersonneMoraleRepository.findById(payload.getIdTypePersonneMorale())
                .orElseThrow(() -> new IllegalArgumentException("Type personne morale introuvable: " + payload.getIdTypePersonneMorale()));

        Personnemorale morale = new Personnemorale();
        morale.setPromoteur(promoteur);
        morale.setCodePromoteur(promoteur.getCodePromoteur());
        morale.setLibellePromoteur(promoteur.getLibellePromoteur());
        morale.setDenomination(payload.getDenomination());
        morale.setNomProgramme(payload.getNomProgramme());
        morale.setNomRepresentantLegalStructure(payload.getNomRepresentant());
        morale.setContact(payload.getContact());
        morale.setBoitePostale(payload.getBoitePostale());
        morale.setMail(payload.getMail());
        morale.setTypePersonneMorale(type);
        personnemoraleRepository.save(morale);
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String trim(String value) {
        return normalize(value);
    }
}
