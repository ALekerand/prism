# Simulation navigateur : CORS + login + /me + dashboard (séquentiel et parallèle)
param(
  [string]$BaseUrl = 'http://localhost:8080',
  [string]$Origin = 'http://localhost:4200',
  [string]$Username = 'admin',
  [string]$Password = 'admin123',
  [int]$LoginMaxMs = 2000,
  [int]$MeMaxMs = 2000,
  [int]$DashboardMaxMs = 8000
)

$ErrorActionPreference = 'Stop'
$failed = 0

function Write-Step($name, $ms, $max, $ok, $detail) {
  $status = if ($ok) { 'OK' } else { 'FAIL' }
  if (-not $ok) { $script:failed++ }
  Write-Host ("[{0}] {1} {2}ms (seuil {3}ms) {4}" -f $status, $name, $ms, $max, $detail)
}

function Invoke-Json {
  param($Method, $Url, $Headers, $Body, $TimeoutSec = 15)
  $sw = [System.Diagnostics.Stopwatch]::StartNew()
  try {
    if ($null -ne $Body) {
      $resp = Invoke-WebRequest -Uri $Url -Method $Method -Headers $Headers -Body $Body -ContentType 'application/json' -TimeoutSec $TimeoutSec -UseBasicParsing
    } else {
      $resp = Invoke-WebRequest -Uri $Url -Method $Method -Headers $Headers -TimeoutSec $TimeoutSec -UseBasicParsing
    }
    $sw.Stop()
    return @{ ok = $true; ms = $sw.ElapsedMilliseconds; status = [int]$resp.StatusCode; headers = $resp.Headers; body = $resp.Content }
  } catch {
    $sw.Stop()
    return @{ ok = $false; ms = $sw.ElapsedMilliseconds; err = $_.Exception.Message }
  }
}

Write-Host "=== Simulation login navigateur ==="
Write-Host "API $BaseUrl | Origin $Origin | user $Username"

$preflight = Invoke-Json -Method OPTIONS -Url "$BaseUrl/api/auth/login" -Headers @{
  Origin = $Origin
  'Access-Control-Request-Method' = 'POST'
  'Access-Control-Request-Headers' = 'content-type'
}
$acao = $preflight.headers['Access-Control-Allow-Origin']
Write-Step 'OPTIONS /api/auth/login' $preflight.ms 1500 $preflight.ok ("CORS=$acao")

$loginBody = @{ username = $Username; password = $Password } | ConvertTo-Json
$login = Invoke-Json -Method POST -Url "$BaseUrl/api/auth/login" -Headers @{ Origin = $Origin } -Body $loginBody
if (-not $login.ok) {
  Write-Step 'POST /api/auth/login' $login.ms $LoginMaxMs $false $login.err
  exit 1
}
$payload = $login.body | ConvertFrom-Json
$token = [string]$payload.token
$permCount = @($payload.permissions).Count
$roleJoin = ($payload.roles -join ',')
$loginOk = $login.ok -and $token -and ($login.ms -le $LoginMaxMs)
Write-Step 'POST /api/auth/login' $login.ms $LoginMaxMs $loginOk ("roles=$roleJoin perms=$permCount")

$authHeaders = @{ Origin = $Origin; Authorization = "Bearer $token" }

$me = Invoke-Json -Method GET -Url "$BaseUrl/api/auth/me" -Headers $authHeaders
$meOk = $me.ok -and ($me.ms -le $MeMaxMs)
Write-Step 'GET /api/auth/me' $me.ms $MeMaxMs $meOk ''

$dash = Invoke-Json -Method GET -Url "$BaseUrl/api/admin/dashboard" -Headers $authHeaders
$dashOk = $dash.ok -and ($dash.ms -le $DashboardMaxMs)
Write-Step 'GET /api/admin/dashboard' $dash.ms $DashboardMaxMs $dashOk ''

Write-Host '--- Parallèle me + dashboard (comme le navigateur après login) ---'
$sw = [System.Diagnostics.Stopwatch]::StartNew()
$jobs = @(
  Start-Job { param($u,$h) Invoke-WebRequest -Uri $u -Headers $h -TimeoutSec 15 -UseBasicParsing | Out-Null } -ArgumentList "$BaseUrl/api/auth/me", $authHeaders
  Start-Job { param($u,$h) Invoke-WebRequest -Uri $u -Headers $h -TimeoutSec 15 -UseBasicParsing | Out-Null } -ArgumentList "$BaseUrl/api/admin/dashboard", $authHeaders
)
$jobs | Wait-Job | Out-Null
$sw.Stop()
$jobFail = @($jobs | Where-Object { $_.State -ne 'Completed' }).Count
$jobs | Remove-Job -Force
Write-Step 'Parallèle /me + /dashboard' $sw.ElapsedMilliseconds 10000 ($jobFail -eq 0) "jobs_fail=$jobFail"

if ($failed -gt 0) {
  Write-Host "RESULTAT: $failed étape(s) en échec"
  exit 1
}
Write-Host 'RESULTAT: simulation OK'
exit 0
