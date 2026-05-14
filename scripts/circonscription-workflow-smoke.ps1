# Scénario manuel / smoke : connexion par rôle puis lecture paginée des centres (Alpha, CEC, CP, SIE).
# Prérequis : API Prism démarrée (ex. port 8080), comptes de test issus de ActivitesCentreRbacInitializer (mot de passe 123456).
param(
    [string] $BaseUrl = "http://localhost:8080"
)

$ErrorActionPreference = "Stop"

function Get-LoginToken {
    param([string] $Username, [string] $Password)
    $body = @{ username = $Username; password = $Password } | ConvertTo-Json
    $r = Invoke-RestMethod -Uri "$BaseUrl/api/auth/login" -Method Post -ContentType "application/json" -Body $body
    return $r.token
}

function Invoke-AuthedGet {
    param([string] $Token, [string] $Path)
    $h = @{ Authorization = "Bearer $Token" }
    return Invoke-RestMethod -Uri "$BaseUrl$Path" -Headers $h -Method Get
}

$accounts = @(
    @{ name = "conseiller"; user = "conseiller_test" },
    @{ name = "coordonnateur"; user = "coordonnateur_test" },
    @{ name = "superviseur_drena"; user = "superviseur_test" },
    @{ name = "superviseur_aenf_national"; user = "superviseur_aenf_test" }
)

foreach ($a in $accounts) {
    Write-Host "=== $($a.name) ($($a.user)) ===" -ForegroundColor Cyan
    try {
        $tok = Get-LoginToken -Username $a.user -Password "123456"
    } catch {
        Write-Host "  Login échoué (API injoignable ou compte absent): $($_.Exception.Message)" -ForegroundColor Yellow
        continue
    }
    foreach ($path in @("/api/alpha?page=0&size=5", "/api/cec?page=0&size=5", "/api/cp?page=0&size=5", "/api/sie?page=0&size=5")) {
        try {
            $page = Invoke-AuthedGet -Token $tok -Path $path
            $total = $page.totalElements
            $nb = ($page.content | Measure-Object).Count
            Write-Host "  $path -> totalElements=$total, contentCount=$nb"
        } catch {
            Write-Host "  $path -> erreur: $($_.Exception.Message)" -ForegroundColor Yellow
        }
    }
}

Write-Host "`nTerminé. Attendu : conseiller/coordonnateur bornés à l’IEP ; superviseur DRENA >= IEP ; AENF >= tous (totalElements souvent plus élevé)." -ForegroundColor Green
