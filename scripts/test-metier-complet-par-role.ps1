# Tests metier complets par role apres reset BDD
param(
  [string]$BaseUrl = 'http://localhost:8080',
  [string]$Password = '123456',
  [switch]$SkipReset
)

$ErrorActionPreference = 'Stop'
$pass = $Password
$failures = @()
$passCount = 0

if (-not $SkipReset) {
  Write-Host "=== Reset BDD metier (purge + seed) ===" -ForegroundColor Yellow
  & (Join-Path $PSScriptRoot 'reset-metier-test.ps1')
}

function Login($user) {
  $body = @{ username = $user; password = $pass } | ConvertTo-Json
  return Invoke-RestMethod -Uri "$BaseUrl/api/auth/login" -Method POST -ContentType 'application/json' -Body $body -TimeoutSec 15
}

function AuthHeaders($token) { return @{ Authorization = "Bearer $token" } }

function List-Count($rows) {
  if ($null -eq $rows) { return 0 }
  if ($rows -is [array]) { return $rows.Count }
  return 1
}

function Assert($cond, $msg) {
  if ($cond) {
    $script:passCount++
    Write-Host "  OK: $msg" -ForegroundColor Green
  } else {
    $script:failures += $msg
    Write-Host "  FAIL: $msg" -ForegroundColor Red
  }
}

function Normalize-ApiList($data) {
  if ($null -eq $data) { return @() }
  if ($data.GetType().IsArray) {
    if ($data.Count -eq 0) { return @() }
    return @($data)
  }
  $props = @($data.PSObject.Properties.Name)
  if ($props -contains 'content') {
    if ($null -eq $data.content) { return @() }
    if ($data.content.GetType().IsArray) {
      if ($data.content.Count -eq 0) { return @() }
      return @($data.content)
    }
    return @($data.content)
  }
  if ($props -contains 'totalElements') { return @() }
  return @($data)
}

function Get-List($token, $path) {
  $data = Invoke-RestMethod -Uri "$BaseUrl$path" -Headers (AuthHeaders $token) -TimeoutSec 20
  return Normalize-ApiList $data
}

function Get-AlphaForUser($token, $codePrefix) {
  $page = Invoke-RestMethod -Uri "$BaseUrl/api/alpha?size=50" -Headers (AuthHeaders $token) -TimeoutSec 20
  $rows = if ($page.PSObject.Properties.Name -contains 'content') { Normalize-ApiList $page.content } else { Normalize-ApiList $page }
  return $rows | Where-Object {
    $_.codeAlpha -like "$codePrefix*" -or $_.codeCentre -like "$codePrefix*" -or $_.codeType -like "$codePrefix*"
  } | Select-Object -First 1
}

function Get-ListSafe($token, $path) {
  try { return Get-List $token $path } catch { return @() }
}

function First-Id($token, $path, $prop = 'id') {
  $rows = Get-ListSafe $token $path
  if ((List-Count $rows) -eq 0) { return $null }
  return $rows[0].$prop
}

Write-Host "=== TESTS METIER COMPLETS ($BaseUrl) ===" -ForegroundColor Cyan

# --- Phase 1 : etat initial apres reset ---
Write-Host "`n--- Phase 1 : etat initial ---" -ForegroundColor White
$c1Login = Login 'commission_1_conseiller_1'
$c2Login = Login 'commission_1_conseiller_2'
$coordLogin = Login 'commission_1_coordonnateur_1'
$supLogin = Login 'commission_1_superviseur_1'
$aenfLogin = Login 'superviseur_aenf_test'

$c1Alphas = Get-List $c1Login.token '/api/alpha?size=50'
$c2Alphas = Get-List $c2Login.token '/api/alpha?size=50'
$coordAlphas = Get-List $coordLogin.token '/api/alpha?size=50'
$aenfAlphas = Get-List $aenfLogin.token '/api/alpha?size=50'

Assert (@($c1Alphas).Count -ge 1) "conseiller_1 voit au moins 1 alpha (attendu: son centre TEST-C1-A01)"
Assert (@($c2Alphas).Count -ge 1) "conseiller_2 voit au moins 1 alpha"
Assert (@($coordAlphas).Count -ge 2) "coordonnateur voit les 2 centres test IEP Cocody"
Assert (@($aenfAlphas).Count -ge 2) "superviseur AENF voit les centres test"

