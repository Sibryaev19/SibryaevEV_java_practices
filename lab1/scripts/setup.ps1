Param(
    [string]$WildFlyHome = "$env:WILDFLY_HOME",
    [string]$AdminUser = "admin",
    [string]$AdminPass = "admin",
    [string]$MgmtHost = "127.0.0.1",
    [int]$MgmtPort = 9990,
    [string]$PgHost = "localhost",
    [int]$PgPort = 5432,
    [string]$PgUser = "postgres",
    [string]$PgPass = "999111"
)

Write-Host "=== Проверка переменной WILDFLY_HOME ==="
if (-not $WildFlyHome -or -not (Test-Path $WildFlyHome)) {
    Write-Error "Укажите путь к WildFly через параметр -WildFlyHome или переменную окружения WILDFLY_HOME"; exit 1
}

$ModulesDir = Join-Path $WildFlyHome "modules\system\layers\base\org\postgresql\main"
New-Item -ItemType Directory -Force -Path $ModulesDir | Out-Null

$DriverUrl = "https://repo1.maven.org/maven2/org/postgresql/postgresql/42.7.4/postgresql-42.7.4.jar"
$DriverJar = Join-Path $ModulesDir "postgresql-42.7.4.jar"
Write-Host "=== Загрузка PostgreSQL JDBC драйвера ==="
Invoke-WebRequest -Uri $DriverUrl -OutFile $DriverJar -UseBasicParsing

$ModuleXml = @'
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.5" name="org.postgresql">
  <resources>
    <resource-root path="postgresql-42.7.4.jar"/>
  </resources>
  <dependencies>
    <module name="javax.api"/>
    <module name="javax.transaction.api"/>
  </dependencies>
  </module>
'@
Set-Content -Path (Join-Path $ModulesDir "module.xml") -Value $ModuleXml -Encoding UTF8

Write-Host "=== Настройка DataSource через jboss-cli ==="
$CliCmd = Join-Path $WildFlyHome "bin\jboss-cli.bat"
& $CliCmd -c --controller=$MgmtHost:$MgmtPort --user=$AdminUser --password=$AdminPass "
  batch
  /subsystem=datasources/jdbc-driver=postgresql:add(driver-name=postgresql,driver-module-name=org.postgresql,driver-class-name=org.postgresql.Driver)
  /subsystem=datasources/data-source=ProductDS:add(jndi-name=java:jboss/datasources/ProductDS, driver-name=postgresql, connection-url=jdbc:postgresql://$PgHost:$PgPort/product_db, user-name=$PgUser, password=$PgPass, min-pool-size=5, max-pool-size=20)
  run-batch
"

Write-Host "Готово. Проверьте DataSource 'ProductDS' в консоли WildFly."


