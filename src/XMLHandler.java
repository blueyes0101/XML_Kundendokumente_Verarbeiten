import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLHandler {

    private final String ordersDirectory = "bestellungen";
    private final String invoiceDirectory = "rechnungen";
    private final String productListFile = "produktdaten" + File.separator + "produktListe.xml";

    public XMLHandler() {
        createDirectory(ordersDirectory);
        createDirectory(invoiceDirectory);
        createDirectory("produktdaten");
        createDefaultProductList();
    }

    private void createDirectory(String dirName) {
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public void createOrderXMLFile(String fileName, String rootElement, String firstName, String lastName, String address, String timestamp) {
        try {
            String filePath = ordersDirectory + File.separator + fileName;
            Document doc = createDocument(rootElement);

            Element customerElement = doc.createElement("Kunde");
            customerElement.appendChild(createElement(doc, "Vorname", firstName));
            customerElement.appendChild(createElement(doc, "Nachname", lastName));
            customerElement.appendChild(createElement(doc, "Adresse", address));
            doc.getDocumentElement().appendChild(customerElement);

            doc.getDocumentElement().appendChild(createElement(doc, "Zeitstempel", timestamp));

            saveDocument(doc, filePath);
            System.out.println("Datei " + filePath + " wurde erfolgreich erstellt.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createInvoiceForCustomer(String customerNumber, String invoiceDate, String dueDate) {
        try {
            File directory = new File(ordersDirectory);
            File[] filesList = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
            if (filesList == null || filesList.length == 0) {
                System.out.println("Keine Bestelldateien gefunden.");
                return;
            }

            List<Element> lineItems = new ArrayList<>();
            double subtotal = 0.0;
            double totalTax = 0.0;
            String invoiceNumber = "INV-" + customerNumber;
            String customerFirstName = "";
            String customerLastName = "";
            String customerAddress = "";
            String customerTaxID = "Unbekannt";  // Def
            String customerEmail = "Unbekannt";  // Def

            Document invoiceDoc = createDocument("Invoice"); // neu 

            for (File file : filesList) {
                DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = docBuilder.parse(file);

                NodeList customerNodes = doc.getElementsByTagName("Kunde");
                if (customerNodes.getLength() > 0 && file.getName().contains(customerNumber)) {
                    Element customerElement = (Element) customerNodes.item(0);
                    customerFirstName = customerElement.getElementsByTagName("Vorname").item(0).getTextContent();
                    customerLastName = customerElement.getElementsByTagName("Nachname").item(0).getTextContent();
                    customerAddress = customerElement.getElementsByTagName("Adresse").item(0).getTextContent();

                    NodeList productList = doc.getElementsByTagName("Produkt");
                    NodeList quantityList = doc.getElementsByTagName("Menge");

                    for (int i = 0; i < productList.getLength(); i++) {
                        String productName = productList.item(i).getTextContent();
                        double productPrice = getProductPrice(productName);
                        int quantity = Integer.parseInt(quantityList.item(i).getTextContent());
                        double lineTotal = productPrice * quantity;
                        double taxRate = 18.0; // tax
                        double taxAmount = lineTotal * (taxRate / 100);

                        subtotal += lineTotal;
                        totalTax += taxAmount;

                        // clon
                        Element lineItem = createLineItem(productName, quantity, productPrice, lineTotal, taxRate, taxAmount);
                        Node importedNode = invoiceDoc.importNode(lineItem, true);
                        lineItems.add((Element) importedNode);
                    }
                }
            }

            double totalAmount = subtotal + totalTax;
            createInvoiceXML(invoiceDoc, invoiceNumber, customerFirstName, customerLastName, customerAddress, customerTaxID, customerEmail, "Banküberweisung", invoiceDate, dueDate, lineItems, subtotal, totalTax, totalAmount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Document createDocument(String rootElement) throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement(rootElement);
        doc.appendChild(root);
        return doc;
    }

    private Element createElement(Document doc, String name, String value) {
        Element element = doc.createElement(name);
        element.appendChild(doc.createTextNode(value));
        return element;
    }

    private void saveDocument(Document doc, String filePath) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }

    private void createInvoiceXML(Document invoiceDoc, String invoiceNumber, String customerFirstName, String customerLastName, String customerAddress, String customerTaxID, String customerEmail, String paymentMethod, String invoiceDate, String dueDate, List<Element> lineItems, double subtotal, double totalTax, double totalAmount) throws Exception {

        Element rootElement;
        
        // test
        if (invoiceDoc.getDocumentElement() == null) {
            // test
            rootElement = invoiceDoc.createElement("Rechnung");
            invoiceDoc.appendChild(rootElement);
        } else {
            // test
            rootElement = invoiceDoc.getDocumentElement();
        }
    
        Element invoiceInfo = invoiceDoc.createElement("InvoiceInformation");
        invoiceInfo.appendChild(createElement(invoiceDoc, "InvoiceNumber", invoiceNumber));
        invoiceInfo.appendChild(createElement(invoiceDoc, "InvoiceDate", invoiceDate));
        invoiceInfo.appendChild(createElement(invoiceDoc, "DueDate", dueDate));
        invoiceInfo.appendChild(createElement(invoiceDoc, "CurrencyCode", "CHF"));
        rootElement.appendChild(invoiceInfo);
    
        Element supplierInfo = invoiceDoc.createElement("SupplierInformation");
        supplierInfo.appendChild(createElement(invoiceDoc, "SupplierName", "Ozdag GmBh"));
        supplierInfo.appendChild(createElement(invoiceDoc, "SupplierAddress", "Muster1str 22 9999 Muster"));
        supplierInfo.appendChild(createElement(invoiceDoc, "SupplierTaxID", "1234567890"));
        supplierInfo.appendChild(createElement(invoiceDoc, "SupplierContact", "info@info.com"));
        rootElement.appendChild(supplierInfo);
    
        Element customerInfo = invoiceDoc.createElement("CustomerInformation");
        customerInfo.appendChild(createElement(invoiceDoc, "CustomerName", customerFirstName + " " + customerLastName));
        customerInfo.appendChild(createElement(invoiceDoc, "CustomerAddress", customerAddress));
        customerInfo.appendChild(createElement(invoiceDoc, "CustomerTaxID", customerTaxID));
        customerInfo.appendChild(createElement(invoiceDoc, "CustomerContact", customerEmail));
        rootElement.appendChild(customerInfo);
    
        Element lineItemsElement = invoiceDoc.createElement("LineItems");
        for (Element item : lineItems) {
            lineItemsElement.appendChild(invoiceDoc.importNode(item, true));
        }
        rootElement.appendChild(lineItemsElement);
    
        Element summaryInfo = invoiceDoc.createElement("SummaryInformation");
        summaryInfo.appendChild(createElement(invoiceDoc, "SubtotalAmount", String.format("%.2f", subtotal)));
        summaryInfo.appendChild(createElement(invoiceDoc, "TaxAmount", String.format("%.2f", totalTax)));
        summaryInfo.appendChild(createElement(invoiceDoc, "TotalAmount", String.format("%.2f", totalAmount)));
        rootElement.appendChild(summaryInfo);
    
        Element paymentInfo = invoiceDoc.createElement("PaymentInformation");
        paymentInfo.appendChild(createElement(invoiceDoc, "PaymentMethod", paymentMethod));
        Element bankDetails = invoiceDoc.createElement("BankAccountDetails");
        bankDetails.appendChild(createElement(invoiceDoc, "BankName", "Örnek Bank"));
        bankDetails.appendChild(createElement(invoiceDoc, "IBAN", "TR123456789012345678901234"));
        bankDetails.appendChild(createElement(invoiceDoc, "SWIFT", "BANKTRIS"));
        paymentInfo.appendChild(bankDetails);
        paymentInfo.appendChild(createElement(invoiceDoc, "PaymentTerms", "30 days from invoice date"));
        rootElement.appendChild(paymentInfo);
    
        String filePath = invoiceDirectory + File.separator + invoiceNumber + ".xml";
        saveDocument(invoiceDoc, filePath);
        System.out.println("Die Rechnung wurde erfolgreich erstellt: " + filePath);
    }

    private Element createLineItem(String description, int quantity, double unitPrice, double totalAmount, double taxRate, double taxAmount) throws ParserConfigurationException {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        Element lineItem = doc.createElement("LineItem");
        lineItem.appendChild(createElement(doc, "ItemDescription", description));
        lineItem.appendChild(createElement(doc, "ItemQuantity", String.valueOf(quantity)));
        lineItem.appendChild(createElement(doc, "ItemUnitPrice", String.format("%.2f", unitPrice)));
        lineItem.appendChild(createElement(doc, "ItemTotalAmount", String.format("%.2f", totalAmount)));
        lineItem.appendChild(createElement(doc, "ItemTaxRate", String.valueOf(taxRate)));
        lineItem.appendChild(createElement(doc, "ItemTaxAmount", String.format("%.2f", taxAmount)));

        return lineItem;
    }

    public void addProductToOrder(String fileName) {
        try {
            String filePath = ordersDirectory + File.separator + fileName;
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("Die Datei existiert nicht.");
                return;
            }

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);

            List<String> productList = getProductList();
            System.out.println("Verfügbare Produkte:");
            for (int i = 0; i < productList.size(); i++) {
                System.out.println((i + 1) + ". " + productList.get(i));
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("Bitte wählen Sie die Produktnummer, die Sie hinzufügen möchten: ");
            int productChoice = scanner.nextInt();
            scanner.nextLine();  // Zeilenumbruch bereinigen

            if (productChoice > 0 && productChoice <= productList.size()) {
                String productName = productList.get(productChoice - 1);
                System.out.print("Menge: ");
                String quantity = scanner.nextLine();

                Element root = doc.getDocumentElement();
                root.appendChild(createElement(doc, "Produkt", productName));
                root.appendChild(createElement(doc, "Menge", quantity));

                saveDocument(doc, filePath);
                System.out.println("Produkt erfolgreich hinzugefügt.");
            } else {
                System.out.println("Ungültige Auswahl.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeProductAndQuantityFromXML(String fileName) {
        try {
            String filePath = ordersDirectory + File.separator + fileName;
            File file = new File(filePath);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);

            NodeList productList = doc.getElementsByTagName("Produkt");
            NodeList quantityList = doc.getElementsByTagName("Menge");

            if (productList.getLength() > 0) {
                System.out.println("Verfügbare Produkte in dieser Bestellung:");
                for (int i = 0; i < productList.getLength(); i++) {
                    System.out.println((i + 1) + ". " + productList.item(i).getTextContent());
                }

                Scanner scanner = new Scanner(System.in);
                System.out.print("Bitte wählen Sie die Nummer des Produkts, das Sie entfernen möchten: ");
                int productIndex = scanner.nextInt() - 1;

                if (productIndex >= 0 && productIndex < productList.getLength()) {
                    Element productElement = (Element) productList.item(productIndex);
                    Element quantityElement = (Element) quantityList.item(productIndex);

                    productElement.getParentNode().removeChild(productElement);
                    quantityElement.getParentNode().removeChild(quantityElement);

                    saveDocument(doc, filePath);
                    System.out.println("Produkt und Menge erfolgreich entfernt.");
                } else {
                    System.out.println("Ungültige Auswahl.");
                }
            } else {
                System.out.println("Keine Produkte in dieser Bestellung gefunden.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteXMLFile(String fileName) {
        String filePath = ordersDirectory + File.separator + fileName;
        File file = new File(filePath);
        if (file.delete()) {
            System.out.println("Die Datei " + filePath + " wurde gelöscht.");
        } else {
            System.out.println("Das Löschen der Datei war nicht erfolgreich.");
        }
    }

    public void printXMLContent(String fileName) {
        try {
            String filePath = ordersDirectory + File.separator + fileName;
            File file = new File(filePath);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listXMLFilesInDirectory(String directory) {
        File dir = new File(directory);
        File[] filesList = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".xml"));

        if (filesList != null && filesList.length > 0) {
            System.out.println("Verfügbare XML-Dateien:");
            for (File file : filesList) {
                String fileNameWithoutExtension = file.getName().replaceFirst("[.][^.]+$", "");
                System.out.println("- " + fileNameWithoutExtension);
            }
        } else {
            System.out.println("Keine .xml-Dateien in diesem Verzeichnis gefunden.");
        }
    }

    public void addElementToXML(String fileName, String parentElement, String newElement, String elementValue) {
        try {
            // file
            File file = new File(fileName);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);
    
            NodeList nodeList = doc.getElementsByTagName(parentElement);
            if (nodeList.getLength() == 0) {
                System.out.println("Das Element '" + parentElement + "' wurde in der Datei nicht gefunden: " + fileName);
                return;
            }
            Element root = (Element) nodeList.item(0);
    
            Element element = doc.createElement(newElement);
            element.appendChild(doc.createTextNode(elementValue));
            root.appendChild(element);
    
            // XML 
            saveDocument(doc, fileName);
            System.out.println(newElement + " wurde zur Datei " + fileName + " hinzugefügt.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public List<String> getProductList() {
        List<String> productList = new ArrayList<>();
        try {
            File file = new File(productListFile);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);

            NodeList nodeList = doc.getElementsByTagName("name");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                productList.add(element.getTextContent());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }

    private double getProductPrice(String productName) {
        try {
            File file = new File(productListFile);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);

            NodeList productList = doc.getElementsByTagName("produkt");
            for (int i = 0; i < productList.getLength(); i++) {
                Element productElement = (Element) productList.item(i);
                String name = productElement.getElementsByTagName("name").item(0).getTextContent();
                if (name.equalsIgnoreCase(productName)) {
                    return Double.parseDouble(productElement.getElementsByTagName("preis").item(0).getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private void createDefaultProductList() {
        try {
            File file = new File(productListFile);
            if (!file.exists()) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();

                Element rootElement = doc.createElement("produkte");
                doc.appendChild(rootElement);

                rootElement.appendChild(createProduct(doc, "Computer", "1000"));
                rootElement.appendChild(createProduct(doc, "Smartphone", "800"));
                rootElement.appendChild(createProduct(doc, "Tablet", "600"));

                saveDocument(doc, productListFile);
                System.out.println("Die Standardproduktliste wurde erstellt.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Element createProduct(Document doc, String name, String price) {
        Element produkt = doc.createElement("produkt");

        Element nameElement = doc.createElement("name");
        nameElement.appendChild(doc.createTextNode(name));
        produkt.appendChild(nameElement);

        Element priceElement = doc.createElement("preis");
        priceElement.appendChild(doc.createTextNode(price));
        produkt.appendChild(priceElement);

        return produkt;
    }
}
