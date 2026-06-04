# Begleitdokument zum Leistungsnachweis Softwaredesign
**Thema:** Smart-Home-Szenario-Editor  
**Modul:** Anwendungsentwicklung III: Software Engineering

**Studierende** Anna Knötgen, Marcel Kecker, Marcel Woker, Viktor Kalka

---

### 1. Anwendungsstruktur & Architektur
Die Anwendung implementiert strikt das **Model-View-Controller (MVC) Architekturmuster** zur sauberen Entkopplung von Fachlogik, Datenhaltung und Präsentationsebene:

- **Model (Fachlogik & Daten):** Beinhaltet die Domänenobjekte wie `Raum`, `Device` (sowie die konkreten Implementierungen `Lamp`, `Heating`, `Shutter`) und `Scenario`. Das Model besitzt keinerlei Kenntnisse oder Abhängigkeiten zur grafischen Benutzeroberfläche (GUI).
- **View (Präsentation):** Realisiert durch die JavaFX-Komponenten innerhalb von `SmartHomeApp.java`. Sie bindet Steuerelemente an die Datenstrukturen und visualisiert die Zustände.
- **Controller / Service-Layer (Vermittlung):** Die Klassen `RoomService`, `DeviceService` und `ScenarioService` verwalten den globalen Zustand der Anwendung im Arbeitsspeicher, steuern die Datenströme und kapseln die Geschäftslogik ab.

---

### 2. GUI-Aufbau und Interaktionskonzept
Das GUI-Layout folgt dem im Anhang des Leistungsnachweises empfohlenen Drei-Spalten-Prinzip für Desktop-Anwendungen:

- **Hauptfenster (`BorderPane`):** Bildet den äußeren Rahmen.
- **TopBar (`North`):** Enthält persistente Aktionen (Neu, Öffnen, Speichern) sowie eine globale Schnellwahl zur direkten Ausführung von Szenarien aus jeder Ansicht heraus.
- **Navigationsbereich / Sidebar (`West`):** Eine übersichtliche Seitenleiste zur Umschaltung des Arbeitsbereichs zwischen den Kernbereichen *Räume*, *Geräte* und *Szenarien*.
- **Arbeitsbereich (`Center`):** Dynamischer Workspace, der je nach Auswahl Tabellenansichten (`TableView`), Listen (`ListView`) oder detaillierte Formulare/Editoren darstellt.
- **Zusatzbereich / Aktivitäts-Log (`East`):** Eine persistente `TextArea` zur Echtzeit-Protokollierung von Ereignissen (z. B. Zustandsänderungen von Aktoren nach einer Szenario-Ausführung) inklusive automatischem Zeitstempel.

Das Styling wurde mithilfe des modernen **AtlantaFX-Themes** (*PrimerLight*) und einer externen `style.css` umgesetzt, um eine professionelle und konsistente Benutzerführung zu garantieren.

---

### 3. Verwendete Softwarepakete und Werkzeuge
- **Java SE Development Kit (JDK) 17:** Programmiergrundlage.
- **JavaFX 21 (org.openjfx):** Framework für die grafische Oberfläche.
- **AtlantaFX (io.github.mkpaz):** CSS-Theme-Bibliothek für native Steuerelemente.
- **JUnit:** Testframework für automatisierte Unittests des Model-Layers.
- **Apache Maven:** Build-Management und Dependency-Inversion-Werkzeug.

---

### 4. Eingesetzte Entwurfsmuster (Design Patterns)
Zur Einhaltung von Kernprinzipien des objektorientierten Designs wurden folgende Entwurfsmuster gezielt implementiert:

