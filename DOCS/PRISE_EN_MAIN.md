# PRISM — Prise en main (équipe / nouveau développeur)

Guide unique pour installer, lancer en local, appliquer les migrations SQL et déployer sur le VPS.

| Dépôt | Rôle | Chemin type |
|--------|------|-------------|
| **prism** | API Spring Boot (WAR), scripts SQL, config Apache | `…/AENF/prism-backend/prism` |
| **prism_front** | Application Angular 17 | `…/AENF/prism-frontend` |

**Autres docs :**

- Front — roadmap d’intégration : `prism-frontend/DOCS/ROADMAP.md`
- Mémoire architecte : `AENF/.docs/` (source de vérité)
- Apache (staging VPS) : `prism/deploy/apache/dcspa-prism-port91.conf`

---

## Prérequis

| Outil | Version indicative |
|--------|-------------------|
| **Node.js** | LTS compatible Angular 17 |
| **npm** | fourni avec Node |
| **Java** | 17+ (Spring Boot 4 / projet Maven) |
| **Maven** | 3.9+ |
| **MySQL** | 8.x, base `prism_bd` |

---

## 1. Base de données

1. Créer la base MySQL `prism_bd` (schéma initial selon votre dump / scripts d’équipe).
2. Appliquer les migrations incrémentales dans l’ordre chronologique du nom de fichier :

   ```text
   prism/db/migration-*.sql
   ```

   Exemples récents : `migration-saisie-workflow-validateurs-2026.sql` (idempotent), `migration-visite-niveau-alpha-2026.sql`, etc.

3. En **local**, le profil `local` peut utiliser `ddl-auto=update` (voir `application-local.properties`). En **staging/prod**, `ddl-auto=validate` — les colonnes doivent déjà exister en base.

---

## 2. Backend (prism)

### Configuration

| Fichier | Usage |
|---------|--------|
| `application.properties` | Commun ; profil par défaut si non précisé |
| `application-local.properties` | Dev local : MySQL `root`, port `8080`, CORS `localhost` |
| `application-staging.properties` | VPS / Tomcat : variables d’environnement |

**Profil local (recommandé en dev) :**

```powershell
cd prism
$env:SPRING_PROFILES_ACTIVE = "local"
$env:JWT_SECRET = "votre-secret-local"
mvn spring-boot:run
```

API : `http://localhost:8080` — préfixe des contrôleurs : `/api/...`

**Build WAR :**

```powershell
mvn clean package -DskipTests
# Artefact : target/prism.war
```

### Variables utiles (staging / serveur)

| Variable | Rôle |
|----------|------|
| `SPRING_PROFILES_ACTIVE` | `staging` |
| `SPRING_DATASOURCE_URL` | JDBC MySQL |
| `SPRING_DATASOURCE_USERNAME` / `PASSWORD` | Compte BDD |
| `JWT_SECRET` | Secret JWT (obligatoire en staging) |
| `SERVER_PORT` | Port embarqué si exécution jar ; sous Tomcat, le connecteur Tomcat prime |

Après modification du code ou ajout d’endpoints : **redémarrer** `spring-boot:run` ou **redéployer** le WAR sur Tomcat.

---

## 3. Frontend (prism-frontend)

### Installation des dépendances

```powershell
cd prism-frontend
npm ci
```

> **Windows + OneDrive :** si `npm ci` échoue avec `EPERM` sur `esbuild.exe`, un processus verrouille le fichier (`ng serve`, antivirus, synchro OneDrive). Arrêter les processus puis relancer :
>
> ```powershell
> Get-Process node,esbuild -ErrorAction SilentlyContinue | Stop-Process -Force
> npm ci
> ```
>
> À long terme : éviter `node_modules` sous OneDrive (déplacer le clone ou exclure le dossier de la synchro).

### Dev local

```powershell
npm start
# ou : ng serve
```

- UI : `http://localhost:4200`
- API : `src/environments/environment.ts` → `apiBaseUrl: 'http://localhost:8080'`

Le backend doit tourner avec CORS autorisé pour `localhost` (profil `local`).

### Build staging (VPS Apache `/dcspa/`)

```powershell
npm run build -- --configuration=staging
```

- `baseHref` : `/dcspa/`
- `environment.staging.ts` : `apiBaseUrl: '/dcspa'` (Apache proxy vers Tomcat)

Déployer le contenu de `dist/prism_front/browser/` (ou équivalent selon `angular.json`) vers `/var/www/dcspa` sur le serveur.

### Tests

```powershell
npm test
mvn test   # depuis prism
```

---

## 4. Déploiement VPS (résumé)

Architecture cible documentée dans `deploy/apache/dcspa-prism-port91.conf` :

| Composant | Détail |
|-----------|--------|
| **Apache** | Port **91**, alias statique `/dcspa` → `/var/www/dcspa` |
| **Proxy API** | `/dcspa/api/` → `http://127.0.0.1:8081/prism/api/` |
| **Tomcat** | WAR `prism.war` → contexte `/prism`, connecteur **8081** |

**Ordre type :**

1. `npm run build -- --configuration=staging` → copier les fichiers statiques vers `/var/www/dcspa`
2. `mvn clean package -DskipTests` → copier `target/prism.war` vers Tomcat
3. Configurer `SPRING_PROFILES_ACTIVE=staging` + secrets BDD/JWT sur Tomcat
4. Activer le vhost Apache (`a2ensite`, `reload apache2`)

Vérifier en navigateur : `http://<ip>:91/dcspa/` et les appels réseau vers `/dcspa/api/...`.

---

## 5. Dépannage fréquent

| Symptôme | Piste |
|----------|--------|
| `EPERM` / `esbuild.exe` sous `npm ci` | Arrêter `ng serve` / processus `node` et `esbuild` (voir §3) |
| 404 sur un nouvel endpoint `/api/...` | Backend non redémarré ou WAR non redéployé |
| Erreur MySQL **#1060** (colonne déjà là) | Réexécuter le script idempotent ou ignorer si la migration est déjà passée |
| CORS en local | Profil `local` + origine `http://localhost:4200` |
| Front staging appelle la mauvaise API | Rebuild avec `--configuration=staging` et vider le cache navigateur |

---

## 6. Contacts / conventions

- Migrations SQL : toujours versionner dans `prism/db/` avec un nom daté explicite.
- Roadmap fonctionnelle front ↔ API : `prism-frontend/DOCS/ROADMAP.md` (mise à jour manuelle après chaque lot).
- Architecture / API / BDD : voir `../../.docs/` à la racine AENF.

*Dernière mise à jour : 2026-05-17*
