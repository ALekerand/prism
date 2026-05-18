package com.dcspa.prism.controller.support;

import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.AnneScolaire;
import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.entity.AspectAAmeliorer;
import com.dcspa.prism.entity.AutoriteAutorisation;
import com.dcspa.prism.entity.Campagne;
import com.dcspa.prism.entity.CategorieAppui;
import com.dcspa.prism.entity.Cec;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Civilite;
import com.dcspa.prism.entity.Communaute;
import com.dcspa.prism.entity.Commune;
import com.dcspa.prism.entity.Competence;
import com.dcspa.prism.entity.Cp;
import com.dcspa.prism.entity.Departement;
import com.dcspa.prism.entity.Designation;
import com.dcspa.prism.entity.Difficulte;
import com.dcspa.prism.entity.Diplome;
import com.dcspa.prism.entity.Discipline;
import com.dcspa.prism.entity.DomaineActivite;
import com.dcspa.prism.entity.Drena;
import com.dcspa.prism.entity.Fonction;
import com.dcspa.prism.entity.Fonctionnalite;
import com.dcspa.prism.entity.Impact;
import com.dcspa.prism.entity.Infrastructure;
import com.dcspa.prism.entity.Iep;
import com.dcspa.prism.entity.LangueApprentissage;
import com.dcspa.prism.entity.Manuel;
import com.dcspa.prism.entity.MaterielsPedagogique;
import com.dcspa.prism.entity.MilieuImplantation;
import com.dcspa.prism.entity.Ministere;
import com.dcspa.prism.entity.Modealphabetisation;
import com.dcspa.prism.entity.Naturecentre;
import com.dcspa.prism.entity.NatureDocument;
import com.dcspa.prism.entity.NiveauAlpha;
import com.dcspa.prism.entity.NiveauControle;
import com.dcspa.prism.entity.NiveauCp;
import com.dcspa.prism.entity.NiveauEvaluation;
import com.dcspa.prism.entity.NiveauPersonnel;
import com.dcspa.prism.entity.NiveauSieCec;
import com.dcspa.prism.entity.Ong;
import com.dcspa.prism.entity.Partenaire;
import com.dcspa.prism.entity.Particulier;
import com.dcspa.prism.entity.Permission;
import com.dcspa.prism.entity.PeriodeActivite;
import com.dcspa.prism.entity.PeriodeEvaluation;
import com.dcspa.prism.entity.Periodicite;
import com.dcspa.prism.entity.Personnel;
import com.dcspa.prism.entity.Personnemorale;
import com.dcspa.prism.entity.Personnephysique;
import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.entity.Ptf;
import com.dcspa.prism.entity.Programme;
import com.dcspa.prism.entity.Regimealphabetisation;
import com.dcspa.prism.entity.Region;
import com.dcspa.prism.entity.Sie;
import com.dcspa.prism.entity.SocieteCivile;
import com.dcspa.prism.entity.SousPrefecture;
import com.dcspa.prism.entity.StatutPersonnel;
import com.dcspa.prism.entity.StructureFormationCertification;
import com.dcspa.prism.entity.SupportDidactique;
import com.dcspa.prism.entity.TauxEvaluation;
import com.dcspa.prism.entity.ThemeEvaluation;
import com.dcspa.prism.entity.TypeAlpha;
import com.dcspa.prism.entity.TypeDocument;
import com.dcspa.prism.entity.TypePersonneMorale;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.hibernate.ObjectNotFoundException;

/**
 * Réponses API format B : références sous forme d'objets {@code { id, code, libelle, ... }} au lieu de simples id.
 */
public final class ReferentielEnricher {

	private ReferentielEnricher() {
	}

	/**
	 * @param refPascalName nom court type référentiel en PascalCase (ex. {@code PeriodeEvaluation}) → clé JSON camelCase
	 *                      {@code periodeEvaluation}
	 */
	public static void putRef(Map<String, Object> row, String refPascalName, Object association) {
		row.put(firstCharLower(refPascalName), toRef(association));
	}

