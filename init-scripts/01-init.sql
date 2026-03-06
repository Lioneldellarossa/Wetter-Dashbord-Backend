-- Tabelle für die Standorte
CREATE TABLE locations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabelle für die kontinuierlichen Sensor- und Wetterdaten
CREATE TABLE weather_metrics (
    id INT AUTO_INCREMENT PRIMARY KEY,
    location_id INT NOT NULL,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    temperature DECIMAL(5,2),
    weather_condition VARCHAR(50),
    humidity_percent INT,
    wind_speed_kmh INT,
    pressure_hpa INT,
    cloud_cover_percent INT,
    precipitation_mm DECIMAL(5,2),
    air_quality_status VARCHAR(100),
    FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE
);

-- Tabelle für die berechneten Zyklen
CREATE TABLE ventilation_cycles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    location_id INT NOT NULL,
    calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ventilation_duration_minutes INT NOT NULL,
    closed_duration_hours DECIMAL(4,1) NOT NULL,
    next_cycle_target DATETIME NOT NULL,
    system_status VARCHAR(50) DEFAULT 'Warten',
    is_active TINYINT(1) DEFAULT 1, -- 1 = TRUE, 0 = FALSE
    FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE
);

-- View für das Frontend Dashboard
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
