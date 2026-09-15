# PRISM — Build + paquet de deploiement staging (Apache /dcspa + Tomcat /prism)
param(
  [string]$OutDir = '',
  [switch]$SkipBackend,
  [switch]$SkipFrontend
)

$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$repo = Split-Path -Parent $root
$aenf = Split-Path -Parent (Split-Path -Parent $repo)
$frontRoot = Join-Path $aenf 'prism-frontend'

if (-not $OutDir) {
  $OutDir = Join-Path $repo "dist\deploy-staging-$(Get-Date -Format 'yyyyMMdd-HHmmss')"
}

function Resolve-JavaHome {
  if ($env:JAVA_HOME -and (Test-Path (Join-Path $env:JAVA_HOME 'bin\java.exe'))) { return $env:JAVA_HOME }
  $candidates = @(
    'C:\Users\DELL\.cursor\extensions\redhat.java-1.55.0-win32-x64\jre\21.0.11-win32-x86_64',
    'C:\Program Files\Java\jdk-21',
    'C:\Program Files\Eclipse Adoptium\jdk-21*'
  )
  foreach ($c in $candidates) {
    $resolved = Resolve-Path $c -ErrorAction SilentlyContinue
    if ($resolved -and (Test-Path (Join-Path $resolved 'bin\java.exe'))) { return $resolved.Path }
  }
  throw 'JAVA_HOME introuvable (Java 21 requis).'
}

Write-Host "=== PRISM deploy staging ===" -ForegroundColor Cyan
Write-Host "Sortie : $OutDir"

New-Item -ItemType Directory -Force -Path $OutDir, (Join-Path $OutDir 'front'), (Join-Path $OutDir 'server') | Out-Null

if (-not $SkipBackend) {
  Write-Host "`n--- Backend (mvn package) ---" -ForegroundColor Yellow
  $env:JAVA_HOME = Resolve-JavaHome
  $javaProcs = Get-CimInstance Win32_Process -Filter "Name='java.exe'" -ErrorAction SilentlyContinue |
    Where-Object { $_.CommandLine -match 'prism\.war' }
  foreach ($p in $javaProcs) {
    Write-Host "Arret processus java prism.war (pid $($p.ProcessId))..."
    Stop-Process -Id $p.ProcessId -Force -ErrorAction SilentlyContinue
  }
  Start-Sleep -Seconds 2
  Push-Location $repo
  & .\mvnw.cmd -q clean package -DskipTests
  if ($LASTEXITCODE -ne 0) { throw "Echec mvn package (code $LASTEXITCODE)" }
  Pop-Location
  Copy-Item (Join-Path $repo 'target\prism.war') (Join-Path $OutDir 'server\prism.war') -Force
  Copy-Item (Join-Path $repo 'deploy\apache\dcspa-prism-port91.conf') (Join-Path $OutDir 'server\dcspa-prism-port91.conf') -Force
  Copy-Item (Join-Path $repo 'deploy\apache\dcspa-prism-port80.snippet.conf') (Join-Path $OutDir 'server\dcspa-prism-port80.snippet.conf') -Force
  Write-Host "  prism.war copie" -ForegroundColor Green
}

if (-not $SkipFrontend) {
  Write-Host "`n--- Frontend (ng build staging) ---" -ForegroundColor Yellow
  if (-not (Test-Path (Join-Path $frontRoot 'node_modules'))) {
    Push-Location $frontRoot
    npm ci
    Pop-Location
  }
  Push-Location $frontRoot
  npm run build -- --configuration=staging
  if ($LASTEXITCODE -ne 0) { throw "Echec ng build staging (code $LASTEXITCODE)" }
  Pop-Location
  $frontSrc = Join-Path $frontRoot 'dist\prism_front\browser'
  if (-not (Test-Path $frontSrc)) { $frontSrc = Join-Path $frontRoot 'dist\prism_front' }
  Copy-Item -Path (Join-Path $frontSrc '*') -Destination (Join-Path $OutDir 'front') -Recurse -Force
  Write-Host "  front staging copie depuis $frontSrc" -ForegroundColor Green
}

$readme = @"
PRISM — Paquet deploiement staging
Genere : $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')

CONTENU
  server/prism.war          -> Tomcat webapps/ (contexte /prism)
  server/dcspa-prism-port91.conf
  server/dcspa-prism-port80.snippet.conf
  front/*                   -> /var/www/dcspa/

SERVEUR (VPS 154.0.30.233 — port 91 documente)
  1. Front : rsync/scp front/* vers /var/www/dcspa/
  2. WAR   : scp server/prism.war vers Tomcat webapps/, redemarrer Tomcat
  3. Apache: vhost /dcspa/api/ -> http://127.0.0.1:8081/prism/api/
  4. Env Tomcat (setenv.sh ou catalina.properties) :
       SPRING_PROFILES_ACTIVE=staging
       SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/prism_bd
       SPRING_DATASOURCE_USERNAME=...
       SPRING_DATASOURCE_PASSWORD=...
       JWT_SECRET=...
       SPRING_FLYWAY_ENABLED=true

  Flyway applique V1..V16 au demarrage (dont V16 controle_commentaire).

VERIFICATION
  http://<ip>:91/dcspa/
  http://<ip>:91/dcspa/api/auth/login (POST)

LOCAL (test prod-like sans Apache)
  java -jar server/prism.war --spring.profiles.active=staging
  + servir front/ sous /dcspa/ ou ajuster apiBaseUrl
"@
Set-Content -Path (Join-Path $OutDir 'DEPLOY-README.txt') -Value $readme -Encoding UTF8

Write-Host "`n=== Paquet pret ===" -ForegroundColor Green
Write-Host $OutDir
Get-ChildItem $OutDir -Recurse -File | Measure-Object -Property Length -Sum |
  ForEach-Object { Write-Host ("  {0} fichiers, {1:N1} MB" -f $_.Count, ($_.Sum / 1MB)) }
