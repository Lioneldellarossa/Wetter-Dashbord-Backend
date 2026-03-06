# Wetter Dashboard API

Ein Java 21 Spring Boot REST API Backend für ein Wetter- und Lüftungs-Dashboard.

## Technologie-Stack
* **Java:** 21
* **Framework:** Spring Boot 3.5.0
* **Build Tool:** Gradle
* **Datenbank:** MySQL 8.0 (mit Docker Compose)
* **ORM:** Spring Data JPA / Hibernate
* **Hilfstools:** Lombok, Spring Boot Validation

---

## Voraussetzung
* Java 21 JDK installiert
* Docker und Docker Compose installiert

---

## Lokale Entwicklung und Start

### 1. Datenbank starten
Im Hauptverzeichnis befindet sich eine `docker-compose.yml` Datei, die eine MySQL Datenbank hochfährt und automatisch die benötigten Tabellen (`locations`, `weather_metrics`, `ventilation_cycles`) sowie die Dashboard-View erstellt.

Führe folgenden Befehl aus:
```bash
docker-compose up -d
```
*Die Datenbank ist dann unter `localhost:3306` erreichbar (Benutzer: `user`, Passwort: `password`, Datenbank: `wetter_dashboard`).*

### 2. Spring Boot Applikation starten
Nachdem die Datenbank läuft, kannst du das Backend über Gradle starten:

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
