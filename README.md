# Wetter Dashboard API

Ein Java 21 Spring Boot REST API Backend für ein Wetter- und Lüftungs-Dashboard.

## Technologie-Stack
* **Java:** 21
* **Framework:** Spring Boot 3.5.0
* **Build Tool:** Gradle
* **Datenbank:** Lokale MySQL Installation
* **ORM:** Spring Data JPA / Hibernate
* **Hilfstools:** Lombok, Spring Boot Validation

---

## Voraussetzung
* Java 21 JDK installiert
* Eine lokal installierte MySQL-Datenbank auf Port 3307

---

## Lokale Entwicklung und Start

### 1. Datenbank vorbereiten
Stellen Sie sicher, dass Ihre lokale MySQL-Datenbank läuft (auf `localhost:3307`).
Legen Sie darin ein Schema/eine Datenbank mit dem Namen `ventilation_db` an:
```sql
CREATE DATABASE ventilation_db;
```

**Hinweis:** In der `application.properties` sind aktuell folgende Zugangsdaten hinterlegt:
* **Benutzer:** root
* **Passwort:** 1234
* **Port:** 3307

Die Tabellen werden durch Hibernate (dank `ddl-auto: update`) beim ersten Start automatisch erstellt.

Um den Endpunkt `/api/dashboard/{locationId}` korrekt zu nutzen, müssen Sie zusätzlich **manuell** diesen View-Code in Ihrer MySQL-Datenbank (z.B. über phpMyAdmin, DBeaver oder MySQL Workbench) ausführen:

```sql
CREATE VIEW vw_current_dashboard AS
SELECT
    l.id AS location_id,
    l.name AS location_name,
    w.temperature,
    w.weather_condition,
    w.humidity_percent,
    w.wind_speed_kmh,
    w.pressure_hpa,
    w.cloud_cover_percent,
    w.precipitation_mm,
    w.air_quality_status,
    vc.ventilation_duration_minutes,
    vc.closed_duration_hours,
    vc.next_cycle_target,
    vc.system_status
FROM locations l
-- Holt die aktuellsten Wetterdaten über eine Subquery
LEFT JOIN weather_metrics w ON w.id = (
    SELECT id FROM weather_metrics
    WHERE location_id = l.id
    ORDER BY recorded_at DESC LIMIT 1
)
-- Holt den aktuell aktiven Lüftungszyklus über eine Subquery
LEFT JOIN ventilation_cycles vc ON vc.id = (
    SELECT id FROM ventilation_cycles
    WHERE location_id = l.id AND is_active = 1
    ORDER BY calculated_at DESC LIMIT 1
);
```

### 2. Spring Boot Applikation starten
Starten Sie das Backend über Gradle:

```bash
./gradlew bootRun
```
*Das Backend startet auf Port `8080`. Die API ist unter `http://localhost:8080/api/...` erreichbar.*

---

## API Endpunkte Dokumentation

### 1. Dashboard (Aggregation für das Frontend)

#### GET `/api/dashboard/{locationId}`
Gibt die aggregierten Daten für einen Standort zurück, bestehend aus den Location-Infos, dem **aktuellsten** Wetterdatensatz und dem **derzeit aktiven** Lüftungszyklus.

**Beispiel Response:**
```json
{
  "locationId": 1,
  "locationName": "Bern",
  "temperature": 4.00,
  "weatherCondition": "Sehr Kalt",
  "humidityPercent": 86,
  "windSpeedKmh": 3,
  "pressureHpa": 956,
  "cloudCoverPercent": 21,
  "precipitationMm": 0.00,
  "airQualityStatus": "Gute Luftqualität .",
  "ventilationDurationMinutes": 7,
  "closedDurationHours": 3.0,
  "nextCycleTarget": "2024-03-06T21:03:00",
  "systemStatus": "Warten"
}
```

---

### 2. Locations (Standorte)

#### GET `/api/locations`
Liste aller Standorte.

#### GET `/api/locations/{id}`
Einzelner Standort.

#### POST `/api/locations`
Neuen Standort erstellen.
**Request Body:**
```json
{
  "name": "Bern"
}
```

#### PUT `/api/locations/{id}`
Standortnamen aktualisieren.

#### DELETE `/api/locations/{id}`
Standort löschen (Löscht kaskadierend auch Wetterdaten und Zyklen).

---

### 3. Weather Metrics (Wetterdaten)

#### GET `/api/weather-metrics` / GET `/api/weather-metrics/{id}`
Wetterdaten abrufen.

#### POST `/api/weather-metrics`
Neue Wetterdaten für einen Standort eintragen. Dies wird i.d.R. von einem externen Sensor oder Cronjob aufgerufen.
**Request Body:**
```json
{
  "locationId": 1,
  "temperature": 4.0,
  "weatherCondition": "Sehr Kalt",
  "humidityPercent": 86,
  "windSpeedKmh": 3,
  "pressureHpa": 956,
  "cloudCoverPercent": 21,
  "precipitationMm": 0.0,
  "airQualityStatus": "Gute Luftqualität ."
}
```

#### DELETE `/api/weather-metrics/{id}`
Wetterdatensatz löschen.

---

### 4. Ventilation Cycles (Lüftungszyklen)

#### GET `/api/ventilation-cycles` / GET `/api/ventilation-cycles/{id}`
Lüftungszyklen abrufen.

#### POST `/api/ventilation-cycles`
Einen neu berechneten Lüftungszyklus speichern. **Wichtig:** Durch das Speichern eines neuen Zyklus wird der vorherige aktive Zyklus dieses Standorts automatisch deaktiviert (`isActive = false`).
**Request Body:**
```json
{
  "locationId": 1,
  "ventilationDurationMinutes": 7,
  "closedDurationHours": 3.0,
  "nextCycleTarget": "2024-03-06T21:03:00",
  "systemStatus": "Warten",
  "isActive": true
}
```

#### PATCH `/api/ventilation-cycles/{id}/status`
Schnelles Update des Systemstatus (z.B. vom Frontend, wenn sich der Timer ändert oder das Fenster geöffnet wurde).
**Request Body:**
```json
{
  "systemStatus": "Lüften"
}
```

#### DELETE `/api/ventilation-cycles/{id}`
Zyklus löschen.
