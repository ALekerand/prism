# PRISM — Reset BDD métier : purge centres + seed test Commission 1
param(
  [string]$DbHost = $(if ($env:PRISM_DB_HOST) { $env:PRISM_DB_HOST } else { 'localhost' }),
  [int]$DbPort = $(if ($env:PRISM_DB_PORT) { [int]$env:PRISM_DB_PORT } else { 3306 }),
  [string]$DbUser = $(if ($env:PRISM_DB_USER) { $env:PRISM_DB_USER } else { 'root' }),
  [string]$DbPassword = $(if ($null -ne $env:PRISM_DB_PASSWORD) { $env:PRISM_DB_PASSWORD } else { '' }),
  [string]$DbName = $(if ($env:PRISM_DB_NAME) { $env:PRISM_DB_NAME } else { 'prism_bd' }),
  [string]$MysqlBin = $(if ($env:PRISM_MYSQL_BIN) { $env:PRISM_MYSQL_BIN } else { '' }),
  [switch]$SkipCommissionSeed
)

$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$repo = Split-Path -Parent $root

function Resolve-MysqlBin {
  param([string]$Preferred)
  if ($Preferred -and (Test-Path $Preferred)) { return $Preferred }
  $candidates = @(
    'C:\laragon\bin\mysql\mysql-8.0.30-winx64\bin\mysql.exe',
    'C:\laragon\bin\mysql\mysql-8.4.3-winx64\bin\mysql.exe',
    'C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe'
  )
  foreach ($c in $candidates) { if (Test-Path $c) { return $c } }
  $found = Get-Command mysql -ErrorAction SilentlyContinue
  if ($found) { return $found.Source }
  throw 'Client mysql introuvable.'
}

function Invoke-SqlFile {
  param([string]$Mysql, [string[]]$MysqlCliArgs, [string]$Path, [string]$Label)
  Write-Host "`n=== $Label ===" -ForegroundColor Cyan
  if (-not (Test-Path $Path)) { throw "Fichier introuvable : $Path" }
  Get-Content -Raw -Path $Path | & $Mysql @MysqlCliArgs 2>&1 | ForEach-Object { Write-Host $_ }
  if ($LASTEXITCODE -ne 0) { throw "Echec SQL : $Label (code $LASTEXITCODE)" }
}

$mysql = Resolve-MysqlBin -Preferred $MysqlBin
$mysqlArgs = @("-h$DbHost", "-P$DbPort", "-u$DbUser", "-D$DbName", '--default-character-set=utf8mb4', '--batch', '--raw')
if ($DbPassword -ne '') { $mysqlArgs += "-p$DbPassword" }

Write-Host "Reset metier PRISM sur ${DbUser}@${DbHost}:$DbPort/$DbName" -ForegroundColor Yellow
Write-Host "MySQL : $mysql"

Invoke-SqlFile -Mysql $mysql -MysqlCliArgs $mysqlArgs -Path (Join-Path $repo 'db\purge-centres-et-activites.sql') -Label 'Purge centres et activites'
Invoke-SqlFile -Mysql $mysql -MysqlCliArgs $mysqlArgs -Path (Join-Path $repo 'db\seed-metier-test-commission1.sql') -Label 'Seed metier TEST-C1'

if (-not $SkipCommissionSeed) {
  $commissionSeed = Join-Path $repo 'db\commissions-1-4-users-seed.sql'
  if (Test-Path $commissionSeed) {
    Invoke-SqlFile -Mysql $mysql -MysqlCliArgs $mysqlArgs -Path $commissionSeed -Label 'Seed comptes commission (idempotent)'
  } else {
    Write-Host "SKIP: commissions-1-4-users-seed.sql absent" -ForegroundColor Yellow
  }
}

Write-Host "`n=== Reset termine ===" -ForegroundColor Green
Write-Host "Relancez ou rafraichissez le backend si necessaire, puis :"
Write-Host "  pwsh scripts/test-metier-complet-par-role.ps1 -BaseUrl http://localhost:8080"
