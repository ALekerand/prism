# Tests périmètre métier : conseiller / coordonnateur / superviseur / central

param([string]$BaseUrl = 'http://localhost:8080')

$ErrorActionPreference = 'Continue'

$pass = '123456'



$users = @(

  'commission_1_conseiller_1',

  'commission_1_conseiller_2',

  'commission_1_coordonnateur_1',

  'commission_1_superviseur_1',

  'superviseur_aenf_test',

  'conseiller_test'

)



function Login($user) {

  try {

    $body = @{ username = $user; password = $pass } | ConvertTo-Json

    $r = Invoke-RestMethod -Uri "$BaseUrl/api/auth/login" -Method POST -ContentType 'application/json' -Body $body -TimeoutSec 8

    return @{ ok = $true; token = $r.token; roles = ($r.roles -join ','); perms = @($r.permissions) }

  } catch {

    return @{ ok = $false; err = $_.Exception.Message }

  }

}



function Me($token) {

  $h = @{ Authorization = "Bearer $token" }

  return Invoke-RestMethod -Uri "$BaseUrl/api/auth/me" -Headers $h -TimeoutSec 8

}



function Count-Api($token, $path) {

  $h = @{ Authorization = "Bearer $token" }

  try {

    $data = Invoke-RestMethod -Uri "$BaseUrl$path" -Headers $h -TimeoutSec 12

    if ($null -eq $data) { return 0 }

    if ($data -is [array]) { return $data.Count }

    if ($data.content) { return $data.content.Count }

    if ($data.totalElements -ne $null) { return [int]$data.totalElements }

    if ($data.value) { return @($data.value).Count }

    return 1

  } catch {

    $code = $_.Exception.Response.StatusCode.value__

    if (-not $code) { $code = 'ERR' }

    return "ERR:$code"

  }

}



function Is-Count($value) {

  return ($value -is [int]) -or ($value -match '^\d+$')

}



function WorkflowOwners($token, $resource) {

  $h = @{ Authorization = "Bearer $token" }

  try {

    $rows = Invoke-RestMethod -Uri "$BaseUrl$resource" -Headers $h -TimeoutSec 12

    if (-not ($rows -is [array])) { return @() }

    return $rows | ForEach-Object {

      $id = $_.id

      $wf = $_.workflowProprietaire

      if (-not $wf) { $wf = $_.workflowSoumisPar }

      if (-not $wf) { $wf = '(sans)' }

      [pscustomobject]@{ id = $id; owner = $wf; statut = $_.workflowStatut }

    }

  } catch { return @() }

}



Write-Host "=== TESTS PERIMETRE METIER ($BaseUrl) ===" -ForegroundColor Cyan

$results = @()



foreach ($u in $users) {

  $login = Login $u

  if (-not $login.ok) {

    Write-Host "SKIP $u : $($login.err)" -ForegroundColor Yellow

    continue

  }

  $me = Me $login.token

  $row = [ordered]@{

    user = $u

    roles = $login.roles

    idIep = $me.idIep

    idDrena = $me.idDrena

    scopeLabel = $me.scopeLabel

    nationalView = $me.nationalView

    alpha = Count-Api $login.token '/api/alpha'

    centres = Count-Api $login.token '/api/centres'

    controle = Count-Api $login.token '/api/controle'

    evaluation = Count-Api $login.token '/api/evaluation'

    performance = Count-Api $login.token '/api/performance'

    documents = Count-Api $login.token '/api/documents'

    effectifAlpha = Count-Api $login.token '/api/effectif-alpha'

    visite = Count-Api $login.token '/api/visite'

  }

  $results += [pscustomobject]$row

  Write-Host "`n--- $u ($($login.roles)) ---" -ForegroundColor White

  Write-Host "  scope: $($me.scopeLabel) | IEP=$($me.idIep) DRENA=$($me.idDrena) national=$($me.nationalView)"

  Write-Host "  alpha=$($row.alpha) centres=$($row.centres) controle=$($row.controle) eval=$($row.evaluation) perf=$($row.performance) docs=$($row.documents) effectif=$($row.effectifAlpha) visite=$($row.visite)"

}



Write-Host "`n=== COMPARAISON CONSEILLERS MEME COMMISSION ===" -ForegroundColor Cyan

