/**
 * Génère le document Word des spécifications serveur production PRISM.
 * Usage : node scripts/generate-spec-serveur-docx.mjs
 */
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import {
  AlignmentType,
  BorderStyle,
  Document,
  Footer,
  Header,
  HeadingLevel,
  Packer,
  PageNumber,
  Paragraph,
  ShadingType,
  Table,
  TableCell,
  TableRow,
  TextRun,
  WidthType,
} from 'docx';

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const OUT = path.join(__dirname, '..', 'DOCS', 'SPECIFICATIONS_SERVEUR_PRODUCTION_PRISM.docx');

const BRAND = '1F4E79';
const ACCENT = '2E75B6';
const LIGHT = 'D6E4F0';

function run(text, opts = {}) {
  return new TextRun({ text, font: 'Calibri', size: 22, ...opts });
}

function bold(text, opts = {}) {
  return run(text, { bold: true, ...opts });
}

function heading(text, level) {
  return new Paragraph({
    heading: level,
    spacing: { before: level === HeadingLevel.HEADING_1 ? 360 : 240, after: 120 },
    children: [new TextRun({ text, font: 'Calibri', bold: true, color: BRAND, size: level === HeadingLevel.HEADING_1 ? 32 : 26 })],
  });
}

function para(lines, spacing = { after: 120 }) {
  const children = [];
  lines.forEach((line, i) => {
    if (typeof line === 'string') children.push(run(line));
    else children.push(line);
  });
  return new Paragraph({ spacing, children });
}

function bullet(text, level = 0) {
  return new Paragraph({
    bullet: { level },
    spacing: { after: 60 },
    children: [run(text)],
  });
}

function cell(text, opts = {}) {
  const { header = false, width = 2340, colspan = 1 } = opts;
  return new TableCell({
    columnSpan: colspan,
    width: { size: width, type: WidthType.DXA },
    shading: header ? { fill: BRAND, type: ShadingType.CLEAR } : undefined,
    margins: { top: 80, bottom: 80, left: 120, right: 120 },
    children: [
      new Paragraph({
        children: [
          new TextRun({
            text,
            font: 'Calibri',
            size: 20,
            bold: header,
            color: header ? 'FFFFFF' : '000000',
          }),
        ],
      }),
    ],
  });
}

function table(headers, rows, colWidths) {
  const w = colWidths || headers.map(() => Math.floor(9360 / headers.length));
  const headerRow = new TableRow({
    children: headers.map((h, i) => cell(h, { header: true, width: w[i] })),
  });
  const dataRows = rows.map(
    (row) =>
      new TableRow({
        children: row.map((c, i) => cell(c, { width: w[i] })),
      })
  );
  return new Table({
    width: { size: 9360, type: WidthType.DXA },
    rows: [headerRow, ...dataRows],
  });
}

function spacer(after = 120) {
  return new Paragraph({ spacing: { after }, children: [] });
}

