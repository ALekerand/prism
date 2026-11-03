# Smoke E2E : workflow retour + alerte TDB (sans reset BDD).
param(
  [string]$BaseUrl = 'http://localhost:8080',
  [string]$Password = '123456'
)

$ErrorActionPreference = 'Stop'
$failures = @()
$passCount = 0

function Login($user) {
  $body = @{ username = $user; password = $Password } | ConvertTo-Json
  return Invoke-RestMethod -Uri "$BaseUrl/api/auth/login" -Method POST -ContentType 'application/json' -Body $body -TimeoutSec 20
}

function AuthHeaders($token) { return @{ Authorization = "Bearer $token" } }

function Assert($cond, $msg) {
  if ($cond) {
    $script:passCount++
    Write-Host "  OK: $msg" -ForegroundColor Green
  } else {
    $script:failures += $msg
    Write-Host "  FAIL: $msg" -ForegroundColor Red
  }
}

Write-Host "=== Smoke workflow retours ($BaseUrl) ===" -ForegroundColor Cyan

try {
  $c1 = Login 'commission_1_conseiller_1'
  $coord = Login 'commission_1_coordonnateur_1'
} catch {
  Write-Host "Login impossible: $($_.Exception.Message)" -ForegroundColor Red
  exit 1
}

# Endpoints alerte (nouveau livrable)
try {
  $dash = Invoke-RestMethod -Uri "$BaseUrl/api/admin/dashboard" -Headers (AuthHeaders $c1.token) -TimeoutSec 25
  Assert ($null -ne $dash.workflowReturns) 'dashboard expose workflowReturns'
  Assert ($dash.workflowReturns.PSObject.Properties.Name -contains 'total') 'workflowReturns.total present'
  Assert ($dash.workflowReturns.PSObject.Properties.Name -contains 'items') 'workflowReturns.items present'
} catch {
  Assert $false "GET /api/admin/dashboard conseiller: $($_.Exception.Message)"
}

try {
  $alert = Invoke-RestMethod -Uri "$BaseUrl/api/saisie-workflows/retours-alertes" -Headers (AuthHeaders $c1.token) -TimeoutSec 25
  Assert ($alert.PSObject.Properties.Name -contains 'total') 'retours-alertes.total present'
} catch {
  Assert $false "GET /api/saisie-workflows/retours-alertes: $($_.Exception.Message)"
}

# Visite liste (régression 500)
try {
  $visites = Invoke-RestMethod -Uri "$BaseUrl/api/visite" -Headers (AuthHeaders $c1.token) -TimeoutSec 25
  Assert ($true) 'GET /api/visite conseiller OK'
} catch {
  Assert $false "GET /api/visite conseiller: $($_.Exception.Message)"
}

# Statuts workflow sur une visite existante si présente
$visRows = @()
if ($visites -is [array]) { $visRows = @($visites) }
elseif ($visites.content) { $visRows = @($visites.content) }
elseif ($visites) { $visRows = @($visites) }

if ($visRows.Count -gt 0 -and $visRows[0].id) {
  $id = $visRows[0].id
  try {
    $statuses = Invoke-RestMethod -Uri "$BaseUrl/api/saisie-workflows/statuses?resource=/api/visite&ids=$id" -Headers (AuthHeaders $c1.token) -TimeoutSec 25
    $st = $statuses["$id"]
    Assert ($null -ne $st) "statuses workflow visite #$id"
    if ($st) {
      Assert ($st.PSObject.Properties.Name -contains 'workflowOnglet') 'workflowOnglet present'
      Assert ($st.PSObject.Properties.Name -contains 'workflowDecidePar') 'workflowDecidePar present'
    }
  } catch {
    Assert $false "GET statuses visite: $($_.Exception.Message)"
  }
} else {
  Write-Host '  INFO: aucune visite existante — scénario retour complet ignoré' -ForegroundColor DarkGray
}

Write-Host "`n=== BILAN smoke workflow ===" -ForegroundColor Cyan
Write-Host "Tests reussis : $passCount"
Write-Host "Echecs        : $($failures.Count)"
if ($failures.Count -gt 0) {
  $failures | ForEach-Object { Write-Host "  - $_" -ForegroundColor Red }
  exit 1
}
Write-Host 'Smoke workflow OK.' -ForegroundColor Green
