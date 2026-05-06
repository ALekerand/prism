package com.dcspa.prism.config;

import com.dcspa.prism.entity.*;
import com.dcspa.prism.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Données de démonstration pour les référentiels sans dépendances FK complexes.
 * Exécuté au démarrage si la table concernée est vide (idempotent avec {@code ddl-auto=update}).
 * <p>
 * Pour les entités annotées {@link com.dcspa.prism.codegen.AutoCode}, les codes métier ne sont
 * <strong>pas</strong> renseignés ici : {@link com.dcspa.prism.codegen.AutoCodeEntityListener} les
 * génère au {@code persist} via {@link com.dcspa.prism.codegen.CodeGeneratorService}
 * (préfixe 3 lettres dérivé du nom de table + compteur 7 chiffres, ex. {@code ASC0000001}).
 * Ne couvre pas : documents, ministères/communautés (liées à promoteur), modes alpha / niveaux alpha (liés à centre), etc.
 */
@Component
@RequiredArgsConstructor
public class ReferentielDemoDataInitializer {

    private static final String DEMO_CENTRE_ALPHA = "CTR-ALPHA-01";
    private static final String DEMO_CENTRE_CEC = "CTR-CEC-01";
    private static final String DEMO_CENTRE_CP = "CTR-CP-01";
    private static final String DEMO_CENTRE_SIE = "CTR-SIE-01";
    private static final List<String> NIVEAUX_ALPHA_REELS = List.of("NIVEAU_1", "NIVEAU_2", "POST_ALPHA");
    private static final List<String> NIVEAUX_CEC_SIE_REELS = List.of("PS", "MS", "GS", "PRE_PRIMAIRE", "CP1", "CP2", "CE1", "CE2", "CM1", "CM2");
    private static final List<String> NIVEAUX_CP_REELS = List.of("CPU", "CEU", "CMU");

    private final AnneScolaireRepository anneScolaireRepository;
    private final AutoriteAutorisationRepository autoriteAutorisationRepository;
    private final CategorieAppuiRepository categorieAppuiRepository;
    private final CiviliteRepository civiliteRepository;
    private final CompetenceRepository competenceRepository;
    private final DesignationRepository designationRepository;
    private final DifficulteRepository difficulteRepository;
    private final DiplomeRepository diplomeRepository;
    private final DomaineActiviteRepository domaineActiviteRepository;
    private final FonctionRepository fonctionRepository;
    private final ImpactRepository impactRepository;
    private final InfrastructureRepository infrastructureRepository;
    private final MaterielsPedagogiqueRepository materielsPedagogiqueRepository;
    private final NaturecentreRepository naturecentreRepository;
    private final NatureDocumentRepository natureDocumentRepository;
    private final NiveauPersonnelRepository niveauPersonnelRepository;
    private final NiveauAlphaRepository niveauAlphaRepository;
    private final NiveauCpRepository niveauCpRepository;
    private final NiveauSieCecRepository niveauSieCecRepository;
    private final PartenaireRepository partenaireRepository;
    private final PeriodeActiviteRepository periodeActiviteRepository;
    private final PeriodiciteRepository periodiciteRepository;
    private final RegimealphabetisationRepository regimealphabetisationRepository;
    private final StatutPersonnelRepository statutPersonnelRepository;
    private final SupportDidactiqueRepository supportDidactiqueRepository;
    private final TypeAlphaRepository typeAlphaRepository;
    private final TypeDocumentRepository typeDocumentRepository;
    private final CampagneRepository campagneRepository;
    private final CategorieCentreAlphaRepository categorieCentreAlphaRepository;
    private final AlphaRepository alphaRepository;
    private final CecRepository cecRepository;
    private final CpRepository cpRepository;
    private final SieRepository sieRepository;
    private final AppRoleRepository appRoleRepository;

    // Dépendances nécessaires pour créer des centres + personnel de démo
    private final DepartementRepository departementRepository;
    private final SousPrefectureRepository sousPrefectureRepository;
    private final MilieuImplantationRepository milieuImplantationRepository;
    private final CommuneRepository communeRepository;
    private final LocaliteDImplantationRepository localiteDImplantationRepository;
    private final DrenaRepository drenaRepository;
    private final IeppRepository ieppRepository;
    private final PromoteurRepository promoteurRepository;
    private final PersonnephysiqueRepository personnephysiqueRepository;
    private final TypePersonneMoraleRepository typePersonneMoraleRepository;
    private final CentreRepository centreRepository;
    private final PersonnelRepository personnelRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Order(25)
    @Transactional
    public void seedReferentielsDemo() {
        // Socles requis pour centres/personnel
        seedDepartements();
        seedMilieuxImplantation();
        seedCommunes();
        seedSousPrefectures();
        seedLocalites();
        seedDrenas();
        seedIeps();
        seedTypePersonneMorale();
        seedPromoteurs();
        seedNiveauxPersonnel();

        seedAnneScolaires();
        seedAutorites();
        seedCategoriesAppui();
        seedCivilites();
        seedCompetences();
        seedDesignations();
        seedDifficultes();
        seedDiplomes();
        seedDomainesActivite();
        seedFonctions();
        seedImpacts();
        seedInfrastructures();
        seedMaterielsPedagogiques();
        seedNaturesCentre();
        seedNaturesDocument();
        seedNiveauxCp();
        seedNiveauxSieCec();
        seedPartenaires();
        seedPeriodesActivite();
        seedPeriodicites();
        seedRegimes();
        seedStatutsPersonnel();
        seedSupportsDidactiques();
        seedTypesAlpha();
        seedTypesDocument();
        seedCampagnes();
        seedCategoriesCentreAlpha();

        // Jeux de données métiers de démo (pour tester l'écran Administration > Personnel)
        seedCentres();
        seedPersonnel();

        // "Typage" métier des centres (un centre appartient à Alpha/Cec/Cp/Sie)
        seedAlphaCentre();
        seedNiveauxAlpha();
        seedCecCentre();
        seedCpCentre();
        seedSieCentre();

        // Synchronisation "réalité" non destructive (libellés/codes + inserts manquants)
        syncReferenceRealiteLabels();
    }

