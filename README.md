# XML Kundendokumente Verarbeiten

## Zusammenfassung
Dieses Projekt entwickelt eine Java-Anwendung zur Verarbeitung von XML-basierten Kundendokumenten. Die Anwendung ermöglicht das Erstellen, Bearbeiten und Löschen von Bestellungen sowie das Generieren von Rechnungen. Sie ist plattformunabhängig und bietet eine benutzerfreundliche Kommandozeilenoberfläche.

## Funktionen
- **Bestell-XML-Datei erstellen:** Ermöglicht das Anlegen neuer Bestellungen.
- **Produkte hinzufügen:** Produkte können zu bestehenden Bestellungen hinzugefügt werden.
- **Produkte entfernen:** Entfernt Produkte aus bestehenden Bestellungen.
- **Bestell-XML-Dateien löschen:** Ermöglicht das Löschen von Bestellungen.
- **Rechnungen erstellen:** Generiert Rechnungen basierend auf Bestellungen.
- **Zahlungsinformationen hinzufügen:** Fügt Zahlungsinformationen zu bestehenden Rechnungen hinzu.
- **Bestell- oder Rechnungs-XML-Datei anzeigen:** Zeigt den Inhalt von Bestell- oder Rechnungsdateien an.

## Installation
1. Java installieren: Stellen Sie sicher, dass Java JDK 11 oder höher auf Ihrem System installiert ist.
2. Projekt klonen: Klonen Sie das Repository auf Ihr lokales System.
3. Navigieren Sie zum Projektverzeichnis.
4. Projekt kompilieren: Kompilieren Sie den Java-Code im Verzeichnis.
5. Anwendung ausführen: Starten Sie die Anwendung.

## Verwendung
Nach dem Start der Anwendung können Sie folgende Aktionen ausführen:
1. **Neue Bestell-XML-Datei erstellen:** Legen Sie eine neue Bestellung an, indem Sie Kundendaten eingeben.
2. **Produkt zur bestehenden Bestellung hinzufügen:** Fügen Sie Produkte zu einer bestehenden Bestellung hinzu.
3. **Produkt aus Bestellung entfernen:** Entfernen Sie ein Produkt aus einer bestehenden Bestellung.
4. **Bestell-XML-Datei löschen:** Löschen Sie eine bestehende Bestell-XML-Datei.
5. **Neue Rechnungs-XML-Datei erstellen:** Generieren Sie eine Rechnung basierend auf einer bestehenden Bestellung.
6. **Zahlungsinformationen zur Rechnung hinzufügen:** Fügen Sie Zahlungsinformationen zu einer bestehenden Rechnung hinzu.
7. **Bestell- oder Rechnungs-XML-Datei anzeigen:** Zeigen Sie den Inhalt einer bestehenden XML-Datei an.
8. **Beenden:** Beenden Sie die Anwendung.

## Verzeichnisstruktur
- `src/`: Enthält den Quellcode der Anwendung.
- `bin/`: Kompilierte Java-Klassen.
- `bestellungen/`: Verzeichnis, in dem die Bestell-XML-Dateien gespeichert werden.
- `rechnungen/`: Verzeichnis, in dem die Rechnungs-XML-Dateien gespeichert werden.
- `produktdaten/`: Verzeichnis, in dem die Produktliste gespeichert wird.

## Lizenz
Dieses Projekt steht unter der [MIT-Lizenz](LICENSE).
