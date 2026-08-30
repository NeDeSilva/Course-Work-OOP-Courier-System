# build.ps1 - compile the sources and produce the standalone cms.jar
# Usage: powershell -ExecutionPolicy Bypass -File build.ps1
# or, if you are already in PowerShell:  .\build.ps1
#
# The final cms.jar bundles the app classes plus the SQLite JDBC driver and
# the SLF4J API it needs, so it runs with:  java -jar cms.jar

$ErrorActionPreference = "Stop"
$root = $PSScriptRoot
$stage = Join-Path $env:TEMP "cms-build-stage"
$manifest = Join-Path $env:TEMP "cms-manifest.txt"

# 1. compile the app
Remove-Item -Recurse -Force (Join-Path $root "build") -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force -Path (Join-Path $root "build") | Out-Null
Push-Location $root
javac -encoding UTF-8 -cp "sqlite-jdbc.jar;slf4j-api.jar" -d build *.java
if ($LASTEXITCODE -ne 0) { throw "Compilation failed" }
Pop-Location

# 2. stage everything into one folder
Remove-Item -Recurse -Force $stage -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force -Path $stage | Out-Null
Copy-Item -Recurse -Force (Join-Path $root "build\*") $stage
Push-Location $stage
jar xf (Join-Path $root "sqlite-jdbc.jar")
jar xf (Join-Path $root "slf4j-api.jar")
Pop-Location

# remove any bundled signature files (would break the fat jar)
Get-ChildItem -Path (Join-Path $stage "META-INF") -File |
    Where-Object { $_.Extension -in ".SF", ".RSA", ".DSA", ".EC" } |
    Remove-Item -Force

# 3. create the manifest and build the jar
Set-Content -Path $manifest -Value "Main-Class: App`r`n" -NoNewline -Encoding ascii
Push-Location $stage
jar cfm (Join-Path $root "cms.jar") $manifest .
Pop-Location

Write-Host "Built: $(Join-Path $root 'cms.jar')"
