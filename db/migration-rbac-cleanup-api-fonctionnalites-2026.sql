-- Nettoyage RBAC : supprime uniquement les fonctionnalités auto-générées (module = 'API')
-- qui ne font pas partie du catalogue officiel menu + activités centre.
--
-- CONSERVÉ (volontairement) :
--   - Les 6 permissions : CREER, LIRE, MODIFIER, SUPPRIMER, VALIDER, EXPORTER
--   - Toutes les fonctionnalités hors module 'API' (menu, activités, ancien seed CAMPAGNE/CENTRE…)
--   - Les liaisons role_fonctionnalite_permission sur le catalogue conservé
--
-- Exécuter une fois après prism.rbac.auto-api-fonctionnalites=false

-- Aperçu (optionnel) :
-- SELECT code_fonctionnalite, libelle_fonctionnalite FROM fonctionnalite
-- WHERE module = 'API' AND code_fonctionnalite NOT IN (...liste ci-dessous...);

DELETE rfp FROM role_fonctionnalite_permission rfp
INNER JOIN fonctionnalite f ON f.id_fonctionnalite = rfp.id_fonctionnalite
WHERE f.module = 'API'
  AND f.code_fonctionnalite NOT IN (
    'DASHBOARD', 'CENTRES_ALPHA', 'CENTRES_CEC', 'CENTRES_CP', 'CENTRES_SIE',
    'PERSONNEL', 'PROMOTEUR',
    'APPRENANT_EFFECTIF', 'APPRENANT_ABANDON', 'APPRENANT_PASSAGE', 'APPRENANT_HANDICAP',
    'APPRENANT_ADMIS_CEPE', 'APPRENANT_INTEGRES_FORMEL_CP', 'APPRENANT_ADMIS_TEST_INTEGRATION_CP',
    'PARAMETRAGE_GEOGRAPHIE', 'PARAMETRAGE_CENTRES_AUTORISATIONS', 'PARAMETRAGE_PEDAGOGIE',
    'PARAMETRAGE_ACTIVITES_CENTRE', 'PARAMETRAGE_DOCUMENTS', 'PARAMETRAGE_AUTRES',
    'ADMIN_UTILISATEURS', 'ADMIN_ACTEURS', 'ADMIN_ROLE_PERMISSIONS',
    'POINTS_VISITES', 'VALIDATION_VISITES_CONSEILLER', 'SUIVI_CONSEILLER', 'SUIVI_SUPERVISEUR',
    'SUIVI_IEPP', 'SUIVI_CENTRALE',
    'ACTIVITES_CENTRE_PARTENARIAT', 'ACTIVITES_CENTRE_PERFORMANCE', 'ACTIVITES_CENTRE_CONTROLE',
    'ACTIVITES_CENTRE_EVALUATION', 'ACTIVITES_CENTRE_INFOS', 'SAISIE_DONNEES'
  );

DELETE FROM fonctionnalite
WHERE module = 'API'
  AND code_fonctionnalite NOT IN (
    'DASHBOARD', 'CENTRES_ALPHA', 'CENTRES_CEC', 'CENTRES_CP', 'CENTRES_SIE',
    'PERSONNEL', 'PROMOTEUR',
    'APPRENANT_EFFECTIF', 'APPRENANT_ABANDON', 'APPRENANT_PASSAGE', 'APPRENANT_HANDICAP',
    'APPRENANT_ADMIS_CEPE', 'APPRENANT_INTEGRES_FORMEL_CP', 'APPRENANT_ADMIS_TEST_INTEGRATION_CP',
    'PARAMETRAGE_GEOGRAPHIE', 'PARAMETRAGE_CENTRES_AUTORISATIONS', 'PARAMETRAGE_PEDAGOGIE',
    'PARAMETRAGE_ACTIVITES_CENTRE', 'PARAMETRAGE_DOCUMENTS', 'PARAMETRAGE_AUTRES',
    'ADMIN_UTILISATEURS', 'ADMIN_ACTEURS', 'ADMIN_ROLE_PERMISSIONS',
    'POINTS_VISITES', 'VALIDATION_VISITES_CONSEILLER', 'SUIVI_CONSEILLER', 'SUIVI_SUPERVISEUR',
    'SUIVI_IEPP', 'SUIVI_CENTRALE',
    'ACTIVITES_CENTRE_PARTENARIAT', 'ACTIVITES_CENTRE_PERFORMANCE', 'ACTIVITES_CENTRE_CONTROLE',
    'ACTIVITES_CENTRE_EVALUATION', 'ACTIVITES_CENTRE_INFOS', 'SAISIE_DONNEES'
  );

-- Contrôle après exécution :
-- SELECT module, COUNT(*) FROM fonctionnalite GROUP BY module;
-- SELECT code_permission, COUNT(*) FROM permission p
--   JOIN role_fonctionnalite_permission rfp ON rfp.id_permission = p.id_permission
--   GROUP BY code_permission;
