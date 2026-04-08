# FleetGuard E2E Tests
> Serenity BDD + JUnit 5 + Screenplay Pattern

Suite de pruebas End-to-End para el frontend Next.js 14 de **FleetGuard MVP** — sistema de gestión de flotas con mantenimiento preventivo.

---

## Prerequisitos

| Requisito | Versión mínima |
|---|---|
| Java | 17 |
| Maven | 3.8+ (o usar `./mvnw` incluido) |
| Google Chrome | Última versión estable |
| Docker + Docker Compose | Para levantar el backend |

### Servicios que deben estar levantados

```bash
# Backend + RabbitMQ + BD
docker-compose up -d

# Frontend Next.js
cd fleetguard-frontend && npm run dev
# Disponible en: http://localhost:3000
```

Los microservicios estarán en:
- `http://localhost:8081` — fleet-service
- `http://localhost:8093` — rules-alerts-service

---

## Cómo ejecutar los tests

```bash
# Ejecutar toda la suite
./mvnw clean verify

# Ejecutar una clase específica
./mvnw verify -Dit.test=RegisterVehicleTests

# Ejecutar en modo headless (para CI)
./mvnw verify -Dheadless.mode=true

# Ver el reporte HTML
start target/site/serenity/index.html    # Windows
open target/site/serenity/index.html     # macOS/Linux
```

---

## Estructura del proyecto

```
src/test/java/com/fleetguard/e2e/
├── features/                          ← Clases de test (JUnit 5)
│   ├── RegisterVehicleTests.java      → 3 tests de /register
│   ├── RegisterMileageTests.java      → 3 tests de /mileage
│   ├── CreateRuleTests.java           → 2 tests de /rules
│   ├── RegisterServiceTests.java      → 3 tests de /services
│   ├── NavigationTests.java           → 1 test de sidebar
│   └── FullFlowTest.java              → Ciclo completo E2E (8 pasos)
└── screenplay/                        ← Screenplay Pattern
    ├── navigation/
    │   ├── NavigateTo.java            ← Factory de navigación
    │   └── SidebarLinks.java          ← Targets del sidebar
    ├── vehicle/
    │   ├── VehicleData.java           ← Record de datos de vehículo
    │   ├── VehicleForm.java           ← Targets de /register
    │   └── RegisterVehicle.java       ← Task: registrar vehículo
    ├── mileage/
    │   ├── MileageData.java           ← Record de datos de km
    │   ├── MileageForm.java           ← Targets de /mileage
    │   └── UpdateMileage.java         ← Task: actualizar km
    ├── rules/
    │   ├── RuleData.java              ← Record de regla de mantenimiento
    │   ├── RulesForm.java             ← Targets de /rules
    │   └── CreateMaintenanceRule.java ← Task: crear regla
    ├── services/
    │   ├── ServiceData.java           ← Record de datos de servicio
    │   ├── ServicesForm.java          ← Targets de /services
    │   ├── QueryAlerts.java           ← Task: consultar alertas (Paso 1)
    │   └── RegisterService.java       ← Task: registrar servicio (Paso 2)
    ├── notifications/
    │   ├── ToastTargets.java          ← Targets del toast global
    │   └── Toast.java                 ← Questions del toast
    └── utils/
        ├── TestDataGenerator.java     ← Generador de datos únicos (UUID)
        └── WaitForToast.java          ← Esperas del toast con WaitUntil
```

---

## Selectores usados

Todos los selectores XPath provienen del archivo [`SELECTORS.md`](./SELECTORS.md) generado inspeccionando el DOM real del frontend. **No se usan clases CSS de Tailwind** (hashean en builds de producción).

| Página | Estrategia de selección |
|---|---|
| `/register` | `By.name()` — todos los inputs tienen atributo `name` |
| `/mileage` | XPath `label/following-sibling::div//input` — sin `name` |
| `/rules` | `By.name()` para inputs; texto visible para botones toggle |
| `/services` | XPath por label (Paso 1); `By.name()` (Paso 2) |
| Toast | Multi-clase XPath: `fixed + bottom + right + rounded` |

---

## Notas importantes

- **El `FullFlowTest`** usa `Thread.sleep(3000)` para esperar el procesamiento asíncrono de RabbitMQ entre el registro de km y la generación de alertas. Es la única excepción al uso de `WaitUntil`.
- **`shouldRegisterServiceWhenAlertExists`** requiere un vehículo con alerta pre-existente (placa `TEST01`). Configurar seed data en docker-compose para este escenario.
- El **input de km** en `/mileage` dispara `onWheel.blur()` — `UpdateMileage` envía `Keys.TAB` después de `sendKeys()` para evitar interferencia del scroll.

---

## Reporte Serenity

Tras ejecutar `./mvnw clean verify`, el reporte HTML interactivo se genera en:

```
target/site/serenity/index.html
```