    private void syncReferenceRealiteLabels() {
        syncRolesRealite();
        syncMilieuxRealite();
        syncBasicGeoLabels();
        syncNiveauxRealite();
    }

    private void syncRolesRealite() {
        upsertRole("CONSEILLER", "Conseiller", "Niveau 1");
        upsertRole("COORDONNATEUR", "Coordonnateur", "Niveau 2");
        upsertRole("SUPERVISEUR", "Superviseur", "Niveau 3");
        upsertRole("DIRECTEUR", "Directeur", "Niveau 4");
        upsertRole("CABINET", "Cabinet", "Niveau 5");
        upsertRole("AGENT_ARCHIVE", "Agent archive", "Niveau 6");
        upsertRole("SUPER_ADMIN", "Super admin", "Niveau 7");
    }

    private void upsertRole(String code, String libelle, String description) {
        AppRole role = appRoleRepository.findByCodeRole(code).orElseGet(AppRole::new);
        role.setCodeRole(code);
        role.setLibelleRole(libelle);
        role.setDescriptionRole(description);
        appRoleRepository.save(role);
    }

    private void syncMilieuxRealite() {
        upsertMilieu("URB", "URBAIN");
        upsertMilieu("RUR", "RURAL");
        upsertMilieu("ABJ", "ABIDJAN");
    }

    private void upsertMilieu(String code, String libelle) {
        MilieuImplantation m = milieuImplantationRepository.findAll().stream()
                .filter(x -> code.equalsIgnoreCase(String.valueOf(x.getCodeMilieuImplentation())))
                .findFirst()
                .orElseGet(MilieuImplantation::new);
        m.setCodeMilieuImplentation(code);
        m.setLibelleTypeImplentation(libelle);
        milieuImplantationRepository.save(m);
    }

    private void syncBasicGeoLabels() {
        departementRepository.findAll().stream()
                .filter(d -> "ABJ".equalsIgnoreCase(String.valueOf(d.getCodeDepartement())))
                .findFirst()
                .ifPresent(d -> {
                    d.setNomDepartement("Abidjan");
                    departementRepository.save(d);
                });

        sousPrefectureRepository.findAll().stream()
                .filter(s -> "SPABJ".equalsIgnoreCase(String.valueOf(s.getCodeSousPrefecture())))
                .findFirst()
                .ifPresent(s -> {
                    s.setNomSousPrefecture("Abidjan");
                    sousPrefectureRepository.save(s);
                });

        communeRepository.findAll().stream()
                .filter(c -> "COC".equalsIgnoreCase(String.valueOf(c.getCodeCommune())))
                .findFirst()
                .ifPresent(c -> {
                    c.setNomCommune("Cocody");
                    communeRepository.save(c);
                });

        drenaRepository.findAll().stream()
                .filter(d -> "DABJ".equalsIgnoreCase(String.valueOf(d.getCodeDrena())))
                .findFirst()
                .ifPresent(d -> {
                    d.setNomDrena("DRENA Abidjan");
                    drenaRepository.save(d);
                });

        ieppRepository.findAll().stream()
                .filter(i -> "IEP1".equalsIgnoreCase(String.valueOf(i.getCodeIep())))
                .findFirst()
                .ifPresent(i -> {
                    i.setNomIep("IEP Cocody");
                    ieppRepository.save(i);
                });

        localiteDImplantationRepository.findAll().stream()
                .filter(l -> "LOC1".equalsIgnoreCase(String.valueOf(l.getCodeLocalite())))
                .findFirst()
                .ifPresent(l -> {
                    l.setNomLocalite("Cocody Angré");
                    localiteDImplantationRepository.save(l);
                });
    }

    private void seedCampagnes() {
        if (campagneRepository.count() > 0) return;
        Campagne c = new Campagne();
        c.setDateDebutCampagne(LocalDate.of(2026, 1, 1));
        c.setDateFinCampagne(LocalDate.of(2026, 12, 31));
        c.setEtatCampagne(true);
        campagneRepository.save(c);
    }

    private void seedCategoriesCentreAlpha() {
        if (categorieCentreAlphaRepository.count() > 0) return;
        CategorieCentreAlpha cat = new CategorieCentreAlpha();
        cat.setCodeCategorieCentreAlpha("CAT1");
        cat.setLibelleCategorieCentreAlpha("Centre démo");
        categorieCentreAlphaRepository.save(cat);
    }