const doc = new Document({
  creator: 'Équipe PRISM / DCSPA',
  title: 'Spécifications serveur production — PRISM',
  description: 'Fiche technique pour déploiement production PRISM',
  styles: {
    default: {
      document: { run: { font: 'Calibri', size: 22 } },
    },
  },
  sections: [
    {
      properties: {
        page: {
          margin: { top: 1134, right: 1134, bottom: 1134, left: 1134 },
        },
      },
      headers: {
        default: new Header({
          children: [
            new Paragraph({
              alignment: AlignmentType.RIGHT,
              children: [run('PRISM — Spécifications serveur production', { size: 18, color: '666666', italics: true })],
            }),
          ],
        }),
      },
      footers: {
        default: new Footer({
          children: [
            new Paragraph({
              alignment: AlignmentType.CENTER,
              children: [
                run('Document confidentiel — DCSPA / DTSI — ', { size: 18, color: '666666' }),
                run('Page ', { size: 18, color: '666666' }),
                new TextRun({ children: [PageNumber.CURRENT], font: 'Calibri', size: 18, color: '666666' }),
                run(' / ', { size: 18, color: '666666' }),
                new TextRun({ children: [PageNumber.TOTAL_PAGES], font: 'Calibri', size: 18, color: '666666' }),
              ],
            }),
          ],
        }),
      },
      children: [
        // --- Page de garde ---
        new Paragraph({ spacing: { before: 2400 }, children: [] }),
        new Paragraph({
          alignment: AlignmentType.CENTER,
          spacing: { after: 200 },
          children: [new TextRun({ text: 'DCSPA — Direction de la Coopération et du Soutien Pédagogique aux AENF', font: 'Calibri', size: 22, color: ACCENT })],
        }),
        new Paragraph({
          alignment: AlignmentType.CENTER,
          spacing: { after: 400 },
          children: [new TextRun({ text: 'CAHIER DES CHARGES INFRASTRUCTURE', font: 'Calibri', size: 40, bold: true, color: BRAND })],
        }),
        new Paragraph({
          alignment: AlignmentType.CENTER,
          spacing: { after: 200 },
          children: [new TextRun({ text: 'Application PRISM', font: 'Calibri', size: 36, bold: true, color: BRAND })],
        }),
        new Paragraph({
          alignment: AlignmentType.CENTER,
          spacing: { after: 200 },
          children: [new TextRun({ text: 'Plateforme de suivi des activités des centres AENF', font: 'Calibri', size: 24, italics: true, color: '444444' })],
        }),
        new Paragraph({
          alignment: AlignmentType.CENTER,
          spacing: { before: 600, after: 200 },
          children: [new TextRun({ text: 'Déploiement en environnement de PRODUCTION', font: 'Calibri', size: 26, bold: true })],
        }),
        spacer(400),
        table(
          ['Référence', 'Valeur'],
          [
            ['Version document', '1.0'],
            ['Date', '21 juillet 2026'],
            ['Rédaction', 'Équipe technique PRISM / DTSI'],
            ['Classification', 'Usage interne — Infrastructure'],
            ['Périmètre', 'Serveur applicatif + base de données MySQL'],
          ],
          [3120, 6240]
        ),
        new Paragraph({ children: [new TextRun({ break: 1 })] }),

        // --- 1. Objet ---
        heading('1. Objet du document', HeadingLevel.HEADING_1),
        para([
          'Le présent document définit les ',
          bold('spécifications techniques minimales et optimisées'),
          ' du serveur requis pour héberger l’application ',
          bold('PRISM'),
          ' (frontend Angular 17 + API Spring Boot 4) en ',
          bold('production'),
          '. Il est destiné aux équipes ',
          bold('infrastructure'),
          ', ',
          bold('hébergement'),
          ' et ',
          bold('sécurité'),
          ' pour la commande, la mise en service et l’exploitation du serveur.',
        ]),
        para([
          'Les besoins ont été ',
          bold('dimensionnés de façon optimisée'),
          ' : allocation mémoire maîtrisée (8 Go RAM), espace disque confortable pour logs, sauvegardes et croissance des données (250 Go minimum), tout en conservant une marge pour les pics de charge (imports Excel, sessions simultanées).',
        ]),

        // --- 2. Synthèse ---
        heading('2. Synthèse exécutive', HeadingLevel.HEADING_1),
        table(
          ['Composant', 'Technologie', 'Rôle'],
          [
            ['Interface utilisateur', 'Angular 17 (fichiers statiques)', 'Application web navigateur — chemin public /dcspa/'],
            ['API métier', 'Java 21 + Spring Boot 4.0.3 (WAR)', 'REST JSON sécurisée JWT — chemin public /dcspa/api/'],
            ['Serveur applicatif', 'Apache Tomcat 10.x (instance dédiée)', 'Hébergement du WAR prism.war — port interne 8081'],
            ['Reverse proxy', 'Apache HTTP Server 2.4', 'Routage HTTP/HTTPS, fichiers statiques, proxy API'],
            ['Base de données', 'MySQL 8.x', 'Persistance — base prism_bd'],
          ],
          [2200, 3200, 3960]
        ),
        spacer(),
        para([bold('URL publique cible :'), run(' https://<domaine-production>/dcspa/')]),
        para([bold('API publique :'), run(' https://<domaine-production>/dcspa/api/...')]),

        // --- 3. Architecture ---
        heading('3. Architecture cible', HeadingLevel.HEADING_1),
        para(['Le trafic utilisateur transite exclusivement par ', bold('Apache'), ' (ports 80/443). Tomcat et MySQL ne sont ', bold('pas exposés'), ' sur Internet.']),
        spacer(80),
        table(
          ['Couche', 'Composant', 'Détail technique'],
          [
            ['Présentation', 'Apache — fichiers statiques', 'Répertoire /var/www/dcspa/ (build Angular, baseHref /dcspa/)'],
            ['Présentation', 'Apache — reverse proxy', 'ProxyPass /dcspa/api/ → http://127.0.0.1:8081/prism/api/'],
            ['Application', 'Tomcat tomcat-prism', 'Connecteur HTTP port 8081, contexte /prism, artefact prism.war'],
            ['Données', 'MySQL 8', 'Base prism_bd, charset utf8mb4, accès localhost ou réseau privé'],
          ],
          [1800, 2800, 4760]
        ),
        spacer(),
        para([bold('Schéma de flux :'), run(' Navigateur → Apache (443) → [ /dcspa/ → static | /dcspa/api/ → Tomcat:8081/prism ] → MySQL:3306')]),

        // --- 4. Specs matérielles ---
        heading('4. Spécifications matérielles (optimisées)', HeadingLevel.HEADING_1),
        para([
          'Configuration ',
          bold('minimum validée'),
          ' pour la production PRISM, avec marge d’exploitation (logs, sauvegardes locales temporaires, croissance BDD).',
        ]),
        spacer(80),
        table(
          ['Ressource', 'Spécification minimale', 'Justification'],
          [
            ['Processeur', '4 vCPU (x86_64)', 'API REST + JPA/Hibernate, imports Excel (Apache POI), sessions concurrentes'],
            ['Mémoire vive (RAM)', '8 Go', 'JVM 768 Mo max + Tomcat + MySQL buffer pool ~1,5 Go + Apache + OS (~2 Go marge)'],
            ['Stockage', '250 Go SSD minimum', 'OS, applications, logs Tomcat/Apache, dumps MySQL, marge croissance données 5 ans'],
            ['Type de disque', 'SSD ou NVMe', 'Performances I/O MySQL et temps de réponse API'],
            ['Réseau', '100 Mbit/s symétrique minimum', 'Accès multi-utilisateurs centres AENF sur le territoire'],
            ['Sauvegarde externe', 'Espace dédié hors serveur (SAN/NAS/cloud)', 'Les 250 Go incluent une marge locale ; les sauvegardes principales sont externalisées'],
          ],
          [2000, 2800, 4560]
        ),
        spacer(),
        heading('4.1 Répartition mémoire optimisée (8 Go)', HeadingLevel.HEADING_2),
        table(
          ['Composant', 'Allocation', 'Paramétrage'],
          [
            ['Système d’exploitation Linux', '1,0 Go', 'Debian 12 / Ubuntu 22.04 LTS'],
            ['Apache HTTP Server', '256 Mo', 'Workers mod_proxy + static'],
            ['Tomcat (overhead)', '384 Mo', 'Instance dédiée tomcat-prism'],
            ['JVM PRISM (heap)', '768 Mo max', 'CATALINA_OPTS : -Xms256m -Xmx768m -XX:MaxMetaspaceSize=160m'],
            ['MySQL 8 (InnoDB buffer pool)', '1,5 Go', 'innodb_buffer_pool_size=1536M'],
            ['Marge / pics (imports Excel)', '1,5 Go', 'Charge transitoire Apache POI'],
            ['Cache / buffers divers', '2,6 Go', 'Marge exploitation et monitoring'],
            ['TOTAL', '8 Go', '—'],
          ],
          [2800, 2200, 4360]
        ),

        heading('4.2 Répartition disque optimisée (250 Go)', HeadingLevel.HEADING_2),
        table(
          ['Partition / usage', 'Taille indicative', 'Contenu'],
          [
            ['Système (/)', '30 Go', 'OS, paquets, Tomcat, Java, Apache'],
            ['Applications (/opt)', '10 Go', 'Instances Tomcat, WAR, scripts'],
            ['Données web (/var/www)', '5 Go', 'Build Angular /var/www/dcspa/'],
            ['MySQL (/var/lib/mysql)', '80 Go', 'Base prism_bd + indexes + croissance'],
            ['Logs (/var/log)', '20 Go', 'Apache, Tomcat, MySQL — rotation obligatoire'],
            ['Sauvegardes locales (/var/backups)', '50 Go', 'Dumps MySQL quotidiens (7 jours glissants)'],
            ['Espace libre / marge', '55 Go', 'Déploiements, temporaires, extension'],
            ['TOTAL', '250 Go', '—'],
          ],
          [2600, 2200, 4560]
        ),

        // --- 5. Logiciels ---
        heading('5. Stack logicielle', HeadingLevel.HEADING_1),
        table(
          ['Logiciel', 'Version', 'Obligatoire prod', 'Remarque'],
          [
            ['Système d’exploitation', 'Debian 12 ou Ubuntu 22.04 LTS', 'Oui', 'amd64, UTF-8 fr_FR'],
            ['Java (JDK/JRE)', '21', 'Oui', 'Requis par Spring Boot 4 / pom.xml'],
            ['Apache Tomcat', '10.1.x', 'Oui', 'Instance dédiée, port 8081'],
            ['Apache HTTP Server', '2.4+', 'Oui', 'mod_proxy, mod_rewrite, mod_ssl'],
            ['MySQL Server', '8.0.x', 'Oui', 'Base prism_bd, utf8mb4'],
            ['Node.js', '18 ou 20 LTS', 'Non (build uniquement)', 'Machine CI ou poste développeur'],
            ['Maven', '3.9+', 'Non (build uniquement)', 'Génération du WAR en amont'],
          ],
          [2200, 1600, 1600, 3960]
        ),

        // --- 6. Réseau ---
        heading('6. Réseau, ports et pare-feu', HeadingLevel.HEADING_1),
        table(
          ['Port', 'Service', 'Exposition', 'Usage'],
          [
            ['80', 'Apache HTTP', 'Public (redirection HTTPS)', 'Redirection vers 443'],
            ['443', 'Apache HTTPS', 'Public', 'Front /dcspa/ + API /dcspa/api/'],
            ['8081', 'Tomcat PRISM', 'Localhost uniquement', 'Backend interne /prism'],
            ['3306', 'MySQL', 'Localhost / VLAN privé', 'Base prism_bd'],
            ['22', 'SSH', 'IP administration uniquement', 'Maintenance serveur'],
          ],
          [1200, 2200, 2600, 3360]
        ),
        spacer(),
        para([bold('Règle pare-feu :'), run(' autoriser 443 (et éventuellement 80) depuis le réseau utilisateurs ; bloquer 8081 et 3306 depuis Internet.')]),

        // --- 7. Variables ---
        heading('7. Configuration applicative', HeadingLevel.HEADING_1),
        heading('7.1 Variables d’environnement Tomcat', HeadingLevel.HEADING_2),
        table(
          ['Variable', 'Exemple / valeur', 'Obligatoire'],
          [
            ['SPRING_PROFILES_ACTIVE', 'staging', 'Oui'],
            ['SPRING_DATASOURCE_URL', 'jdbc:mysql://localhost:3306/prism_bd?useSSL=false&serverTimezone=UTC', 'Oui'],
            ['SPRING_DATASOURCE_USERNAME', 'prism_user', 'Oui'],
            ['SPRING_DATASOURCE_PASSWORD', '<secret fort>', 'Oui'],
            ['JWT_SECRET', '<secret ≥ 256 bits aléatoire>', 'Oui'],
            ['SPRING_FLYWAY_ENABLED', 'true', 'Oui'],
          ],
          [3200, 4160, 2000]
        ),
        heading('7.2 Paramètres MySQL recommandés', HeadingLevel.HEADING_2),
        bullet('innodb_buffer_pool_size = 1536M'),
        bullet('character-set-server = utf8mb4'),
        bullet('collation-server = utf8mb4_unicode_ci'),
        bullet('max_connections = 100 (ajuster selon charge réelle)'),
        bullet('Compte applicatif prism_user : droits limités à la base prism_bd uniquement'),

        heading('7.3 CORS et domaine production', HeadingLevel.HEADING_2),
        para(['Adapter les origines autorisées dans application-staging.properties pour le domaine définitif (ex. https://prism.dcspa.ci).']),
        para(['Mettre à jour environment.prod.ts côté frontend avec l’URL API de production avant le build final.']),

        // --- 8. Déploiement ---
        heading('8. Procédure de déploiement', HeadingLevel.HEADING_1),
        table(
          ['Étape', 'Action', 'Artefact / cible'],
          [
            ['1', 'Build backend (machine CI)', 'mvn clean package -DskipTests → target/prism.war'],
            ['2', 'Déploiement WAR', 'Copie vers webapps/ Tomcat, redémarrage tomcat-prism'],
            ['3', 'Build frontend (machine CI)', 'npm ci && npm run build -- --configuration=staging'],
            ['4', 'Déploiement front', 'Contenu dist/prism_front/browser/ → /var/www/dcspa/'],
            ['5', 'Configuration Apache', 'Snippet deploy/apache/dcspa-prism-port80.snippet.conf dans vhost'],
            ['6', 'Vérification', 'HTTPS, login, appels /dcspa/api/, logs Flyway OK'],
          ],
          [800, 4560, 4000]
        ),

        // --- 9. Sécurité ---
        heading('9. Exigences de sécurité', HeadingLevel.HEADING_1),
        bullet('Certificat TLS valide sur Apache (Let’s Encrypt ou PKI interne).'),
        bullet('Secrets (JWT, BDD) stockés hors dépôt Git — fichier setenv.sh ou gestionnaire de secrets.'),
        bullet('Stack traces désactivées en production (include-stacktrace=never).'),
        bullet('Authentification API par JWT Bearer — expiration 24 h.'),
        bullet('Sauvegardes MySQL chiffrées, externalisées, test de restauration mensuel.'),
        bullet('Mises à jour de sécurité OS et MySQL planifiées (fenêtre de maintenance).'),
        bullet('Accès SSH par clé, compte dédié déploiement, pas de root direct.'),

        // --- 10. Supervision ---
        heading('10. Supervision et maintenance', HeadingLevel.HEADING_1),
        table(
          ['Élément', 'Emplacement / action'],
          [
            ['Logs Apache', '/var/log/apache2/dcspa-*-access.log, *-error.log — rotation logrotate'],
            ['Logs Tomcat', '$CATALINA_BASE/logs/ — surveillance espace disque'],
            ['Logs MySQL', '/var/log/mysql/ — slow query log si perf dégradée'],
            ['Health check', 'http://127.0.0.1:8081/prism/actuator/health (accès local uniquement)'],
            ['Sauvegarde BDD', 'Dump quotidien automatisé → /var/backups + copie externe'],
            ['Redémarrage planifié', 'Fenêtre hebdomadaire si patches OS/MySQL'],
          ],
          [3200, 6160]
        ),

        // --- 11. Livrables infra ---
        heading('11. Livrables attendus de l’équipe infrastructure', HeadingLevel.HEADING_1),
        table(
          ['Livrable', 'Description'],
          [
            ['Serveur VM ou bare-metal', '4 vCPU, 8 Go RAM, 250 Go SSD, Linux amd64'],
            ['Nom de domaine + DNS', 'Enregistrement A/AAAA vers IP serveur'],
            ['Certificat SSL', 'Installation Apache mod_ssl'],
            ['Comptes', 'SSH déploiement, MySQL prism_user, accès monitoring'],
            ['Pare-feu', '443 public ; 8081/3306 bloqués Internet'],
            ['Sauvegarde', 'Politique quotidienne BDD + rétention 30 jours minimum'],
            ['Documentation retour', 'IP, hostname, chemins, procédure restauration'],
          ],
          [3200, 6160]
        ),

        // --- 12. Checklist ---
        heading('12. Checklist de mise en service', HeadingLevel.HEADING_1),
        ...[
          'Serveur provisionné : 4 vCPU / 8 Go RAM / 250 Go SSD',
          'Java 21, Tomcat 10, Apache 2.4, MySQL 8 installés et démarrés',
          'Base prism_bd créée, utilisateur prism_user configuré',
          'prism.war déployé, Flyway migrations V1–V9 appliquées sans erreur',
          'Front Angular déployé dans /var/www/dcspa/',
          'Apache : proxy /dcspa/api/ et alias /dcspa/ opérationnels',
          'HTTPS actif, redirection HTTP → HTTPS',
          'JWT_SECRET et mots de passe BDD configurés (non par défaut)',
          'CORS adapté au domaine production',
          'Test fonctionnel : connexion, navigation menus, saisie métier, import Excel',
          'Sauvegarde automatique MySQL testée',
          'Pare-feu validé (8081/3306 non accessibles depuis Internet)',
        ].map((t) => bullet(t)),

        spacer(200),
        new Paragraph({
          alignment: AlignmentType.CENTER,
          spacing: { before: 240 },
          children: [
            new TextRun({
              text: '— Fin du document —',
              font: 'Calibri',
              size: 20,
              italics: true,
              color: '666666',
            }),
          ],
        }),
        para([
          run('Références projet : prism-backend/prism/deploy/apache/, application-staging.properties, DOCS/PRISE_EN_MAIN.md', {
            size: 18,
            color: '888888',
            italics: true,
          }),
        ]),
      ],
    },
  ],
});

const buffer = await Packer.toBuffer(doc);
fs.mkdirSync(path.dirname(OUT), { recursive: true });
fs.writeFileSync(OUT, buffer);
console.log('Document généré :', OUT);
