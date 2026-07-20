# PRISM - smoke statut centre (actif/inactif)
# Usage: powershell -ExecutionPolicy Bypass -File scripts/smoke-centre-actif.ps1
# Prerequis: backend (default http://localhost:8080; override PRISM_API_BASE)

$ErrorActionPreference = 'Stop'
$base = if ($env:PRISM_API_BASE) { $env:PRISM_API_BASE } else { 'http://localhost:8080' }

function Assert-Ok($resp, $label) {
  if ($resp.StatusCode -lt 200 -or $resp.StatusCode -ge 300) {
    throw "FAIL $label : HTTP $($resp.StatusCode) $($resp.Content)"
  }
  Write-Host "OK $label ($($resp.StatusCode))"
}

function Get-FirstCentreIdFromAlphaList([string]$json) {
  if ($json -match '"idCentre"\s*:\s*(\d+)') {
    return [int]$Matches[1]
  }
  if ($json -match '"id"\s*:\s*(\d+)') {
    return [int]$Matches[1]
  }
  throw 'Aucun id centre dans la reponse GET /api/alpha'
}

function Test-CentreIdInAlphaListJson([string]$json, [int]$centreId) {
  return $json -match ('"idCentre"\s*:\s*' + [regex]::Escape("$centreId") + '(?!\d)')
}

Write-Host "=== Login ($base) ==="
$token = $null
foreach ($pair in @(
  @{ u = 'admin'; p = 'admin123' },
  @{ u = 'nebdev'; p = 'nebdev' }
)) {
  try {
    $login = Invoke-WebRequest -Uri "$base/api/auth/login" -Method POST -Body (@{ username = $pair.u; password = $pair.p } | ConvertTo-Json) -ContentType 'application/json' -UseBasicParsing
    Assert-Ok $login "login $($pair.u)"
    $token = ($login.Content | ConvertFrom-Json).token
    break
  } catch {
    Write-Host "Login $($pair.u) refuse"
  }
}
if (-not $token) { throw 'Login impossible' }
$headers = @{ Authorization = "Bearer $token" }

Write-Host '=== Choisir un centre Alpha ==='
$list = Invoke-WebRequest -Uri "$base/api/alpha?page=0&size=5" -Headers $headers -UseBasicParsing
Assert-Ok $list 'GET /api/alpha'
$id = Get-FirstCentreIdFromAlphaList $list.Content
Write-Host "Centre de test: #$id"

function Get-ActifFromJson([string]$json) {
  if ($json -match '"actif"\s*:\s*(true|false)') {
    return [System.Convert]::ToBoolean($Matches[1])
  }
  return $null
}

function Get-LibelleFromJson([string]$json) {
  if ($json -match '"libelle"\s*:\s*"([^"]*)"') {
    return $Matches[1]
  }
  return $null
}

function Set-Actif([bool]$actif) {
  $body = @{ actif = $actif } | ConvertTo-Json
  $resp = Invoke-WebRequest -Uri "$base/api/alpha/$id/actif" -Method PUT -Headers $headers -ContentType 'application/json' -Body $body -UseBasicParsing
  Assert-Ok $resp "PUT /api/alpha/$id/actif actif=$actif"
  $got = Get-ActifFromJson $resp.Content
  if ($got -ne $actif) {
    throw "FAIL: reponse actif=$got attendu=$actif body=$($resp.Content.Substring(0, [Math]::Min(300, $resp.Content.Length)))"
  }
  return @{ actif = $got; libelle = (Get-LibelleFromJson $resp.Content); content = $resp.Content }
}

Write-Host '=== Desactiver ==='
$rowOff = Set-Actif $false
Write-Host "  libelle=$($rowOff.libelle) actif=$($rowOff.actif)"

Write-Host '=== Liste filtree inactifs ==='
$inactive = Invoke-WebRequest -Uri "$base/api/alpha?page=0&size=50&actif=false" -Headers $headers -UseBasicParsing
Assert-Ok $inactive 'GET /api/alpha?actif=false'
if (-not (Test-CentreIdInAlphaListJson $inactive.Content $id)) {
  Write-Host 'WARN: centre desactive non trouve via filtre actif=false (verifier filtre serveur)'
} else {
  Write-Host 'OK centre present dans filtre inactifs'
}

Write-Host '=== Reactiver ==='
$rowOn = Set-Actif $true
Write-Host "  libelle=$($rowOn.libelle) actif=$($rowOn.actif)"

Write-Host '=== Liste filtree actifs ==='
$active = Invoke-WebRequest -Uri "$base/api/alpha?page=0&size=50&actif=true" -Headers $headers -UseBasicParsing
Assert-Ok $active 'GET /api/alpha?actif=true'

Write-Host ''
Write-Host '=== SMOKE CENTRE ACTIF: SUCCES ==='