    private void seedDepartements() {
        if (departementRepository.count() > 0) return;

        Departement d = new Departement();
        d.setCodeDepartement("ABJ");
        d.setNomDepartement("Abidjan");
        departementRepository.save(d);
    }

    private void seedSousPrefectures() {
        if (sousPrefectureRepository.count() > 0) return;
        Departement dep = departementRepository.findAll().stream().findFirst().orElse(null);
        if (dep == null) return;

        SousPrefecture sp = new SousPrefecture();
        sp.setIdDepartement(dep);
        sp.setCodeSousPrefecture("SPABJ");
        sp.setNomSousPrefecture("Abidjan");
        sousPrefectureRepository.save(sp);
    }

    private void seedMilieuxImplantation() {
        if (milieuImplantationRepository.count() > 0) return;

        MilieuImplantation urbain = new MilieuImplantation();
        urbain.setCodeMilieuImplentation("URB");
        urbain.setLibelleTypeImplentation("Urbain");
        milieuImplantationRepository.save(urbain);

        MilieuImplantation rural = new MilieuImplantation();
        rural.setCodeMilieuImplentation("RUR");
        rural.setLibelleTypeImplentation("Rural");
        milieuImplantationRepository.save(rural);
    }

    private void seedCommunes() {
        if (communeRepository.count() > 0) return;

        Commune c = new Commune();
        c.setCodeCommune("COC");
        c.setNomCommune("Cocody");
        communeRepository.save(c);
    }

    private void seedLocalites() {
        if (localiteDImplantationRepository.count() > 0) return;
        SousPrefecture sp = sousPrefectureRepository.findAll().stream().findFirst().orElse(null);
        MilieuImplantation mi = milieuImplantationRepository.findAll().stream().findFirst().orElse(null);
        if (sp == null || mi == null) return;

        Commune com = communeRepository.findAll().stream().findFirst().orElse(null);

        LocaliteDImplantation loc = new LocaliteDImplantation();
        loc.setIdSousPrefecture(sp);
        loc.setIdMilieuImplentation(mi);
        loc.setIdCommune(com);
        loc.setCodeLocalite("LOC1");
        loc.setNomLocalite("Cocody Angré");
        localiteDImplantationRepository.save(loc);
    }

    private void seedDrenas() {
        if (drenaRepository.count() > 0) return;

        Drena d = new Drena();
        d.setCodeDrena("DABJ");
        d.setNomDrena("DRENA Abidjan");
        d.setMailDrena("drena-abj@prism.local");
        d.setTelephoneDrena("0102030405");
        drenaRepository.save(d);
    }

    private void seedIeps() {
        if (ieppRepository.count() > 0) return;
        Drena d = drenaRepository.findAll().stream().findFirst().orElse(null);
        if (d == null) return;

        Iep iep = new Iep();
        iep.setIdDrena(d);
        iep.setCodeIep("IEP1");
        iep.setNomIep("IEP Cocody");
        iep.setMailIep("iep-cocody@prism.local");
        iep.setTelephoneIep("0708091011");
        ieppRepository.save(iep);
    }

    private void seedPromoteurs() {
        if (promoteurRepository.count() > 0) return;

        Promoteur p = new Promoteur();
        p.setCodePromoteur("PROMO1");
        p.setLibellePromoteur("Promoteur Démo");
        p.setTypePromoteur(TypePromoteur.PHYSIQUE);
        Promoteur saved = promoteurRepository.save(p);

        Personnephysique physique = new Personnephysique();
        physique.setPromoteur(saved);
        physique.setCodePromoteur(saved.getCodePromoteur());
        physique.setLibellePromoteur(saved.getLibellePromoteur());
        physique.setLibellePersonnePhysique("Promoteur Démo");
        personnephysiqueRepository.save(physique);
    }

    private void seedTypePersonneMorale() {
        if (typePersonneMoraleRepository.count() > 0) return;
        saveTypePersonneMorale("PTF");
        saveTypePersonneMorale("MINISTERE");
        saveTypePersonneMorale("ONG");
        saveTypePersonneMorale("SOCIETE CIVILE");
        saveTypePersonneMorale("ASSOCIATION");
        saveTypePersonneMorale("COMMUNAUTE");
        saveTypePersonneMorale("PARTICULIER");
        saveTypePersonneMorale("AUTRE");
    }

    private void saveTypePersonneMorale(String libelle) {
        TypePersonneMorale type = new TypePersonneMorale();
        type.setLibelle(libelle);
        typePersonneMoraleRepository.save(type);
    }

    private void seedNiveauxPersonnel() {
        if (niveauPersonnelRepository.count() > 0) return;

        NiveauPersonnel n1 = new NiveauPersonnel();
        n1.setCodeNiveauPersonnel("N1");
        n1.setLibelleNiveauPersonnel("Niveau 1");
        niveauPersonnelRepository.save(n1);

        NiveauPersonnel n2 = new NiveauPersonnel();
        n2.setCodeNiveauPersonnel("N2");
        n2.setLibelleNiveauPersonnel("Niveau 2");
        niveauPersonnelRepository.save(n2);
    }