	public static Map<String, Object> toRef(Object association) {
		if (association == null) {
			return null;
		}
		Object entity;
		try {
			entity = Hibernate.unproxy(association);
		} catch (LazyInitializationException | EntityNotFoundException | ObjectNotFoundException ex) {
			// FK orpheline ou proxy hors session : renvoyer au moins l'id pour eviter une 500.
			return refIdOnly(association);
		}
		return switch (entity) {
			case Alpha a -> ref3(a.getId(), a.getCodeAlpha(), a.getLibelleAlpha());
			case AppRole r -> ref3(r.getId(), r.getCodeRole(), r.getLibelleRole());
			case AnneScolaire a -> refAnneeScolaire(a);
			case AspectAAmeliorer aa -> ref3(aa.getId(), aa.getCodeAspectAAmeliorer(), aa.getLibelleAspectAAmeliorer());
			case AutoriteAutorisation auth -> ref3(auth.getId(), auth.getCodeAutorisation(), auth.getLibelleAutoriteAutorisation());
			case Campagne camp -> refCampagne(camp);
			case Centre c -> ref3(c.getId(), c.getCodeCentre(), c.getLocalisationCentre());
			case Cp cp -> refCentreSubtype(cp.getCentre(), cp.getId());
			case Cec cec -> refCentreSubtype(cec.getCentre(), cec.getId());
			case Sie sie -> refCentreSubtype(sie.getCentre(), sie.getId());
			case CategorieAppui x -> ref3(x.getId(), x.getCodeCategorieAppui(), x.getLibelleCategorieAppui());
			case Communaute comm -> refCommunaute(comm);
			case Departement dep -> ref3(dep.getId(), dep.getCodeDepartement(), dep.getNomDepartement());
			case Diplome dip -> ref3(dip.getId(), dip.getCodeDiplome(), dip.getLibelleDiplome());
			case Drena dr -> ref3(dr.getId(), dr.getCodeDrena(), dr.getNomDrena());
			case Iep iep -> ref3(iep.getId(), iep.getCodeIep(), iep.getNomIep());
			case Fonctionnalite fn -> ref3(fn.getId(), fn.getCodeFonctionnalite(), fn.getLibelleFonctionnalite());
			case Infrastructure inf -> ref3(inf.getId(), inf.getCodeInfrastructure(), inf.getLibelleInfrastructure());
			case LangueApprentissage lang -> refLangueApprentissage(lang);
			case Ministere min -> refMinistere(min);
			case Modealphabetisation mo -> refModeAlphabetisation(mo);
			case Naturecentre nat -> ref3(nat.getId(), nat.getCodeNatureCentre(), nat.getLibelleNatureCentre());
			case NiveauCp ncp -> ref3(ncp.getId(), null, ncp.getLibelleNiveauCp());
			case NiveauSieCec nsie -> ref3(nsie.getId(), nsie.getCodeNiveauSie(), nsie.getLibelleNiveauSie());
			case Ong ong -> refOng(ong);
			case Partenaire p -> ref3(p.getId(), p.getCodePartenaire(), p.getLibellePartenaire());
			case Particulier par -> refParticulier(par);
			case Periodicite per -> ref3(per.getId(), per.getCodePeriodicite(), per.getLibellePeriodicite());
			case Permission perm -> ref3(perm.getId(), perm.getCodePermission(), perm.getLibellePermission());
			case Personnel pers -> refPersonnel(pers);
			case Personnemorale pm -> refPersonneMorale(pm);
			case Personnephysique pp -> refPersonnePhysique(pp);
			case Promoteur pr -> refPromoteur(pr);
			case Ptf ptf -> refPtf(ptf);
			case Competence c -> ref3(c.getId(), c.getCodeCompetence(), c.getLibelleCompetence());
			case PeriodeActivite p -> ref3(p.getId(), p.getCodePeriodeActivite(), p.getLibellePeriodeActivite());
			case PeriodeEvaluation pe -> ref3(pe.getId(), pe.getCodePeriodeEvaluation(), pe.getLibellePeriodeEvaluation());
			case NiveauEvaluation n -> ref3(n.getId(), n.getCodeNiveauEvaluation(), n.getLibelleNiveauEvaluation());
			case ThemeEvaluation t -> refThemeEvaluation(t);
			case TauxEvaluation te -> ref3(te.getId(), te.getCodeTauxEvaluation(), te.getLibelleTauxEvaluation());
			case Discipline d -> ref3(d.getId(), d.getCodeDiscipline(), d.getLibelleDiscipline());
			case Manuel m -> ref3(m.getId(), m.getCodeManuel(), m.getLibelleManuel());
			case MaterielsPedagogique mp -> ref3(mp.getId(), mp.getCodeMaterielPedagogique(), mp.getLibelleMaterielPedagogique());
			case NiveauControle nvc -> ref3(nvc.getId(), nvc.getCodeNiveauControle(), nvc.getLibelleNiveauControle());
			case NiveauAlpha nv -> ref3(nv.getId(), nv.getCodeNiveauAlpha(), nv.getLibelleNiveauAlpha());
			case NiveauPersonnel nivp -> ref3(nivp.getId(), nivp.getCodeNiveauPersonnel(), nivp.getLibelleNiveauPersonnel());
			case SousPrefecture s -> ref3(s.getId(), s.getCodeSousPrefecture(), s.getNomSousPrefecture());
			case SocieteCivile sc -> refSocieteCivile(sc);
			case MilieuImplantation mil -> ref3(mil.getId(), mil.getCodeMilieuImplentation(), mil.getLibelleTypeImplentation());
			case Commune com -> ref3(com.getId(), com.getCodeCommune(), com.getNomCommune());
			case Designation des -> ref3(des.getId(), des.getCodeDesignation(), des.getLibelleDesignation());
			case Difficulte diff -> ref3(diff.getId(), diff.getCodeDifficulte(), diff.getLibelleDifficulte());
			case DomaineActivite da -> ref3(da.getId(), da.getCodeDomaineActivite(), da.getLibelleDomaineActivite());
			case Civilite civi -> ref3(civi.getId(), civi.getCodeCivilite(), civi.getLibelleCivilite());
			case Fonction fon -> ref3(fon.getId(), fon.getCodeFonction(), fon.getLibelleFonction());
			case Impact imp -> ref3(imp.getId(), imp.getCodeImpact(), imp.getLibelleImpact());
			case NatureDocument nd -> ref3(nd.getId(), null, nd.getLibelleNatureDocument());
			case Programme prog -> ref3(prog.getId(), prog.getCodeProgramme(), null);
			case Regimealphabetisation reg -> ref3(reg.getId(), null, reg.getLibelleRegimeAlpha());
			case Region region -> ref3(region.getId(), region.getCodeRegion(), region.getLibelleRegion());
			case SupportDidactique sd -> ref3(sd.getId(), null, sd.getLibelleSupportDidactique());
			case StatutPersonnel stp -> ref3(stp.getId(), stp.getCodeStatutPersonnel(), stp.getLibelleStatutPersonnel());
			case StructureFormationCertification sfc -> ref3(sfc.getId(), sfc.getCodeStructureCertification(), sfc.getLibelleStructureCertification());
			case TypeAlpha ta -> ref3(ta.getId(), null, ta.getLibelleTypeAlpha());
			case TypeDocument td -> ref3(td.getId(), td.getCodeTypeDocument(), td.getLibelleTypeDocument());
			case TypePersonneMorale tpm -> ref3(tpm.getId(), null, tpm.getLibelle());
			default -> refIdOnly(entity);
		};
	}