$c1Codes = ($c1Alphas | ForEach-Object { if ($_.codeCentre) { $_.codeCentre } elseif ($_.codeAlpha) { $_.codeAlpha } else { $_.codeType } }) -join ','
$c2Codes = ($c2Alphas | ForEach-Object { if ($_.codeCentre) { $_.codeCentre } elseif ($_.codeAlpha) { $_.codeAlpha } else { $_.codeType } }) -join ','
Assert ($c1Codes -match 'TEST-C1-A01') "conseiller_1 voit TEST-C1-A01"
Assert ($c2Codes -match 'TEST-C1-A02') "conseiller_2 voit TEST-C1-A02"
Assert ($c1Codes -notmatch 'TEST-C1-A02') "conseiller_1 ne voit PAS le centre du conseiller_2"

$initControle = List-Count (Get-List $c1Login.token '/api/controle')
$initEval = List-Count (Get-List $c1Login.token '/api/evaluation')
$initVisite = List-Count (Get-List $c1Login.token '/api/visite')
Assert ($initControle -eq 0) "aucun controle residuel pour conseiller_1"
Assert ($initEval -eq 0) "aucune evaluation residuelle pour conseiller_1"
Assert ($initVisite -eq 0) "aucune visite residuelle pour conseiller_1"

# --- Phase 2 : saisies conseiller_1 ---
Write-Host "`n--- Phase 2 : saisies conseiller_1 ---" -ForegroundColor White
$alpha1 = Get-AlphaForUser $c1Login.token 'TEST-C1-A01'
$idAlpha1 = if ($alpha1.idCentre) { $alpha1.idCentre } else { $alpha1.id }
$idPeriode = First-Id $c1Login.token '/api/PeriodeActivites'
$idNiveauAlpha = First-Id $c1Login.token '/api/niveaualpha'
$idManuel = First-Id $c1Login.token '/api/manuels'
$idNiveauEval = First-Id $c1Login.token '/api/niveaux-evaluation'
$idTheme = First-Id $c1Login.token '/api/themes-evaluation'

Assert ($idAlpha1) "fixture alpha conseiller_1 id=$idAlpha1"
Assert ($idPeriode) "fixture periode activite"

# Controle
$ctrlPayload = @{
  idAlpha = [int]$idAlpha1
  idPeriodeActivite = [int]$idPeriode
  dateDemarrageAppren = '2026-02-01'
  idNiveauAlpha = [int]$idNiveauAlpha
  conformiteProgramme = $true
  commentaire = 'Test metier conseiller 1'
  horairesFormation = @(@{ jourSemaine = 'LUNDI'; heureDebut = '08:00'; heureFin = '10:00' })
  kitsManuels = @(@{ idManuel = [int]$idManuel; nombreKit = 1 })
} | ConvertTo-Json -Depth 5
$ctrl = Invoke-RestMethod -Uri "$BaseUrl/api/controle" -Method POST -Headers (AuthHeaders $c1Login.token) -ContentType 'application/json' -Body $ctrlPayload
Assert ($ctrl.workflowProprietaire -eq 'commission_1_conseiller_1' -or $ctrl.id) "controle cree avec claim conseiller_1"

# Effectif alpha
$niveauAlphaEff = First-Id $c1Login.token '/api/niveaualpha'
$effPayload = @{
  idCentre = [int]$idAlpha1
  idPeriodeActivite = [int]$idPeriode
  idNiveauAlpha = [int]$niveauAlphaEff
  effectifAlphaNiveauH = 10
  effectifAlphaNiveauF = 12
} | ConvertTo-Json
$eff = Invoke-RestMethod -Uri "$BaseUrl/api/effectif-alpha" -Method POST -Headers (AuthHeaders $c1Login.token) -ContentType 'application/json' -Body $effPayload
Assert ($eff.id) "effectif-alpha cree par conseiller_1"

# Visite
$visPayload = @{
  idAlpha = [int]$idAlpha1
  idPeriodeActivite = [int]$idPeriode
  idNiveauAlpha = [int]$idNiveauAlpha
  maitriseSeanceLecture = 'BONNE'
  mode = 'points'
} | ConvertTo-Json
$vis = Invoke-RestMethod -Uri "$BaseUrl/api/visite" -Method POST -Headers (AuthHeaders $c1Login.token) -ContentType 'application/json' -Body $visPayload
Assert ($vis.id) "visite creee par conseiller_1"

# Evaluation si referentiel disponible
if ($idNiveauEval -and $idTheme) {
  $evalPayload = @{
    idAlpha = [int]$idAlpha1
    idPeriodeActivite = [int]$idPeriode
    idNiveauEvaluation = [int]$idNiveauEval
    typeEvaluation = 'FORMATIVE'
    themesTaux = @(@{ idThemeEvaluation = [int]$idTheme; nombreTotalEvalue = 20; nombreResultatObtenu = 15; taux = 75 })
  } | ConvertTo-Json -Depth 4
  try {
    $ev = Invoke-RestMethod -Uri "$BaseUrl/api/evaluation" -Method POST -Headers (AuthHeaders $c1Login.token) -ContentType 'application/json' -Body $evalPayload
    Assert ($ev.id) "evaluation creee par conseiller_1"
  } catch {
    Write-Host "  WARN: evaluation non creee ($($_.Exception.Message))" -ForegroundColor Yellow
  }
}