$c1 = $results | Where-Object { $_.user -eq 'commission_1_conseiller_1' }

$c2 = $results | Where-Object { $_.user -eq 'commission_1_conseiller_2' }

if ($c1 -and $c2) {

  $sameControle = ($c1.controle -eq $c2.controle)

  $sameEval = ($c1.evaluation -eq $c2.evaluation)

  $samePerf = ($c1.performance -eq $c2.performance)

  $sameAlpha = ($c1.alpha -eq $c2.alpha)

  Write-Host "conseiller_1 vs conseiller_2 : alpha identique=$sameAlpha controle identique=$sameControle eval identique=$sameEval perf identique=$samePerf"

  foreach ($label in @('controle','evaluation','performance')) {

    $v1 = $c1.$label

    $v2 = $c2.$label

    if ($v1 -eq $v2 -and (Is-Count $v1) -and [int]$v1 -gt 0) {

      Write-Host "  ATTENTION: les 2 conseillers voient le meme nombre de $label ($v1)" -ForegroundColor Yellow

    }

    if ($v1 -match '^ERR:' -or $v2 -match '^ERR:') {

      Write-Host "  ERREUR API $label : c1=$v1 c2=$v2" -ForegroundColor Red

    }

  }

}



Write-Host "`n=== WORKFLOW PROPRIETAIRES (effectif-alpha) ===" -ForegroundColor Cyan

foreach ($u in @('commission_1_conseiller_1','commission_1_coordonnateur_1')) {

  $login = Login $u

  if (-not $login.ok) { continue }

  $rows = WorkflowOwners $login.token '/api/effectif-alpha'

  Write-Host "$u : $($rows.Count) lignes visibles"

  $rows | Select-Object -First 5 | ForEach-Object { Write-Host "  id=$($_.id) owner=$($_.owner) statut=$($_.statut)" }

}



Write-Host "`n=== WORKFLOW PROPRIETAIRES (controle / evaluation / performance) ===" -ForegroundColor Cyan

foreach ($resource in @('/api/controle','/api/evaluation','/api/performance')) {

  Write-Host "-- $resource --"

  foreach ($u in @('commission_1_conseiller_1','commission_1_coordonnateur_1')) {

    $login = Login $u

    if (-not $login.ok) { continue }

    $rows = WorkflowOwners $login.token $resource

    Write-Host "$u : $($rows.Count) lignes"

    $rows | Select-Object -First 3 | ForEach-Object { Write-Host "  id=$($_.id) owner=$($_.owner) statut=$($_.statut)" }

  }

}



Write-Host "`n=== DOCUMENTS - conseillers volumetrie ===" -ForegroundColor Cyan

if ($c1 -and $c2) {

  $sameDocs = ($c1.documents -eq $c2.documents)

  if ($sameDocs -and (Is-Count $c1.documents) -and [int]$c1.documents -gt 5) {

    Write-Host "ATTENTION: conseiller_1 et conseiller_2 voient le meme volume documents ($($c1.documents))" -ForegroundColor Yellow

  } else {

    Write-Host "OK: volumes documents differents ou faibles (c1=$($c1.documents) c2=$($c2.documents))" -ForegroundColor Green

  }

}

$central = $results | Where-Object { $_.user -eq 'superviseur_aenf_test' }

if ($central -and $c1 -and (Is-Count $central.documents) -and (Is-Count $c1.documents)) {

  if ([int]$central.documents -ge [int]$c1.documents) {

    Write-Host "OK: central ($($central.documents)) >= conseiller ($($c1.documents))" -ForegroundColor Green

  }

}



Write-Host "`n=== ERREURS HTTP ===" -ForegroundColor Cyan

$errs = $results | ForEach-Object {

  $props = @('controle','evaluation','performance','visite','documents','effectifAlpha')

  foreach ($p in $props) {

    if ($_.$p -match '^ERR:') {

      [pscustomobject]@{ user = $_.user; resource = $p; status = $_.$p }

    }

  }

}

if ($errs) {

  $errs | ForEach-Object { Write-Host "  $($_.user) $($_.resource) $($_.status)" -ForegroundColor Red }

} else {

  Write-Host "  Aucune erreur HTTP sur les ressources auditees" -ForegroundColor Green

}



Write-Host "`n=== FIN ===" -ForegroundColor Cyan

