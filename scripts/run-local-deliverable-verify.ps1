# Vérification livrable local : API + tests métier + smoke workflow.
# Prérequis : MySQL local (prism_bd), JDK 21, mot de passe comptes test = 123456.
param(
  [string]$BaseUrl = 'http://localhost:8080',
  [switch]$SkipBuild
)

$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$repo = Split-Path -Parent $root
$java21 = 'C:\env\java\jdk-21\bin\java.exe'
if (-not (Test-Path $java21)) {
  throw "JDK 21 introuvable : $java21"
}

if (-not $SkipBuild) {
  $env:JAVA_HOME = 'C:\env\java\jdk-21'
  Push-Location $repo
  .\mvnw.cmd -q clean package -DskipTests
  Pop-Location
}

Write-Host 'Demarrage API (profil local)...' -ForegroundColor Cyan
$war = Join-Path $repo 'target\prism.war'
$proc = Start-Process -FilePath $java21 -ArgumentList @('-jar', $war, '--spring.profiles.active=local') -PassThru -WindowStyle Hidden

function Wait-Api {
  param([int]$MaxSec = 120)
  for ($i = 0; $i -lt $MaxSec; $i++) {
    try {
      Invoke-WebRequest -Uri "$BaseUrl/api/auth/login" -Method POST -ContentType 'application/json' -Body '{"username":"a","password":"b"}' -TimeoutSec 2 -UseBasicParsing | Out-Null
      return $true
    } catch {
      if ($_.Exception.Response -and $_.Exception.Response.StatusCode.value__ -in 400,401,403) {
        return $true
      }
    }
    Start-Sleep -Seconds 1
  }
  return $false
}

try {
  if (-not (Wait-Api)) {
    throw 'API injoignable (MySQL local demarre ? port 8080 libre ?)'
  }
  & (Join-Path $root 'test-metier-complet-par-role.ps1') -BaseUrl $BaseUrl
  & (Join-Path $root 'workflow-return-e2e-smoke.ps1') -BaseUrl $BaseUrl
  Write-Host 'Livrable local verifie OK.' -ForegroundColor Green
} finally {
  if ($proc -and -not $proc.HasExited) {
    Stop-Process -Id $proc.Id -Force -ErrorAction SilentlyContinue
  }
}
