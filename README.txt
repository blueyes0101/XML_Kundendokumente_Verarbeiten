XML Kundendokumente Verarbeiten

Projektbeschreibung

Dieses Projekt ist eine Java-Anwendung zur Verwaltung von XML-basierten Kundendokumenten wie Bestellungen und Rechnungen. Die Anwendung ermöglicht es dem Benutzer, neue Bestell-XML-Dateien zu erstellen, Produkte zu bestehenden Bestellungen hinzuzufügen oder zu entfernen, Bestell-XML-Dateien zu löschen, Rechnungen basierend auf den Bestellungen zu erstellen und Zahlungsinformationen zu den Rechnungen hinzuzufügen.

Voraussetzungen

- Java JDK 11 oder höher: Stellen Sie sicher, dass Java auf Ihrem System installiert ist.
- Kommandozeilen-Tool (CLI): Die Anwendung wird über die Kommandozeile bedient.

Installation

1 - Projekt-Repository klonen:

git clone

2 - Navigieren Sie zum Projektverzeichnis:

cd Abgabe-Projekt

3 - Kompilieren Sie das Projekt:

javac -d bin src/*.java

4 - Anwendung ausführen:

java -cp bin Main

Verwendung

Nach dem Start der Anwendung haben Sie verschiedene Optionen zur Auswahl:

1. Neue Bestell-XML-Datei erstellen: Erstellen Sie eine neue Bestellung, indem Sie die Kundendaten eingeben.
2. Produkt zur bestehenden Bestellung hinzufügen: Fügen Sie Produkte zu einer vorhandenen Bestellung hinzu.
3. Produkt aus Bestellung entfernen: Entfernen Sie ein Produkt aus einer vorhandenen Bestellung.
4. Bestell-XML-Datei löschen: Löschen Sie eine vorhandene Bestell-XML-Datei.
5. Neue Rechnungs-XML-Datei erstellen: Erstellen Sie eine Rechnung basierend auf einer vorhandenen Bestellung.
6. Zahlungsinformationen zur Rechnung hinzufügen: Fügen Sie Zahlungsinformationen zu einer vorhandenen Rechnung hinzu.
7. Bestell- oder Rechnungs-XML-Datei anzeigen: Zeigen Sie den Inhalt einer vorhandenen Bestell- oder Rechnungs-XML-Datei an.
8. Beenden: Beenden Sie die Anwendung.

Verzeichnisstruktur

src/: Enthält den Quellcode der Anwendung.
bin/: Kompilierte Java-Klassen.
bestellungen/: Verzeichnis, in dem die Bestell-XML-Dateien gespeichert werden.
rechnungen/: Verzeichnis, in dem die Rechnungs-XML-Dateien gespeichert werden.
produktdaten/: Verzeichnis, in dem die Produktliste gespeichert wird.

Bekannte Probleme

Bei falscher Eingabe könnten Fehler auftreten. Bitte stellen Sie sicher, dass Sie gültige Daten eingeben.
Sollten unerwartete Fehler auftreten, überprüfen Sie die Konsolenausgabe auf detaillierte Fehlermeldungen.

Mitwirkende

Mehmet Ozdag - 

Lizenz
Dieses Projekt ist unter der MIT-Lizenz lizenziert – siehe die LICENSE Datei für Details.


