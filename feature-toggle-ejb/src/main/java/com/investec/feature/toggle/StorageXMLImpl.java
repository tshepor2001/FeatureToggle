package com.investec.feature.toggle;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StorageXMLImpl implements Storage {

    private final NodeList featuresNodes;

    public StorageXMLImpl() throws IOException, SAXException, ParserConfigurationException {
        featuresNodes = loadFromFile();
    }

    @Override
    public List<Feature> retrieveFeatures() {
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

    }

    @Override
    public void addFeature(Feature feature) {
    }



    private NodeList loadFromFile() throws ParserConfigurationException, SAXException, IOException {
        File xmlFile = new File("/usr/local/application/feature-toggle/features.xml");
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = documentFactory
                .newDocumentBuilder();
        Document doc = documentBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();
        return doc.getElementsByTagName("feature");
    }
}