    private void seedCentres() {
        LocaliteDImplantation loc = localiteDImplantationRepository.findAll().stream().findFirst().orElse(null);
        Iep iep = ieppRepository.findAll().stream().findFirst().orElse(null);
        Naturecentre nature = naturecentreRepository.findAll().stream().findFirst().orElse(null);
        Promoteur prom = promoteurRepository.findAll().stream().findFirst().orElse(null);

        if (loc == null || iep == null || nature == null || prom == null) return;

        createCentreIfMissing(DEMO_CENTRE_ALPHA, "Centre Alpha Démo", loc, iep, nature, prom);
        createCentreIfMissing(DEMO_CENTRE_CEC, "Centre CEC Démo", loc, iep, nature, prom);
        createCentreIfMissing(DEMO_CENTRE_CP, "Centre CP Démo", loc, iep, nature, prom);
        createCentreIfMissing(DEMO_CENTRE_SIE, "Centre SIE Démo", loc, iep, nature, prom);
    }

    private void createCentreIfMissing(
            String codeCentre,
            String localisation,
            LocaliteDImplantation loc,
            Iep iep,
            Naturecentre nature,
            Promoteur prom
    ) {
        if (centreRepository.findByCodeCentre(codeCentre).isPresent()) {
            return;
        }
        Centre c = new Centre();
        c.setIdLocalite(loc);
        c.setIdIep(iep);
        c.setIdNaturecentre(nature);
        c.setIdPromoteur(prom);
        c.setCodeCentre(codeCentre);
        c.setAutorisation(true);
        c.setEncadrerParMena(true);
        c.setEstElectrifie(true);
        c.setADeLeau(true);
        c.setNombreVisite(0);
        c.setLocalisationCentre(localisation);
        c.setNomMilieuImplentation("Urbain");
        centreRepository.save(c);
    }

    private void seedPersonnel() {
        if (personnelRepository.count() > 0) return;

        Centre centre = centreRepository.findByCodeCentre(DEMO_CENTRE_ALPHA).orElse(null);
        if (centre == null) return;

        Civilite civ = civiliteRepository.findAll().stream().findFirst().orElse(null);
        Fonction fct = fonctionRepository.findAll().stream().findFirst().orElse(null);
        NiveauPersonnel niv = niveauPersonnelRepository.findAll().stream().findFirst().orElse(null);
        StatutPersonnel statut = statutPersonnelRepository.findAll().stream().findFirst().orElse(null);
        if (civ == null || fct == null || niv == null || statut == null) return;

        Personnel p1 = new Personnel();
        p1.setIdCentre(centre);
        p1.setIdCivilite(civ);
        p1.setIdFonction(fct);
        p1.setIdNiveauPersonnel(niv);
        p1.setIdStatutPersonnel(statut);
        p1.setNomPersonnel("KOUADIO");
        p1.setPrenomsPersonnel("Aminata");
        p1.setContactPersonnel("0101010101");
        p1.setEmailPersonnel("aminata.kouadio@prism.local");
        p1.setSexePersonnel("F");
        p1.setCertifierPersonnel(false);
        p1.setAnneExpePersonnel(3);
        personnelRepository.save(p1);

        Personnel p2 = new Personnel();
        p2.setIdCentre(centre);
        p2.setIdCivilite(civ);
        p2.setIdFonction(fct);
        p2.setIdNiveauPersonnel(niv);
        p2.setIdStatutPersonnel(statut);
        p2.setNomPersonnel("TRAORE");
        p2.setPrenomsPersonnel("Moussa");
        p2.setContactPersonnel("0202020202");
        p2.setEmailPersonnel("moussa.traore@prism.local");
        p2.setSexePersonnel("M");
        p2.setCertifierPersonnel(true);
        p2.setAnneExpePersonnel(5);
        personnelRepository.save(p2);
    }

    private void seedAlphaCentre() {
        if (alphaRepository.count() > 0) return;
        Centre centre = centreRepository.findByCodeCentre(DEMO_CENTRE_ALPHA).orElse(null);
        if (centre == null) return;

        Campagne campagne = campagneRepository.findAll().stream().findFirst().orElse(null);
        CategorieCentreAlpha categorie = categorieCentreAlphaRepository.findAll().stream().findFirst().orElse(null);
        TypeAlpha typeAlpha = typeAlphaRepository.findAll().stream().findFirst().orElse(null);
        Regimealphabetisation regime = regimealphabetisationRepository.findAll().stream().findFirst().orElse(null);
        if (campagne == null || categorie == null || typeAlpha == null || regime == null) return;

        Alpha a = new Alpha();
        a.setCentre(centre);
        a.setIdCompagne(campagne);
        a.setIdCategorieCentreAlpha(categorie);
        a.setIdTypeAlpha(typeAlpha);
        a.setIdRegimeAlpha(regime);
        mirrorDenormalizedCentreFields(centre, a);
        a.setLibelleAlpha("Alpha - " + centre.getCodeCentre());
        alphaRepository.save(a);
    }

    private void seedCecCentre() {
        if (cecRepository.count() > 0) return;
        Centre centre = centreRepository.findByCodeCentre(DEMO_CENTRE_CEC).orElse(null);
        if (centre == null) return;

        Cec c = new Cec();
        c.setCentre(centre);
        mirrorDenormalizedCentreFields(centre, c);
        c.setLibelleCec("CEC - " + centre.getCodeCentre());
        cecRepository.save(c);
    }

