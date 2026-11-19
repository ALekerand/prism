# Smoke : jeton invalide → HTTP 401 (pas 403) sur une API protégée.
param(
  [string]$BaseUrl = 'http://154.0.30.233:91/prism'
)

$ErrorActionPreference = 'Stop'
$headers = @{ Authorization = 'Bearer invalid.expired.token' }
try {
  Invoke-WebRequest -Uri "$BaseUrl/api/auth/me" -Headers $headers -UseBasicParsing | Out-Null
  Write-Error 'Attendu: échec 401, mais la requête a réussi.'
} catch {
  $status = $_.Exception.Response.StatusCode.value__
  if ($status -ne 401) {
    Write-Error "Statut $status au lieu de 401 pour jeton invalide."
  }
  Write-Host "OK: jeton invalide → $status"
}