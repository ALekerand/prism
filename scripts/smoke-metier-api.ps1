# PRISM - smoke API metier (dossier + effectifs)
# Usage: pwsh scripts/smoke-metier-api.ps1

$ErrorActionPreference = 'Stop'
$base = $env:PRISM_API_BASE
if (-not $base) { $base = 'http://localhost:8080' }

function Assert-Status($resp, $expected, $label) {
  if ($resp.StatusCode -ne $expected) {
    throw "FAIL $label : HTTP $($resp.StatusCode) (attendu $expected)"
  }
  Write-Host "OK $label ($expected)"
}

Write-Host '=== Login ==='
$loginOk = $false
$token = $null
foreach ($pair in @(
  @{ u = 'admin'; p = 'admin123' },
  @{ u = 'nebdev'; p = 'nebdev' }
)) {
  try {
    $loginBody = @{ username = $pair.u; password = $pair.p } | ConvertTo-Json
    $login = Invoke-WebRequest -Uri "$base/api/auth/login" -Method POST -Body $loginBody -ContentType 'application/json' -UseBasicParsing
    Assert-Status $login 200 "login $($pair.u)"
    $token = ($login.Content | ConvertFrom-Json).token
    $loginOk = $true
    break
  } catch {
    Write-Host "Login $($pair.u) refuse"
  }
}
if (-not $loginOk -or -not $token) {
  throw 'Login impossible (admin/admin123, nebdev/nebdev)'
}
$headers = @{ Authorization = "Bearer $token" }

Write-Host '=== Lecture centres / catalogues dossier ==='
$endpoints = @(
  '/api/centres',
  '/api/alpha',
  '/api/cec',
  '/api/cp',
  '/api/sie',
  '/api/difficulte',
  '/api/impact',
  '/api/competence',
  '/api/infrastructure',
  '/api/designation',
  '/api/materielpedagogiques',
  '/api/SupportDidactiques',
  '/api/LangueApprentissages/catalog',
  '/api/difficulte-alpha',
  '/api/impact-alpha',
  '/api/competence-centre',
  '/api/materielalpha',
  '/api/support-didactique-alpha',
  '/api/infrastructure-centre',
  '/api/ressource-financiere-materiel'
)

foreach ($ep in $endpoints) {
  $r = Invoke-WebRequest -Uri "$base$ep" -Headers $headers -UseBasicParsing
  Assert-Status $r 200 "GET $ep"
}

Write-Host '=== Effectifs H/F (lecture) ==='
$effectifEps = @(
  '/api/effectif-cp',
  '/api/effectif-cec',
  '/api/effectif-sie',
  '/api/effectif-abandon-cp',
  '/api/effectif-cepe-cp',
  '/api/effectif-cepe-cec',
  '/api/effectif-admis-integration-cp',
  '/api/effectif-alpha'
)

foreach ($ep in $effectifEps) {
  try {
    $r = Invoke-WebRequest -Uri "$base$ep" -Headers $headers -UseBasicParsing
    Assert-Status $r 200 "GET $ep"
    $sample = $r.Content | ConvertFrom-Json
    if ($sample -is [Array] -and $sample.Count -gt 0) {
      $row = $sample[0]
      $keys = @($row.PSObject.Properties.Name)
      $hf = @($keys | Where-Object { $_ -match 'NiveauH$|NiveauF$|NiveauHomme$|NiveauFemme$' })
      if ($hf.Count -gt 0) {
        Write-Host ("  colonnes H/F: " + ($hf -join ', '))
      }
    }
  } catch {
    $code = $null
    if ($_.Exception.Response) {
      $code = [int]$_.Exception.Response.StatusCode
    }
    if ($code -eq 404) {
      Write-Host "SKIP $ep (404)"
    } else {
      throw "FAIL GET $ep : $_"
    }
  }
}

Write-Host '=== Smoke metier termine ==='