    private void seedCpCentre() {
        if (cpRepository.count() > 0) return;
        Centre centre = centreRepository.findByCodeCentre(DEMO_CENTRE_CP).orElse(null);
        if (centre == null) return;

        Cp cp = new Cp();
        cp.setCentre(centre);
        mirrorDenormalizedCentreFields(centre, cp);
        cp.setLibellleCp("CP - " + centre.getCodeCentre());
        cpRepository.save(cp);
    }

    private void seedSieCentre() {
        if (sieRepository.count() > 0) return;
        Centre centre = centreRepository.findByCodeCentre(DEMO_CENTRE_SIE).orElse(null);
        if (centre == null) return;

        Sie s = new Sie();
        s.setCentre(centre);
        mirrorDenormalizedCentreFields(centre, s);
        s.setLibelleSie("SIE - " + centre.getCodeCentre());
        sieRepository.save(s);
    }

    /**
     * Même logique que les contrôleurs (création) : les listes API lisent les colonnes dénormalisées
     * sur alpha / cec / cp / sie, pas une jointure vers {@link Centre} à la volée.
     */
    private void mirrorDenormalizedCentreFields(Centre centre, Object typedRow) {
        Integer idLocalite = centre.getIdLocalite() != null ? centre.getIdLocalite().getId() : null;
        Integer idPeriodicite = centre.getIdPeriodicite() != null ? centre.getIdPeriodicite().getId() : null;
        Integer idIep = centre.getIdIep() != null ? centre.getIdIep().getId() : null;
        Integer idAutoriteAutorisation =
                centre.getIdAutoriteAutorisation() != null ? centre.getIdAutoriteAutorisation().getId() : null;
        Integer idNaturecentre = centre.getIdNaturecentre() != null ? centre.getIdNaturecentre().getId() : null;
        Integer idPromoteur = centre.getIdPromoteur() != null ? centre.getIdPromoteur().getId() : null;

        switch (typedRow) {
            case Alpha a -> {
                a.setIdLocalite(idLocalite);
                a.setIdPeriodicite(idPeriodicite);
                a.setIdIep(idIep);
                a.setIdAutoriteAutorisation(idAutoriteAutorisation);
                a.setIdNaturecentre(idNaturecentre);
                a.setIdPromoteur(idPromoteur);
                a.setCodeCentre(centre.getCodeCentre());
                a.setAutorisation(centre.getAutorisation());
                a.setEncadreurNonMena(centre.getEncadreurNonMena());
                a.setEncadrerParMena(centre.getEncadrerParMena());
                a.setEstElectrifie(centre.getEstElectrifie());
                a.setADeLeau(centre.getADeLeau());
                a.setNombreVisite(centre.getNombreVisite());
                a.setLocalisationCentre(centre.getLocalisationCentre());
                a.setNomMilieuImplentation(centre.getNomMilieuImplentation());
            }
            case Cec c -> {
                c.setIdLocalite(idLocalite);
                c.setIdPeriodicite(idPeriodicite);
                c.setIdIep(idIep);
                c.setIdAutoriteAutorisation(idAutoriteAutorisation);
                c.setIdNaturecentre(idNaturecentre);
                c.setIdPromoteur(idPromoteur);
                c.setCodeCentre(centre.getCodeCentre());
                c.setAutorisation(centre.getAutorisation());
                c.setEncadreurNonMena(centre.getEncadreurNonMena());
                c.setEncadrerParMena(centre.getEncadrerParMena());
                c.setEstElectrifie(centre.getEstElectrifie());
                c.setADeLeau(centre.getADeLeau());
                c.setNombreVisite(centre.getNombreVisite());
                c.setLocalisationCentre(centre.getLocalisationCentre());
                c.setNomMilieuImplentation(centre.getNomMilieuImplentation());
            }
            case Cp cp -> {
                cp.setIdLocalite(idLocalite);
                cp.setIdPeriodicite(idPeriodicite);
                cp.setIdIep(idIep);
                cp.setIdAutoriteAutorisation(idAutoriteAutorisation);
                cp.setIdNaturecentre(idNaturecentre);
                cp.setIdPromoteur(idPromoteur);
                cp.setCodeCentre(centre.getCodeCentre());
                cp.setAutorisation(centre.getAutorisation());
                cp.setEncadreurNonMena(centre.getEncadreurNonMena());
                cp.setEncadrerParMena(centre.getEncadrerParMena());
                cp.setEstElectrifie(centre.getEstElectrifie());
                cp.setADeLeau(centre.getADeLeau());
                cp.setNombreVisite(centre.getNombreVisite());
                cp.setLocalisationCentre(centre.getLocalisationCentre());
                cp.setNomMilieuImplentation(centre.getNomMilieuImplentation());
            }
            case Sie s -> {
                s.setIdLocalite(idLocalite);
                s.setIdPeriodicite(idPeriodicite);
                s.setIdIep(idIep);
                s.setIdAutoriteAutorisation(idAutoriteAutorisation);
                s.setIdNaturecentre(idNaturecentre);
                s.setIdPromoteur(idPromoteur);
                s.setCodeCentre(centre.getCodeCentre());
                s.setAutorisation(centre.getAutorisation());
                s.setEncadreurNonMena(centre.getEncadreurNonMena());
                s.setEncadrerParMena(centre.getEncadrerParMena());
                s.setEstElectrifie(centre.getEstElectrifie());
                s.setADeLeau(centre.getADeLeau());
                s.setNombreVisite(centre.getNombreVisite());
                s.setLocalisationCentre(centre.getLocalisationCentre());
                s.setNomMilieuImplentation(centre.getNomMilieuImplentation());
            }
            default -> throw new IllegalArgumentException("Type non géré: " + typedRow.getClass().getName());
        }
    }

