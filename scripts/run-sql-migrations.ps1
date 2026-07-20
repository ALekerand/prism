# PRISM - exécution manuelle des migrations SQL (secours DBA)
# Usage principal staging/prod : Flyway au démarrage (SPRING_FLYWAY_ENABLED=true, voir db/migration/README.md).
# Usage :
#   pwsh scripts/run-sql-migrations.ps1
#   pwsh scripts/run-sql-migrations.ps1 -DbHost db.example.com -DbUser prism -DbPassword secret -DbName prism_bd
#
# Variables d'environnement optionnelles :
#   PRISM_DB_HOST, PRISM_DB_PORT, PRISM_DB_USER, PRISM_DB_PASSWORD, PRISM_DB_NAME, PRISM_MYSQL_BIN

param(
  [string]$DbHost = $(if ($env:PRISM_DB_HOST) { $env:PRISM_DB_HOST } else { 'localhost' }),
  [int]$DbPort = $(if ($env:PRISM_DB_PORT) { [int]$env:PRISM_DB_PORT } else { 3306 }),
  [string]$DbUser = $(if ($env:PRISM_DB_USER) { $env:PRISM_DB_USER } else { 'root' }),
  [string]$DbPassword = $(if ($null -ne $env:PRISM_DB_PASSWORD) { $env:PRISM_DB_PASSWORD } else { '' }),
  [string]$DbName = $(if ($env:PRISM_DB_NAME) { $env:PRISM_DB_NAME } else { 'prism_bd' }),
  [string]$MysqlBin = $(if ($env:PRISM_MYSQL_BIN) { $env:PRISM_MYSQL_BIN } else { '' })
)

$ErrorActionPreference = 'Stop'
$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$sqlDir = Join-Path $scriptRoot 'sql'

function Resolve-MysqlBin {
  param([string]$Preferred)
  if ($Preferred -and (Test-Path $Preferred)) { return $Preferred }
  $candidates = @(
    'C:\laragon\bin\mysql\mysql-8.0.30-winx64\bin\mysql.exe',
    'C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe',
    'C:\Program Files\MariaDB 10.11\bin\mysql.exe'
  )
  foreach ($c in $candidates) {
    if (Test-Path $c) { return $c }
  }
  $found = Get-Command mysql -ErrorAction SilentlyContinue
  if ($found) { return $found.Source }
  throw 'Client mysql introuvable. Indiquez -MysqlBin ou PRISM_MYSQL_BIN.'
}

function Test-BenignSqlError {
  param([string]$Text)
  if (-not $Text) { return $false }
  $benign = @(
    'Duplicate column name',      # 1060
    'Duplicate key name',         # 1061
    "Can't DROP",                 # 1091 FK inexistante
    'already exists',             # table / proc
    'Duplicate foreign key'       # 1826
  )
  foreach ($pattern in $benign) {
    if ($Text -match [regex]::Escape($pattern)) { return $true }
  }
  return $false
}

$mysql = Resolve-MysqlBin -Preferred $MysqlBin
Write-Host "MySQL client : $mysql"
Write-Host "Cible        : $DbUser@${DbHost}:$DbPort/$DbName"

$mysqlArgs = @(
  "-h$DbHost",
  "-P$DbPort",
  "-u$DbUser",
  "-D$DbName",
  '--default-character-set=utf8mb4',
  '--batch',
  '--raw'
)
if ($DbPassword -ne '') {
  $mysqlArgs += "-p$DbPassword"
}

$files = Get-ChildItem -Path $sqlDir -Filter '*.sql' | Sort-Object Name
if ($files.Count -eq 0) {
  throw "Aucun script SQL dans $sqlDir"
}

$ok = 0
$skipped = 0
$failed = 0

foreach ($file in $files) {
  Write-Host ""
  Write-Host "=== $($file.Name) ==="
  $sql = Get-Content -Raw -Path $file.FullName
  $output = $sql | & $mysql @mysqlArgs 2>&1
  $exitCode = $LASTEXITCODE
  $text = ($output | Out-String).Trim()

  if ($exitCode -eq 0) {
    if ($text) { Write-Host $text }
    Write-Host "OK"
    $ok++
    continue
  }

  if (Test-BenignSqlError -Text $text) {
    Write-Host "SKIP (déjà appliqué ou sans effet) : $text"
    $skipped++
    continue
  }

  Write-Host "ERREUR : $text"
  $failed++
}

Write-Host ""
Write-Host "=== Bilan migrations ==="
Write-Host "OK      : $ok"
Write-Host "SKIP    : $skipped"
Write-Host "ERREUR  : $failed"

if ($failed -gt 0) {
  exit 1
}