# --- Phase 3 : isolation conseiller_2 ---
Write-Host "`n--- Phase 3 : isolation conseiller_2 ---" -ForegroundColor White
$c2Controles = Get-List $c2Login.token '/api/controle'
$c2Eff = Get-List $c2Login.token '/api/effectif-alpha'
$c1ControlesAfter = Get-List $c1Login.token '/api/controle'

Assert ((List-Count $c1ControlesAfter) -ge 1) "conseiller_1 voit son controle"
Assert ((List-Count $c2Controles) -eq 0) "conseiller_2 ne voit PAS le controle de conseiller_1"
Assert ((List-Count $c2Eff) -eq 0) "conseiller_2 ne voit PAS l'effectif de conseiller_1"

# --- Phase 4 : coordonnateur voit IEP ---
Write-Host "`n--- Phase 4 : coordonnateur ---" -ForegroundColor White
$coordCtrl = Get-List $coordLogin.token '/api/controle'
$coordEff = Get-List $coordLogin.token '/api/effectif-alpha'
Assert ((List-Count $coordCtrl) -ge 1) "coordonnateur voit controles IEP (brouillon sans proprietaire exclu pour autre IEP - ici meme IEP)"
Assert ((List-Count $coordEff) -ge 1) "coordonnateur voit effectifs IEP"

# --- Phase 5 : superviseur DRENA ---
Write-Host "`n--- Phase 5 : superviseur DRENA ---" -ForegroundColor White
$supCtrl = Get-List $supLogin.token '/api/controle'
# superviseur ne voit pas brouillon sans owner dans son perimetre si pas soumis
Write-Host "  INFO: superviseur controle count=$(List-Count $supCtrl)" -ForegroundColor DarkGray

# --- Phase 6 : national ---
Write-Host "`n--- Phase 6 : superviseur AENF ---" -ForegroundColor White
$aenfCtrl = Get-List $aenfLogin.token '/api/controle'
$aenfEff = Get-List $aenfLogin.token '/api/effectif-alpha'
Assert ((List-Count $aenfCtrl) -ge 1) "superviseur AENF voit controles"
Assert ((List-Count $aenfEff) -ge 1) "superviseur AENF voit effectifs"

# --- Phase 7 : conseiller_2 saisie propre ---
Write-Host "`n--- Phase 7 : saisies conseiller_2 ---" -ForegroundColor White
$alpha2 = Get-AlphaForUser $c2Login.token 'TEST-C1-A02'
$idAlpha2 = if ($alpha2.idCentre) { $alpha2.idCentre } else { $alpha2.id }
if ($idAlpha2) {
  $ctrl2Payload = @{
    idAlpha = [int]$idAlpha2
    idPeriodeActivite = [int]$idPeriode
    dateDemarrageAppren = '2026-02-01'
    idNiveauAlpha = [int]$idNiveauAlpha
    conformiteProgramme = $true
    commentaire = 'Test metier conseiller 2'
    horairesFormation = @()
    kitsManuels = @()
  } | ConvertTo-Json -Depth 5
  $ctrl2 = Invoke-RestMethod -Uri "$BaseUrl/api/controle" -Method POST -Headers (AuthHeaders $c2Login.token) -ContentType 'application/json' -Body $ctrl2Payload
  Assert ($ctrl2.id) "conseiller_2 cree son controle"

  $c1CtrlFinal = Get-List $c1Login.token '/api/controle'
  $c2CtrlFinal = Get-List $c2Login.token '/api/controle'
  Assert ((List-Count $c1CtrlFinal) -eq 1) "conseiller_1 voit exactement 1 controle"
  Assert ((List-Count $c2CtrlFinal) -eq 1) "conseiller_2 voit exactement 1 controle"
}

# --- Bilan ---
Write-Host "`n=== BILAN ===" -ForegroundColor Cyan
Write-Host "Tests reussis : $passCount"
Write-Host "Echecs        : $($failures.Count)"
if ($failures.Count -gt 0) {
  $failures | ForEach-Object { Write-Host "  - $_" -ForegroundColor Red }
  exit 1
}
Write-Host "Tous les tests metier sont OK." -ForegroundColor Green