    private void seedAnneScolaires() {
        if (anneScolaireRepository.count() > 0) {
            return;
        }
        AnneScolaire a1 = new AnneScolaire();
        a1.setDebutAnneeScolaire(LocalDate.of(2024, 9, 1));
        a1.setFinAnneeScolaire(LocalDate.of(2025, 6, 30));
        a1.setEtatAnneeScolaire(false);
        anneScolaireRepository.save(a1);

        AnneScolaire a2 = new AnneScolaire();
        a2.setDebutAnneeScolaire(LocalDate.of(2025, 9, 1));
        a2.setFinAnneeScolaire(LocalDate.of(2026, 6, 30));
        a2.setEtatAnneeScolaire(true);
        anneScolaireRepository.save(a2);
    }

    private void seedAutorites() {
        if (autoriteAutorisationRepository.count() > 0) {
            return;
        }
        saveAutorite("Préfecture");
        saveAutorite("Sous-préfecture");
        saveAutorite("Mairie / commune");
    }

    private void saveAutorite(String libelle) {
        AutoriteAutorisation e = new AutoriteAutorisation();
        e.setLibelleAutoriteAutorisation(libelle);
        autoriteAutorisationRepository.save(e);
    }

    private void seedCategoriesAppui() {
        if (categorieAppuiRepository.count() > 0) {
            return;
        }
        saveCategorieAppui("Appui financier");
        saveCategorieAppui("Appui matériel");
        saveCategorieAppui("Appui technique");
    }

    private void saveCategorieAppui(String libelle) {
        CategorieAppui e = new CategorieAppui();
        e.setLibelleCategorieAppui(libelle);
        categorieAppuiRepository.save(e);
    }

    private void seedCivilites() {
        if (civiliteRepository.count() > 0) {
            return;
        }
        saveCivilite("M.");
        saveCivilite("Mme");
        saveCivilite("Mlle");
    }

    private void saveCivilite(String libelle) {
        Civilite e = new Civilite();
        e.setLibelleCivilite(libelle.length() > 10 ? libelle.substring(0, 10) : libelle);
        civiliteRepository.save(e);
    }

    private void seedCompetences() {
        if (competenceRepository.count() > 0) {
            return;
        }
        saveCompetence("Lecture");
        saveCompetence("Calcul mental");
        saveCompetence("Expression orale");
    }

    private void saveCompetence(String libelle) {
        Competence e = new Competence();
        e.setLibelleCompetence(libelle.length() > 20 ? libelle.substring(0, 20) : libelle);
        competenceRepository.save(e);
    }

    private void seedDesignations() {
        if (designationRepository.count() > 0) {
            return;
        }
        saveDesignation("Animateur");
        saveDesignation("Superviseur");
        saveDesignation("Conseiller pédagogique");
    }

    private void saveDesignation(String libelle) {
        Designation e = new Designation();
        e.setLibelleDesignation(libelle.length() > 50 ? libelle.substring(0, 50) : libelle);
        designationRepository.save(e);
    }

    private void seedDifficultes() {
        if (difficulteRepository.count() > 0) {
            return;
        }
        saveDifficulte("Éloignement géographique");
        saveDifficulte("Manque de matériel");
        saveDifficulte("Barrière linguistique");
    }

    private void saveDifficulte(String libelle) {
        Difficulte e = new Difficulte();
        e.setLibelleDifficulte(libelle.length() > 50 ? libelle.substring(0, 50) : libelle);
        difficulteRepository.save(e);
    }

    private void seedDiplomes() {
        if (diplomeRepository.count() > 0) {
            return;
        }
        saveDiplome("CEPE");
        saveDiplome("BEPC");
        saveDiplome("Baccalauréat");
    }

    private void saveDiplome(String libelle) {
        Diplome e = new Diplome();
        e.setLibelleDiplome(libelle.length() > 100 ? libelle.substring(0, 100) : libelle);
        diplomeRepository.save(e);
    }

    private void seedDomainesActivite() {
        if (domaineActiviteRepository.count() > 0) {
            return;
        }
        saveDomaine("Alphabétisation");
        saveDomaine("Post-alphabétisation");
        saveDomaine("Inclusion numérique");
    }

    private void saveDomaine(String libelle) {
        DomaineActivite e = new DomaineActivite();
        e.setLibelleDomaineActivite(libelle.length() > 100 ? libelle.substring(0, 100) : libelle);
        domaineActiviteRepository.save(e);
    }

    private void seedFonctions() {
        if (fonctionRepository.count() > 0) {
            return;
        }
        saveFonction("Directeur de centre");
        saveFonction("Adjoint");
        saveFonction("Enseignant");
    }

