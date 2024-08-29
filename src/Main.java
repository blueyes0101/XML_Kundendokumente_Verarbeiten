import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        XMLHandler xmlHandler = new XMLHandler();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Online-Shop Verwaltungssystem ===");
            System.out.println("Bitte wählen Sie die gewünschte Aktion:");
            System.out.println("1. Neue Bestell-XML-Datei erstellen");
            System.out.println("2. Produkt zur bestehenden Bestellung hinzufügen");
            System.out.println("3. Produkt aus Bestellung entfernen");
            System.out.println("4. Bestell-XML-Datei löschen");
            System.out.println("5. Neue Rechnungs-XML-Datei erstellen");
            System.out.println("6. Zahlungsinformationen zur Rechnung hinzufügen");
            System.out.println("7. Bestell- oder Rechnungs-XML-Datei anzeigen");
            System.out.println("8. Beenden");
            System.out.print("Ihre Wahl: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Zeilenumbruch bereinigen

            switch (choice) {
                case 1:
                    System.out.print("Bitte geben Sie die Bestellnummer ein (z.B. 123): ");
                    String orderNumber = scanner.nextLine();

                    System.out.print("Bitte geben Sie den Kundenvorname ein: ");
                    String firstName = scanner.nextLine();

                    System.out.print("Bitte geben Sie den Kundennachname ein: ");
                    String lastName = scanner.nextLine();

                    System.out.print("Bitte geben Sie die Kundenadresse ein: ");
                    String address = scanner.nextLine();

                    // Zeitstempel erstellen
                    LocalDate now = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                    String timestamp = now.format(formatter);

                    // Dateiname erstellen (z.B. bestellung123_20231005.xml)
                    String orderFileName = "bestellung" + orderNumber + "_" + timestamp + ".xml";

                    // Neue Bestellung erstellen und Kundeninformationen hinzufügen
                    xmlHandler.createOrderXMLFile(orderFileName, "Bestellung", firstName, lastName, address, timestamp);
                    break;
                case 2:
                    // Bestehende Bestelldateien auflisten
                    xmlHandler.listXMLFilesInDirectory("bestellungen");
                    System.out.print("Bitte geben Sie den Namen der Datei ein, zu der Sie ein Produkt hinzufügen möchten: ");
                    String fileToAdd = scanner.nextLine();
                    fileToAdd = fileToAdd.endsWith(".xml") ? fileToAdd : fileToAdd + ".xml";

                    // Produkt zur Bestellung hinzufügen
                    xmlHandler.addProductToOrder(fileToAdd);
                    break;
                case 3:
                    // Bestehende Bestelldateien auflisten
                    xmlHandler.listXMLFilesInDirectory("bestellungen");
                    System.out.print("Bitte geben Sie den Namen der Datei ein, aus der Sie ein Produkt entfernen möchten: ");
                    String fileToRemove = scanner.nextLine();
                    fileToRemove = fileToRemove.endsWith(".xml") ? fileToRemove : fileToRemove + ".xml";

                    // Produkt aus Bestellung entfernen
                    xmlHandler.removeProductAndQuantityFromXML(fileToRemove);
                    break;
                case 4:
                    // Bestehende Bestelldateien auflisten
                    xmlHandler.listXMLFilesInDirectory("bestellungen");
                    System.out.print("Bitte geben Sie den Namen der zu löschenden Datei ein: ");
                    String fileToDelete = scanner.nextLine();
                    fileToDelete = fileToDelete.endsWith(".xml") ? fileToDelete : fileToDelete + ".xml";
                    xmlHandler.deleteXMLFile(fileToDelete);
                    break;
                case 5:
                    // Neue Rechnungs-XML-Datei erstellen
                    System.out.print("Bitte geben Sie die Kundennummer ein: ");
                    String customerNumber = scanner.nextLine();

                    // Rechnungsdatum und Fälligkeitsdatum festlegen
                    String invoiceDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
                    String dueDate = LocalDate.now().plusDays(30).format(DateTimeFormatter.ISO_DATE);

                    // Fatura erstellen
                    xmlHandler.createInvoiceForCustomer(customerNumber, invoiceDate, dueDate);
                    break;
                case 6:
                // Verfügbare Rechnungsdateien auflisten
                xmlHandler.listXMLFilesInDirectory("rechnungen");
                System.out.print("Bitte geben Sie den Namen der Datei ein, zu der Sie Zahlungsinformationen hinzufügen möchten: ");
                String fileToAddPayment = scanner.nextLine();
                fileToAddPayment = fileToAddPayment.endsWith(".xml") ? fileToAddPayment : fileToAddPayment + ".xml";
                
                System.out.print("Zahlungsmethode: ");
                String paymentMethod = scanner.nextLine();
                
                System.out.print("Zahlungsdatum (z.B. 2023-08-28): ");
                String paymentDate = scanner.nextLine();
                
                //test
                String filePath = "rechnungen" + File.separator + fileToAddPayment;
                xmlHandler.addElementToXML(filePath, "Rechnung", "Zahlungsmethode", paymentMethod);
                xmlHandler.addElementToXML(filePath, "Rechnung", "Zahlungsdatum", paymentDate);
                break;

                case 7:
                    // Bestehende Dateien auflisten
                    xmlHandler.listXMLFilesInDirectory("bestellungen");
                    System.out.print("Bitte geben Sie den Namen der Datei ein, die Sie anzeigen möchten: ");
                    String fileToPrint = scanner.nextLine();
                    fileToPrint = fileToPrint.endsWith(".xml") ? fileToPrint : fileToPrint + ".xml";
                    xmlHandler.printXMLContent(fileToPrint);
                    break;
                case 8:
                    System.out.println("Programm wird beendet... Auf Wiedersehen!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Ungültige Auswahl. Bitte wählen Sie eine Zahl zwischen 1 und 8.");
                    break;
            }
        }
    }
}
