@echo off

rem SET JAVA_HOME="C:\Program Files\Java\jdk1.8.0_151"
SET JAVA_HOME="C:\Program Files\Java\jdk1.8.0_151"
rem SET JAVA_HOME="C:\Program Files (x86)\Java\jre1.8.0_241"

SETLOCAL enabledelayedexpansion
cd ..

for /r . %%g in (\target\ImportSinacor-*.jar) do set s=%%g 
echo %s%

echo 'Iniciando ImportSinacor ... '
%JAVA_HOME%\bin\java -jar %s% -target .\target

pause