# Migrations Flyway (PRISM)

Les scripts `V*__*.sql` sont exécutés **automatiquement au démarrage** du backend lorsque Flyway est activé.

## Configuration

| Profil | `spring.flyway.enabled` | `ddl-auto` |
|--------|-------------------------|------------|
| local (défaut) | `false` (`SPRING_FLYWAY_ENABLED`) | `update` |
| staging / prod | `true` | `validate` |

Variable d'environnement : **`SPRING_FLYWAY_ENABLED`** (`true` / `false`).

## Nouvelle migration

1. Ajouter `src/main/resources/db/migration/V7__description_courte.sql` (numéro suivant).
2. Script **idempotent** si possible (voir `prism_add_column_if_missing` en V1).
3. Optionnel : copier une version miroir dans `scripts/sql/` pour exécution manuelle DBA.

## Base existante (premier déploiement Flyway)

`baseline-on-migrate=true` + `baseline-version=0` : une base déjà peuplée reçoit un baseline puis les migrations non encore enregistrées dans `flyway_schema_history`.

## Hors Flyway

- `scripts/sql/create_nebdev_super_root.sql` : seed RBAC manuel / initialiseur Java, pas au démarrage prod.
- Scripts historiques dans `db/*.sql` : maintenance manuelle, non intégrés à Flyway.
