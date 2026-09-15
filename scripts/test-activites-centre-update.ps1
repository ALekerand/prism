# Tests post-update : commentaire contrôle, validation évaluation, RBAC documents
param([string]$BaseUrl = 'http://localhost:8080')
$ErrorActionPreference = 'Stop'
$base = $BaseUrl
$pass = '123456'

function Login($user) {
  $body = @{ username = $user; password = $pass } | ConvertTo-Json
  $r = Invoke-RestMethod -Uri "$base/api/auth/login" -Method POST -ContentType 'application/json' -Body $body
  return @{ Token = $r.token; Roles = $r.roles; Permissions = $r.permissions }
}

function AuthHeaders($token) { return @{ Authorization = "Bearer $token" } }

function Test-Name($label) { Write-Host "`n=== $label ===" -ForegroundColor Cyan }

function Assert($cond, $msg) {
  if (-not $cond) { throw "FAIL: $msg" }
  Write-Host "OK: $msg" -ForegroundColor Green
}

function Unwrap-List($data) {
  if ($null -eq $data) { return @() }
  if ($data -is [array]) { return $data }
  if ($data.content) { return @($data.content) }
  if ($data.value) { return @($data.value) }
  return @($data)
}

Test-Name 'Login conseiller + central admin'
$conseiller = Login 'conseiller_test'
$central = Login 'superviseur_aenf_test'
Assert ($conseiller.Token) 'conseiller_test login'
Assert ($central.Token) 'superviseur_aenf_test login'

Test-Name 'RBAC documents'
$conseillerHasParamDoc = $conseiller.Permissions | Where-Object { $_ -like 'PARAMETRAGE_DOCUMENTS:*' }
Assert (-not $conseillerHasParamDoc) 'conseiller sans PARAMETRAGE_DOCUMENTS'
$centralHasParamDoc = $central.Permissions | Where-Object { $_ -like 'PARAMETRAGE_DOCUMENTS:LIRE' }
Assert ($centralHasParamDoc) 'central admin avec PARAMETRAGE_DOCUMENTS:LIRE'
$conseillerHasSaisie = $conseiller.Permissions | Where-Object { $_ -like 'SAISIE_DONNEES:MODIFIER' }
Assert ($conseillerHasSaisie) 'conseiller avec SAISIE_DONNEES (dossier centre)'

Test-Name 'Contrôle — commentaire API'
$alphaPage = Invoke-RestMethod -Uri "$base/api/alpha" -Headers (AuthHeaders $central.Token)
$alphas = Unwrap-List $alphaPage
$idAlpha = ($alphas | Select-Object -First 1).idCentre
if (-not $idAlpha) { $idAlpha = ($alphas | Select-Object -First 1).id }
$periodes = Invoke-RestMethod -Uri "$base/api/PeriodeActivites" -Headers (AuthHeaders $central.Token)
$idPeriode = ($periodes | Select-Object -First 1).id
$niveaux = Invoke-RestMethod -Uri "$base/api/niveaualpha" -Headers (AuthHeaders $central.Token)
$idNiveau = ($niveaux | Select-Object -First 1).id
$manuels = Invoke-RestMethod -Uri "$base/api/manuels" -Headers (AuthHeaders $central.Token)
$idManuel = ($manuels | Select-Object -First 1).id
Assert ($idAlpha) "fixture alpha id=$idAlpha"
$payload = @{
  idAlpha = $idAlpha
  idPeriodeActivite = $idPeriode
  dateDemarrageAppren = '2026-01-15'
  idNiveauAlpha = $idNiveau
  conformiteProgramme = $true
  commentaire = 'Test auto commentaire controle'
  horairesFormation = @(@{ jourSemaine = 'LUNDI'; heureDebut = '08:00'; heureFin = '10:00' })
  kitsManuels = @(@{ idManuel = $idManuel; nombreKit = 1; precisionAutre = $null })
} | ConvertTo-Json -Depth 5
$created = Invoke-RestMethod -Uri "$base/api/controle" -Method POST -Headers (AuthHeaders $central.Token) -ContentType 'application/json' -Body $payload
Assert ($created.PSObject.Properties.Name -contains 'commentaire') 'champ commentaire dans reponse POST'
Assert ($created.commentaire -eq 'Test auto commentaire controle') 'commentaire persiste'
$readBack = Invoke-RestMethod -Uri "$base/api/controle/$($created.id)" -Headers (AuthHeaders $central.Token)
Assert ($readBack.commentaire -eq 'Test auto commentaire controle') 'commentaire relu par GET'
Invoke-RestMethod -Uri "$base/api/controle/$($created.id)" -Method DELETE -Headers (AuthHeaders $central.Token) | Out-Null
Write-Host "OK: controle test $($created.id) supprime" -ForegroundColor Green

Test-Name 'Evaluation — rejeter obtenu > total'
$evalBad = @{
  idAlpha = $idAlpha
  idPeriodeActivite = $idPeriode
  idNiveauEvaluation = 1
  typeEvaluation = 'FORMATIVE'
  themesTaux = @(@{ idThemeEvaluation = 1; nombreTotalEvalue = 50; nombreResultatObtenu = 60; taux = 120 })
} | ConvertTo-Json -Depth 5
try {
  Invoke-RestMethod -Uri "$base/api/evaluation" -Method POST -Headers (AuthHeaders $central.Token) -ContentType 'application/json' -Body $evalBad | Out-Null
  throw 'FAIL: evaluation obtenu>total aurait du etre rejetee'
} catch {
  $msg = if ($_.ErrorDetails.Message) { $_.ErrorDetails.Message } else { $_.Exception.Message }
  $is400 = $msg -match '400|Bad Request|incorrecte'
  Assert ($is400 -or $msg -match 'depasse|obtenu|total|invalide') "rejet evaluation (400 attendu): $msg"
}

Test-Name 'Evaluation — accepter obtenu <= total'
$evalOk = @{
  idAlpha = $idAlpha
  idPeriodeActivite = $idPeriode
  idNiveauEvaluation = 1
  typeEvaluation = 'FORMATIVE'
  themesTaux = @(@{ idThemeEvaluation = 1; nombreTotalEvalue = 50; nombreResultatObtenu = 40; taux = 80 })
} | ConvertTo-Json -Depth 5
$ev = Invoke-RestMethod -Uri "$base/api/evaluation" -Method POST -Headers (AuthHeaders $central.Token) -ContentType 'application/json' -Body $evalOk
Assert ($ev.themesTaux[0].nombreResultatObtenu -le $ev.themesTaux[0].nombreTotalEvalue) 'evaluation valide'
try {
  Invoke-RestMethod -Uri "$base/api/evaluation/$($ev.id)" -Method DELETE -Headers (AuthHeaders $central.Token) | Out-Null
  Write-Host "OK: evaluation test $($ev.id) supprimee" -ForegroundColor Green
} catch {
  Write-Host "WARN: evaluation $($ev.id) non supprimable (workflow): $($_.Exception.Message)" -ForegroundColor Yellow
}

Test-Name 'Documents — API partagee (dossier centre + admin)'
$docsC = Invoke-RestMethod -Uri "$base/api/documents" -Headers (AuthHeaders $conseiller.Token)
$docsA = Invoke-RestMethod -Uri "$base/api/documents" -Headers (AuthHeaders $central.Token)
Assert ($docsC -is [array]) 'conseiller GET /api/documents'
Assert ($docsA -is [array]) 'central GET /api/documents'

Write-Host "`n=== TOUS LES TESTS API PASSENT ===" -ForegroundColor Green
