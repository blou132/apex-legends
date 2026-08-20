param(
    [string]$AndroidSdk = "$env:LOCALAPPDATA\Android\Sdk"
)

$ErrorActionPreference = "Stop"

$sourceRoot = $PSScriptRoot
$analysisRoot = [System.IO.Path]::GetFullPath((Join-Path $sourceRoot "..\..\.."))
$localInputsRoot = [System.IO.Path]::GetFullPath((Join-Path $analysisRoot "LocalInputs\Phase15N"))
$buildRoot = [System.IO.Path]::GetFullPath((Join-Path $localInputsRoot "probe-build"))
if (-not $buildRoot.StartsWith($localInputsRoot + [System.IO.Path]::DirectorySeparatorChar, [System.StringComparison]::OrdinalIgnoreCase)) {
    throw "Refusing build cleanup outside the Phase15N LocalInputs directory: $buildRoot"
}
$classesRoot = Join-Path $buildRoot "classes"
$dexRoot = Join-Path $buildRoot "dex"
$androidJar = Join-Path $AndroidSdk "platforms\android-36.1\android.jar"
$buildTools = Join-Path $AndroidSdk "build-tools\36.1.0"
$aapt2 = Join-Path $buildTools "aapt2.exe"
$aapt = Join-Path $buildTools "aapt.exe"
$d8 = Join-Path $buildTools "d8.bat"
$zipalign = Join-Path $buildTools "zipalign.exe"
$apksigner = Join-Path $buildTools "apksigner.bat"
$manifest = Join-Path $sourceRoot "AndroidManifest.xml"
$keystore = Join-Path $buildRoot "local-debug.keystore"
$unsignedApk = Join-Path $buildRoot "graphics-probe-unsigned.apk"
$alignedApk = Join-Path $buildRoot "graphics-probe-aligned.apk"
$signedApk = Join-Path $buildRoot "graphics-probe.apk"

$requiredFiles = @($androidJar, $aapt2, $aapt, $d8, $zipalign, $apksigner, $manifest)
foreach ($requiredFile in $requiredFiles) {
    if (-not (Test-Path -LiteralPath $requiredFile)) {
        throw "Missing local build dependency: $requiredFile"
    }
}

$javaSources = @(Get-ChildItem -LiteralPath (Join-Path $sourceRoot "src") -Filter "*.java" -File -Recurse)
if ($javaSources.Count -eq 0) {
    throw "No Java source found under $sourceRoot\src"
}

New-Item -ItemType Directory -Path $classesRoot -Force | Out-Null
New-Item -ItemType Directory -Path $dexRoot -Force | Out-Null
Get-ChildItem -LiteralPath $classesRoot -Force -ErrorAction SilentlyContinue | Remove-Item -Recurse -Force
Get-ChildItem -LiteralPath $dexRoot -Force -ErrorAction SilentlyContinue | Remove-Item -Recurse -Force
Remove-Item -LiteralPath $unsignedApk, $alignedApk, $signedApk -Force -ErrorAction SilentlyContinue

$sourcePaths = @($javaSources | ForEach-Object FullName)
& javac.exe -source 8 -target 8 -encoding UTF-8 -classpath $androidJar -d $classesRoot @sourcePaths
if ($LASTEXITCODE -ne 0) {
    throw "javac failed with exit code $LASTEXITCODE"
}

$classPaths = @(Get-ChildItem -LiteralPath $classesRoot -Filter "*.class" -File -Recurse | ForEach-Object FullName)
& $d8 --lib $androidJar --min-api 26 --output $dexRoot @classPaths
if ($LASTEXITCODE -ne 0) {
    throw "d8 failed with exit code $LASTEXITCODE"
}

& $aapt2 link -o $unsignedApk -I $androidJar --manifest $manifest --min-sdk-version 26 --target-sdk-version 36 --version-code 1 --version-name 1.0
if ($LASTEXITCODE -ne 0) {
    throw "aapt2 link failed with exit code $LASTEXITCODE"
}

Copy-Item -LiteralPath (Join-Path $dexRoot "classes.dex") -Destination (Join-Path $buildRoot "classes.dex") -Force
Push-Location $buildRoot
try {
    & $aapt add (Split-Path -Leaf $unsignedApk) "classes.dex"
    if ($LASTEXITCODE -ne 0) {
        throw "aapt add failed with exit code $LASTEXITCODE"
    }
} finally {
    Pop-Location
}

& $zipalign -f 4 $unsignedApk $alignedApk
if ($LASTEXITCODE -ne 0) {
    throw "zipalign failed with exit code $LASTEXITCODE"
}

if (-not (Test-Path -LiteralPath $keystore)) {
    & keytool.exe -genkeypair -keystore $keystore -storepass android -keypass android -alias androiddebugkey -dname "CN=Local Graphics Probe,O=Local Development,C=FR" -keyalg RSA -keysize 2048 -validity 10000 -noprompt
    if ($LASTEXITCODE -ne 0) {
        throw "keytool failed with exit code $LASTEXITCODE"
    }
}

& $apksigner sign --ks $keystore --ks-pass pass:android --key-pass pass:android --out $signedApk $alignedApk
if ($LASTEXITCODE -ne 0) {
    throw "apksigner failed with exit code $LASTEXITCODE"
}

& $apksigner verify --verbose $signedApk
if ($LASTEXITCODE -ne 0) {
    throw "apksigner verification failed with exit code $LASTEXITCODE"
}

$apk = Get-Item -LiteralPath $signedApk
$hash = Get-FileHash -LiteralPath $signedApk -Algorithm SHA256
[pscustomobject]@{
    Result = "SUCCESS"
    ApkPath = $apk.FullName
    ApkSize = $apk.Length
    ApkSha256 = $hash.Hash
    InternetPermissionDeclared = $false
} | ConvertTo-Json
