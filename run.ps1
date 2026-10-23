param(
    [ValidateSet('run', 'demo', 'test', 'extension', 'build')]
    [string]$Action = 'run'
)

$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    if ($env:JAVA_HOME -and (Test-Path "$env:JAVA_HOME\bin\javac.exe")) {
        $compiler = "$env:JAVA_HOME\bin\javac.exe"
    } else {
        $compilerCommand = Get-Command javac -ErrorAction SilentlyContinue
        if (-not $compilerCommand) {
            throw 'JDK 17 or newer is required. Set JAVA_HOME to its installation folder.'
        }
        $compiler = $compilerCommand.Source
    }
    $runtime = Join-Path (Split-Path $compiler) 'java.exe'
    New-Item -ItemType Directory -Force 'out' | Out-Null
    $sources = @(Get-ChildItem 'src', 'examples', 'tests' -Recurse -Filter '*.java' | ForEach-Object { $_.FullName })
    & $compiler --release 17 -encoding UTF-8 -Xlint:all -Werror -d out @sources
    if ($LASTEXITCODE -ne 0) { throw 'Compilation failed.' }
    switch ($Action) {
        'run' { & $runtime -cp out app.Main }
        'demo' { & $runtime -cp out app.Main --demo }
        'test' { & $runtime -cp out tests.CalculatorTest }
        'extension' { & $runtime -cp out example.DivisionDemo }
        'build' { Write-Output 'Build passed (Java 17 target).' }
    }
    if ($Action -ne 'build' -and $LASTEXITCODE -ne 0) { throw 'Execution failed.' }
} finally {
    Pop-Location
}