    private void saveFonction(String libelle) {
        Fonction e = new Fonction();
        e.setLibelleFonction(libelle.length() > 100 ? libelle.substring(0, 100) : libelle);
        fonctionRepository.save(e);
    }

    private void seedImpacts() {
        if (impactRepository.count() > 0) {
            return;
        }
        saveImpact("Impact social");
        saveImpact("Impact économique");
        saveImpact("Impact éducatif");
    }

    private void saveImpact(String libelle) {
        Impact e = new Impact();
        e.setLibelleImpact(libelle.length() > 50 ? libelle.substring(0, 50) : libelle);
        impactRepository.save(e);
    }

    private void seedInfrastructures() {
        if (infrastructureRepository.count() > 0) {
            return;
        }
        saveInfrastructure("Salle de classe couverte");
        saveInfrastructure("Latrines");
        saveInfrastructure("Forage / point d’eau");
    }

    private void saveInfrastructure(String libelle) {
        Infrastructure e = new Infrastructure();
        e.setLibelleInfrastructure(libelle.length() > 100 ? libelle.substring(0, 100) : libelle);
        infrastructureRepository.save(e);
    }

    private void seedMaterielsPedagogiques() {
        if (materielsPedagogiqueRepository.count() > 0) {
            return;
        }
        saveMaterielPedago("Ardoises");
        saveMaterielPedago("Livrets pédagogiques");
        saveMaterielPedago("Tableau mural");
    }

    private void saveMaterielPedago(String libelle) {
        MaterielsPedagogique e = new MaterielsPedagogique();
        e.setLibelleMaterielPedagogique(libelle.length() > 50 ? libelle.substring(0, 50) : libelle);
        materielsPedagogiqueRepository.save(e);
    }

    private void seedNaturesCentre() {
        if (naturecentreRepository.count() > 0) {
            return;
        }
        saveNatureCentre("Centre fixe");
        saveNatureCentre("Centre mobile");
        saveNatureCentre("Relais communautaire");
    }

    private void saveNatureCentre(String libelle) {
        Naturecentre e = new Naturecentre();
        e.setLibelleNatureCentre(libelle.length() > 100 ? libelle.substring(0, 100) : libelle);
        naturecentreRepository.save(e);
    }

    private void seedNaturesDocument() {
        if (natureDocumentRepository.count() > 0) {
            return;
        }
        saveNatureDocument("Document administratif");
        saveNatureDocument("Document pédagogique");
        saveNatureDocument("Document financier");
    }

    private void saveNatureDocument(String libelle) {
        NatureDocument e = new NatureDocument();
        e.setLibelleNatureDocument(libelle.length() > 100 ? libelle.substring(0, 100) : libelle);
        natureDocumentRepository.save(e);
    }

    private void seedNiveauxCp() {
        if (niveauCpRepository.count() > 0) {
            return;
        }
        for (String libelle : NIVEAUX_CP_REELS) {
            saveNiveauCp(libelle);
        }
    }

    private void saveNiveauCp(String libelle) {
        NiveauCp e = new NiveauCp();
        e.setLibelleNiveauCp(libelle.length() > 100 ? libelle.substring(0, 100) : libelle);
        niveauCpRepository.save(e);
    }

    private void seedNiveauxSieCec() {
        if (niveauSieCecRepository.count() > 0) {
            return;
        }
        for (String libelle : NIVEAUX_CEC_SIE_REELS) {
            saveNiveauSieCec(libelle);
        }
    }

    private void saveNiveauSieCec(String libelle) {
        NiveauSieCec e = new NiveauSieCec();
        e.setLibelleNiveauSie(libelle.length() > 100 ? libelle.substring(0, 100) : libelle);
        niveauSieCecRepository.save(e);
    }

    private void seedNiveauxAlpha() {
        if (niveauAlphaRepository.count() > 0) return;
        Alpha alpha = alphaRepository.findAll().stream().findFirst().orElse(null);
        if (alpha == null) return;
        for (String libelle : NIVEAUX_ALPHA_REELS) {
            saveNiveauAlpha(alpha, libelle);
        }
    }

    private void saveNiveauAlpha(Alpha alpha, String libelle) {
        NiveauAlpha n = new NiveauAlpha();
        n.setIdCentre(alpha);
        n.setLibelleNiveauAlpha(libelle.length() > 100 ? libelle.substring(0, 100) : libelle);
        niveauAlphaRepository.save(n);
    }

    private void syncNiveauxRealite() {
        syncNiveauxCpRealite();
        syncNiveauxSieCecRealite();
        syncNiveauxAlphaRealite();
    }

    private void syncNiveauxCpRealite() {
        for (String libelle : NIVEAUX_CP_REELS) {
            upsertNiveauCp(libelle);
        }
    }

    private void upsertNiveauCp(String libelle) {
        boolean exists = niveauCpRepository.findAll().stream()
                .anyMatch(x -> libelle.equalsIgnoreCase(String.valueOf(x.getLibelleNiveauCp())));
        if (!exists) {
            saveNiveauCp(libelle);
        }
    }

    private void syncNiveauxSieCecRealite() {
        for (String libelle : NIVEAUX_CEC_SIE_REELS) {
            upsertNiveauSieCec(libelle);
        }
    }

