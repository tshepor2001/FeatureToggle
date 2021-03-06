package com.test.feature.toggle;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLStorage implements Storage {

    private Document document;
    private String filePath;

    public XMLStorage() throws IOException, SAXException, ParserConfigurationException, TransformerException {
        initialiseDocument();

    }

    private void initialiseDocument() throws ParserConfigurationException, TransformerException, SAXException, IOException {
        this.filePath = System.getProperty(EnumStorageConfiguration.XML_STORAGE_LOCATION.getValue());
        if (filePath == null) {
            filePath = EnumStorageConfiguration.DEFAULT_STORAGE_LOCATION.getValue();
        }
        if (!fileExists()) {
            createFile();
        }

        document = loadDocument();
    }

    private void createFile() throws ParserConfigurationException, TransformerException, IOException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element features = doc.createElement("features");
        doc.appendChild(features);

        Element feature = doc.createElement("feature");
        feature.setAttribute("id", "1");
        Element name = doc.createElement("name");
        name.appendChild(doc.createTextNode("default"));
        feature.appendChild(name);
        Element active = doc.createElement("active");
        active.appendChild(doc.createTextNode(String.valueOf(false)));
        feature.appendChild(active);
        features.appendChild(feature);


        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        file.createNewFile();
        StreamResult result = new StreamResult(file);

        transformer.transform(source, result);
    }

    private boolean fileExists() {
        return new File(filePath).exists();
    }

    @Override
    public List<Feature> retrieveFeatures() {
        NodeList featuresNodes = document.getElementsByTagName("feature");
        List<Feature> result = new ArrayList<Feature>();

        for (int i = 0; i < featuresNodes.getLength(); i++) {
            Node node = featuresNodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element feature = (Element) node;

                result.add(new Feature(feature.getAttribute("id"), feature.getElementsByTagName("name").item(0).getTextContent(), Boolean.valueOf(feature.getElementsByTagName("active").item(0).getTextContent())));
            }
        }
        return result;
    }

    @Override
    public void updateFeature(Feature feature) {
        try {
            NodeList featuresNodes = document.getElementsByTagName("feature");

            for (int i = 0; i < featuresNodes.getLength(); i++) {
                Node node = featuresNodes.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;
                    if (feature.getName().equals(element.getElementsByTagName("name").item(0).getTextContent())) {
                        element.getElementsByTagName("active").item(0).setTextContent(String.valueOf(feature.isActive()));
                    }

                }
            }
            writeToFile();
        } catch (TransformerException e) {
            System.out.println("There was an error writing to features file. Feature: " + feature.toString());
        }

    }

    @Override
    public void addFeature(Feature input) {
        try {
            Node features = document.getFirstChild();
            Element feature = document.createElement("feature");
            feature.setAttribute("id", input.getId());
            Element name = document.createElement("name");
            name.appendChild(document.createTextNode(input.getName()));
            feature.appendChild(name);
            Element active = document.createElement("active");
            active.appendChild(document.createTextNode(String.valueOf(input.isActive())));
            feature.appendChild(active);
            features.appendChild(feature);
            writeToFile();
        } catch (TransformerException e) {
            System.out.println("There was an error writing to features file. Feature: " + input.toString());
        }

    }

    private Document loadDocument() throws ParserConfigurationException, SAXException, IOException {
        File xmlFile = new File(filePath);
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = documentFactory
                .newDocumentBuilder();
        Document doc = documentBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();
        return doc;
    }

    private void writeToFile() throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);

        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }
}
