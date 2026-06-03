# Smart Home Szenario-Editor

Eine JavaFX-basierte Desktop-Anwendung zur Simulation, Verwaltung und Ausführung von Smart-Home-Szenarien. Die Anwendung wurde im Rahmen des Moduls Software Engineering (Anwendungsentwicklung III) als Leistungsnachweis realisiert.

## Features & Funktionalitäten
- **Räume-Verwaltung:** Erstellen, Anzeigen, Bearbeiten und Löschen von Räumen.
- **Geräte-Verwaltung:** Dynamische Verwaltung von Aktoren (Lampen, Heizungen, Rollläden) inklusive automatischer Validierung der spezifischen Parameter (z. B. Helligkeit in %, Solltemperatur in °C).
- **Szenarien-Editor:** Verketten von mehreren Geräteaktionen zu logischen Szenarien (Reihenfolge-basiert), Steuerung über einen dedizierten Aktions-Editor.
- **Echtzeit-Simulation & Protokollierung:** Ausführen von Szenarien mit sofortiger Aktualisierung der Gerätezustände und Protokollierung aller Vorfälle in einem Aktivitäts-Log.

---

## Voraussetzungen & Technologien
- **Java:** Version 17 (JDK 17)
- **GUI-Framework:** JavaFX 21
- **UI-Theme:** AtlantaFX (PrimerLight Theme) für ein modernes, konsistentes Oberflächendesign
- **Build-Management:** Apache Maven (mind. Version 3.8)

---

## Build und Installation

Das Projekt nutzt Maven zur Automatisierung des Lifecycles. Das Build-Management fängt Fehler in Tests oder Codestil-Verstößen automatisch ab.

### 1. Projekt kompilieren & verifizieren
Um das Projekt vollständig zu bauen, Unit-Tests auszuführen und die statische Codeanalyse (Checkstyle, PMD, SpotBugs) anzustoßen:
```bash
mvn clean verify