    private void upsertNiveauSieCec(String libelle) {
        boolean exists = niveauSieCecRepository.findAll().stream()
                .anyMatch(x -> libelle.equalsIgnoreCase(String.valueOf(x.getLibelleNiveauSie())));
        if (!exists) {
            saveNiveauSieCec(libelle);
        }
    }

    private void syncNiveauxAlphaRealite() {
        List<Alpha> alphas = alphaRepository.findAll();
        if (alphas.isEmpty()) return;
        for (Alpha a : alphas) {
            for (String libelle : NIVEAUX_ALPHA_REELS) {
                upsertNiveauAlpha(a, libelle);
            }
        }
    }

    private void upsertNiveauAlpha(Alpha alpha, String libelle) {
        boolean exists = niveauAlphaRepository.findAll().stream()
                .anyMatch(x ->
                        x.getIdCentre() != null &&
                                alpha.getId().equals(x.getIdCentre().getId()) &&
                                libelle.equalsIgnoreCase(String.valueOf(x.getLibelleNiveauAlpha()))
                );
        if (!exists) {
            saveNiveauAlpha(alpha, libelle);
        }
    }

    private void seedPartenaires() {
        if (partenaireRepository.count() > 0) {
            return;
        }
        savePartenaire("ONG Éducation pour tous");
        savePartenaire("Partenaire technique financier");
        savePartenaire("Collectivité locale");
    }

    private void savePartenaire(String libelle) {
        Partenaire e = new Partenaire();
        e.setLibellePartenaire(libelle.length() > 100 ? libelle.substring(0, 100) : libelle);
        partenaireRepository.save(e);
    }

    private void seedPeriodesActivite() {
        if (periodeActiviteRepository.count() > 0) {
            return;
        }
        savePeriodeActivite("Trimestre 1");
        savePeriodeActivite("Trimestre 2");
        savePeriodeActivite("Annuel");
    }

    private void savePeriodeActivite(String libelle) {
        PeriodeActivite e = new PeriodeActivite();
        e.setLibellePeriodeActivite(libelle.length() > 50 ? libelle.substring(0, 50) : libelle);
        periodeActiviteRepository.save(e);
    }

    private void seedPeriodicites() {
        if (periodiciteRepository.count() > 0) {
            return;
        }
        savePeriodicite("Hebdomadaire");
        savePeriodicite("Mensuelle");
        savePeriodicite("Annuelle");
    }

    private void savePeriodicite(String libelle) {
        Periodicite e = new Periodicite();
        e.setLibellePeriodicite(libelle.length() > 15 ? libelle.substring(0, 15) : libelle);
        periodiciteRepository.save(e);
    }

    private void seedRegimes() {
        if (regimealphabetisationRepository.count() > 0) {
            return;
        }
        saveRegime("Régime accéléré");
        saveRegime("Régime classique");
        saveRegime("Régime modulaire");
    }

    private void saveRegime(String libelle) {
        Regimealphabetisation e = new Regimealphabetisation();
        e.setLibelleRegimeAlpha(libelle.length() > 100 ? libelle.substring(0, 100) : libelle);
        regimealphabetisationRepository.save(e);
    }

    private void seedStatutsPersonnel() {
        if (statutPersonnelRepository.count() > 0) {
            return;
        }
        saveStatutPersonnel("Volontaire");
        saveStatutPersonnel("Contractuel");
        saveStatutPersonnel("Fonctionnaire détaché");
    }

    private void saveStatutPersonnel(String libelle) {
        StatutPersonnel e = new StatutPersonnel();
        e.setLibelleStatutPersonnel(libelle.length() > 50 ? libelle.substring(0, 50) : libelle);
        statutPersonnelRepository.save(e);
    }

    private void seedSupportsDidactiques() {
        if (supportDidactiqueRepository.count() > 0) {
            return;
        }
        saveSupportDidactique("Guide animateur");
        saveSupportDidactique("Fiches progression");
        saveSupportDidactique("Évaluation formative");
    }

    private void saveSupportDidactique(String libelle) {
        SupportDidactique e = new SupportDidactique();
        e.setLibelleSupportDidactique(libelle.length() > 50 ? libelle.substring(0, 50) : libelle);
        supportDidactiqueRepository.save(e);
    }

    private void seedTypesAlpha() {
        if (typeAlphaRepository.count() > 0) {
            return;
        }
        saveTypeAlpha("Centre communautaire");
        saveTypeAlpha("Centre intégré");
        saveTypeAlpha("Poste d’alphabétisation");
    }

    private void saveTypeAlpha(String libelle) {
        TypeAlpha e = new TypeAlpha();
        e.setLibelleTypeAlpha(libelle.length() > 50 ? libelle.substring(0, 50) : libelle);
        typeAlphaRepository.save(e);
    }

    private void seedTypesDocument() {
        if (typeDocumentRepository.count() > 0) {
            return;
        }
        saveTypeDocument("Procès-verbal");
        saveTypeDocument("Rapport d’activité");
        saveTypeDocument("Contrat / convention");
    }

    private void saveTypeDocument(String libelle) {
        TypeDocument e = new TypeDocument();
        e.setLibelleTypeDocument(libelle.length() > 100 ? libelle.substring(0, 100) : libelle);
        typeDocumentRepository.save(e);
    }
}
