# PRISM — API Spring Boot

Application métier MENA / DCSPA (WAR, MySQL, JWT).

## Démarrage rapide

Voir **[DOCS/PRISE_EN_MAIN.md](DOCS/PRISE_EN_MAIN.md)** : installation, profils Spring, migrations `db/`, build Maven, déploiement VPS (Apache + Tomcat).

```powershell
$env:SPRING_PROFILES_ACTIVE = "local"
mvn spring-boot:run
```

Front Angular associé : dépôt **prism_front** (`FRONTEND/prism_front`).