1. **Command-Muster (Befehl):** Die Ausführung von Szenarioaktionen ist über ein `Command`-Interface gekapselt. Konkrete Klassen wie `SetBrightnessLampCommand` oder `SetTemperatureHeatingCommand` kapseln den Zustand, den Zielwert und den Empfänger (Aktor). Dies ermöglicht eine flexible Erweiterung, Sequenzierung und Entkopplung der Ausführung.
2. **Factory-Muster (Fabrik):** Die Instanziierung von Geräten erfolgt zentral über die `DeviceFactory`. Dadurch wird die GUI von der konkreten Klassenerzeugung entkoppelt. Das System kennt zum Erstellungszeitpunkt nur den generischen Typ, die Fabrik liefert das passende Objekt zurück.
3. **Observer-Muster (Beobachter):** Über das Event-Handling von JavaFX (z. B. `valueProperty().addListener(...)` oder `ObservableList`) reagiert die GUI automatisch und verzögerungsfrei auf Änderungen im darunterliegenden Model. Sobald ein Szenario ausgeführt wird, aktualisieren sich die Tabellen im Center-Bereich synchron.

---

### 5. Erweiterbarkeit um neue Gerätetypen
Das System berücksichtigt konsequent das **Open/Closed-Prinzip**. Um einen komplett neuen Gerätetyp (z. B. `AirConditioner` oder `SmartLock`) einzuführen, sind **keine** Modifikationen am bestehenden Kerncode oder der SmartHomeApp notwendig:

1. Ein neuer Gerätetyp implementiert das bestehende `Device`-Interface oder erweitert die abstrakte Basisklasse innerhalb des `model.device.impl`-Packages.
2. Die Registrierung des neuen Enums im `DeviceType` reicht aus, um die automatische Koppelung im Aktions-Editor zu triggern.
3. *Erweiterungsausblick (Reflection):* Über Java-Reflection (`Class.forName()`) liest das System das Package dynamisch ein, sodass neue Typen zur Laufzeit ohne erneutes Kompilieren per Plug-and-Play zur Verfügung stehen.

---

### 6. Testkonzept und Qualitätssicherung
Die Qualitätssicherung ruht auf zwei Säulen:

- **Automatisiertes Testen (Model & Service):** Mittels JUnit werden isolierte Tests für die Fachlogik durchgeführt. Getestet wird das Erstellen von Räumen, das Hinzufügen von Aktoren sowie die korrekte Kaskadierung von Werteänderungen bei der Ausführung komplexer Szenarien. Eine Mindestabdeckung von **50% Line-Coverage** ist im Build-Prozess fest verankert.
- **GUI-Testing / Validierung:** Die Benutzeroberfläche fängt fehlerhafte Zustände interaktiv ab. Die Speichern-Schaltflächen in Dialogen (z. B. `openAddDevice`) werden über Listener dynamisch deaktiviert (`setDisable(true)`), solange Pflichtfelder leer sind oder ungültige Datentypen (z. B. Buchstaben im Temperaturfeld) eingegeben werden.

---

### 7. Build-Ablauf & Statische Codeanalyse
Der gesamte Build-Prozess ist über den Maven-Lifecycle standardisiert. Er erzwingt beim Aufruf von `mvn clean verify` die strikte Einhaltung des Qualitäts-Gateways in folgender Reihenfolge:

1. **`validate`:** Der Quellcode wird mittels **Checkstyle** gegen die strengen Formatierungsvorgaben des *Google Java Styles* geprüft.
2. **`compile`:** Kompilierung des Quellcodes auf Basis von JDK 17.
3. **`test`:** Ausführung aller JUnit-Tests. Schlägt ein funktionaler Test fehl, bricht Maven ab.
4. **`verify`:** Aktivierung von **PMD** (Suche nach ungenutzten Variablen, Code-Duplikaten) und **SpotBugs** (Laufzeitsicherheits-Prüfung). Insgesamt sind weit über die geforderten 10 Kernregeln aktiv. Erst wenn alle statischen Codeanalysen fehlerfrei sind, wird das lauffähige JAR-Artefakt erzeugt.

### 8. Starten der Anwendung
`mvn javafx:run`