	private static Map<String, Object> refCentreSubtype(Centre centre, Integer fallbackId) {
		if (centre == null) {
			return ref3(fallbackId, null, null);
		}
		return ref3(centre.getId(), centre.getCodeCentre(), centre.getLocalisationCentre());
	}

	private static Map<String, Object> refPromoteur(Promoteur p) {
		Map<String, Object> m = ref3(p.getId(), p.getCodePromoteur(), p.getLibellePromoteur());
		if (p.getTypePromoteur() != null) {
			m.put("typePromoteur", p.getTypePromoteur().name());
		}
		return m;
	}

	private static Map<String, Object> refPersonneMorale(Personnemorale pm) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", pm.getId());
		m.put("promoteur", pm.getPromoteur() != null ? refPromoteur(pm.getPromoteur()) : null);
		m.put("codePromoteur", pm.getCodePromoteur());
		m.put("libellePromoteur", pm.getLibellePromoteur());
		m.put("denomination", pm.getDenomination());
		m.put("nomProgramme", pm.getNomProgramme());
		m.put("nomRepresentantLegalStructure", pm.getNomRepresentantLegalStructure());
		m.put("contact", pm.getContact());
		m.put("boitePostale", pm.getBoitePostale());
		m.put("mail", pm.getMail());
		m.put("typePersonneMorale", pm.getTypePersonneMorale() != null ? toRef(pm.getTypePersonneMorale()) : null);
		return m;
	}

	private static Map<String, Object> refPersonnePhysique(Personnephysique p) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", p.getId());
		m.put("promoteur", p.getPromoteur() != null ? refPromoteur(p.getPromoteur()) : null);
		m.put("codePromoteur", p.getCodePromoteur());
		m.put("libellePromoteur", p.getLibellePromoteur());
		m.put("libellePersonnePhysique", p.getLibellePersonnePhysique());
		m.put("nom", p.getNom());
		m.put("prenom", p.getPrenom());
		m.put("contact", p.getContact());
		m.put("fonction", p.getFonction());
		m.put("sexe", p.getSexe());
		m.put("dateNaissance", p.getDateNaissance());
		m.put("anciennete", p.getAnciennete());
		m.put("boitePostale", p.getBoitePostale());
		m.put("niveauEtudes", p.getNiveauEtudes());
		m.put("civilite", p.getCivilite());
		return m;
	}

	private static Map<String, Object> refCampagne(Campagne c) {
		Map<String, Object> m = ref3(c.getId(), c.getCodeCampagne(), null);
		m.put("dateDebutCampagne", c.getDateDebutCampagne());
		m.put("dateFinCampagne", c.getDateFinCampagne());
		m.put("etatCampagne", c.getEtatCampagne());
		return m;
	}

	private static void putPromoteurPersonneMoraleChildScalars(
			Map<String, Object> m,
			Integer id,
			Personnemorale pm,
			String codePromoteur,
			String libellePromoteur,
			String denomination,
			String nomProgramme,
			String nomRepresentantLegalStructure,
			String contact,
			String boitePostale,
			String mail) {
		m.put("id", id);
		m.put("personnemorale", pm != null ? toRef(pm) : null);
		m.put("codePromoteur", codePromoteur);
		m.put("libellePromoteur", libellePromoteur);
		m.put("denomination", denomination);
		m.put("nomProgramme", nomProgramme);
		m.put("nomRepresentantLegalStructure", nomRepresentantLegalStructure);
		m.put("contact", contact);
		m.put("boitePostale", boitePostale);
		m.put("mail", mail);
	}

	private static Map<String, Object> refCommunaute(Communaute c) {
		Map<String, Object> m = new LinkedHashMap<>();
		putPromoteurPersonneMoraleChildScalars(m, c.getId(), c.getPersonnemorale(),
				c.getCodePromoteur(), c.getLibellePromoteur(), c.getDenomination(),
				c.getNomProgramme(), c.getNomRepresentantLegalStructure(),
				c.getContact(), c.getBoitePostale(), c.getMail());
		m.put("libelleCommunaute", c.getLibelleCommunaute());
		return m;
	}

	private static Map<String, Object> refSocieteCivile(SocieteCivile c) {
		Map<String, Object> m = new LinkedHashMap<>();
		putPromoteurPersonneMoraleChildScalars(m, c.getId(), c.getPersonnemorale(),
				c.getCodePromoteur(), c.getLibellePromoteur(), c.getDenomination(),
				c.getNomProgramme(), c.getNomRepresentantLegalStructure(),
				c.getContact(), c.getBoitePostale(), c.getMail());
		m.put("libelleSocieteCivile", c.getLibelleSocieteCivile());
		return m;
	}

	private static Map<String, Object> refOng(Ong c) {
		Map<String, Object> m = new LinkedHashMap<>();
		putPromoteurPersonneMoraleChildScalars(m, c.getId(), c.getPersonnemorale(),
				c.getCodePromoteur(), c.getLibellePromoteur(), c.getDenomination(),
				c.getNomProgramme(), c.getNomRepresentantLegalStructure(),
				c.getContact(), c.getBoitePostale(), c.getMail());
		m.put("libelleOng", c.getLibelleOng());
		return m;
	}

	private static Map<String, Object> refPtf(Ptf c) {
		Map<String, Object> m = new LinkedHashMap<>();
		putPromoteurPersonneMoraleChildScalars(m, c.getId(), c.getPersonnemorale(),
				c.getCodePromoteur(), c.getLibellePromoteur(), c.getDenomination(),
				c.getNomProgramme(), c.getNomRepresentantLegalStructure(),
				c.getContact(), c.getBoitePostale(), c.getMail());
		m.put("libellePtf", c.getLibellePtf());
		return m;
	}

	private static Map<String, Object> refMinistere(Ministere c) {
		Map<String, Object> m = new LinkedHashMap<>();
		putPromoteurPersonneMoraleChildScalars(m, c.getId(), c.getPersonnemorale(),
				c.getCodePromoteur(), c.getLibellePromoteur(), c.getDenomination(),
				c.getNomProgramme(), c.getNomRepresentantLegalStructure(),
				c.getContact(), c.getBoitePostale(), c.getMail());
		m.put("libelleMinistere", c.getLibelleMinistere());
		return m;
	}

	private static Map<String, Object> refParticulier(Particulier c) {
		Map<String, Object> m = new LinkedHashMap<>();
		putPromoteurPersonneMoraleChildScalars(m, c.getId(), c.getPersonnemorale(),
				c.getCodePromoteur(), c.getLibellePromoteur(), c.getDenomination(),
				c.getNomProgramme(), c.getNomRepresentantLegalStructure(),
				c.getContact(), c.getBoitePostale(), c.getMail());
		m.put("libelleParticulier", c.getLibelleParticulier());
		return m;
	}

	private static Map<String, Object> refLangueApprentissage(LangueApprentissage la) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", la.getId());
		m.put("centre", toRef(la.getIdCentre()));
		m.put("libelleLangue", la.getLibelleLangue());
		return m;
	}

	private static Map<String, Object> refModeAlphabetisation(Modealphabetisation mo) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", mo.getId());
		m.put("alpha", toRef(mo.getIdCentre()));
		m.put("codeModealpha", mo.getCodeModealpha());
		m.put("libelleModealpha", mo.getLibelleModealpha());
		return m;
	}

	private static Map<String, Object> refPersonnel(Personnel pers) {
		StringBuilder lib = new StringBuilder();
		if (pers.getNomPersonnel() != null) {
			lib.append(pers.getNomPersonnel());
		}
		if (pers.getPrenomsPersonnel() != null && !pers.getPrenomsPersonnel().isBlank()) {
			if (!lib.isEmpty()) {
				lib.append(' ');
			}
			lib.append(pers.getPrenomsPersonnel());
		}
		String libStr = lib.isEmpty() ? null : lib.toString();
		return ref3(pers.getId(), pers.getCodePersonnel(), libStr);
	}

	private static Map<String, Object> ref3(Integer id, String code, String libelle) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", id);
		if (code != null) {
			m.put("code", code);
		}
		if (libelle != null) {
			m.put("libelle", libelle);
		}
		return m;
	}

	private static Map<String, Object> refThemeEvaluation(ThemeEvaluation t) {
		Map<String, Object> m = ref3(t.getId(), t.getCodeThemeEvaluation(), t.getLibelleThemeEvaluation());
		if (t.getNiveau() != null) {
			m.put("niveau", t.getNiveau());
		}
		return m;
	}

	private static Map<String, Object> refAnneeScolaire(AnneScolaire a) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", a.getId());
		if (a.getCodeAnneeScolaire() != null) {
			m.put("code", a.getCodeAnneeScolaire());
		}
		LocalDate d0 = a.getDebutAnneeScolaire();
		LocalDate d1 = a.getFinAnneeScolaire();
		if (d0 != null && d1 != null) {
			m.put("libelle", d0 + " → " + d1);
		}
		m.put("debutAnneeScolaire", d0);
		m.put("finAnneeScolaire", d1);
		m.put("etatAnneeScolaire", a.getEtatAnneeScolaire());
		return m;
	}

	private static Map<String, Object> refIdOnly(Object entity) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("id", JpaAssociationIds.intIdOrNull(entity));
		return m;
	}

	private static String firstCharLower(String s) {
		if (s == null || s.isEmpty()) {
			return s;
		}
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}

	/**
	 * Sérialise une entité JPA au format B :
	 * <ul>
	 *   <li>les champs scalaires sont copiés tels quels ;</li>
	 *   <li>les FKs {@code @ManyToOne} / {@code @OneToOne} → objets imbriqués via {@link #toRef(Object)} ;</li>
	 *   <li>les collections {@code @OneToMany} / {@code @ManyToMany} et les champs {@code @JsonIgnore} / {@code @Transient}
	 *       sont ignorés (évite les soucis de proxy lazy).</li>
	 * </ul>
	 * Les champs {@code idXxx} renommés en {@code xxx} pour les associations (ex. {@code idAlpha} → {@code alpha}).
	 */
	public static Map<String, Object> toMap(Object entity) {
		if (entity == null) {
			return null;
		}
		Object real = Hibernate.unproxy(entity);
		Map<String, Object> m = new LinkedHashMap<>();
		for (Field f : collectFields(real.getClass())) {
			if (f.isAnnotationPresent(JsonIgnore.class) || f.isAnnotationPresent(Transient.class)) {
				continue;
			}
			if (f.isAnnotationPresent(OneToMany.class) || f.isAnnotationPresent(jakarta.persistence.ManyToMany.class)) {
				continue;
			}
			f.setAccessible(true);
			Object value;
			try {
				value = f.get(real);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException("Cannot read " + f.getName(), e);
			}
			boolean isAssociation = f.isAnnotationPresent(ManyToOne.class) || f.isAnnotationPresent(OneToOne.class);
			if (isAssociation) {
				m.put(stripIdPrefix(f.getName()), toRef(value));
			} else if (value instanceof Collection<?>) {
				continue;
			} else {
				m.put(f.getName(), value);
			}
		}
		return m;
	}

	public static List<Map<String, Object>> toMapList(Collection<?> entities) {
		return entities.stream().map(ReferentielEnricher::toMap).toList();
	}

	private static List<Field> collectFields(Class<?> type) {
		List<Field> fields = new java.util.ArrayList<>();
		Class<?> c = type;
		while (c != null && c != Object.class) {
			for (Field f : c.getDeclaredFields()) {
				if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
					continue;
				}
				fields.add(f);
			}
			c = c.getSuperclass();
		}
		return fields;
	}

	private static String stripIdPrefix(String name) {
		if (name != null && name.length() > 2 && name.startsWith("id") && Character.isUpperCase(name.charAt(2))) {
			return Character.toLowerCase(name.charAt(2)) + name.substring(3);
		}
		return name;
	}
}
