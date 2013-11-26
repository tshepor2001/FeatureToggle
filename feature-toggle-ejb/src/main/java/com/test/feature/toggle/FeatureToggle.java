package com.test.feature.toggle;

import org.jboss.annotation.ejb.Management;
import org.jboss.annotation.ejb.Service;
import org.jboss.naming.NonSerializableFactory;
import org.xml.sax.SAXException;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;


@Service(name = "FeatureToggle")
@Management(FeatureToggleMBean.class)
public class FeatureToggle implements FeatureToggleMBean {

    @Override
    public void refresh() throws Exception {
        printStart("refresh");
        unbind();
        bindFeatures();
        printEnd();
    }

    @Override
    public void stopFeature(String featureName) throws Exception {
        printStart("stop feature");
        InitialContext ctx = new InitialContext();
        try {
            Properties props = (Properties) ctx.lookup(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
            Boolean featureValue = (Boolean) props.get(featureName);
            if (featureValue == null) {
                throw new NameNotFoundException();
            }
            props.put(featureName, false);
            unbind();
            bind(props);
        } catch (NameNotFoundException e) {
            System.out.println("   feature: " + featureName + " does not exist, available features are:");
            System.out.println("  ");
        }

        printFeatures();
        FeatureStorageManager storageManager = new FeatureStorageManagerImpl(new StorageXMLImpl());
        storageManager.updateFeature(new Feature("99", featureName, false));


    }

    @Override
    public void addFeature(String featureName, boolean active) throws Exception {
        printStart("add feature");
        InitialContext ctx = new InitialContext();
        Properties props = null;
        try {
            props = (Properties) ctx.lookup(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
            Boolean featureValue = (Boolean) props.get(featureName);
            if (featureValue != null) {
                throw new NameNotFoundException();
            }
            props.put(featureName, active);
            unbind();
            bind(props);
        } catch (NameNotFoundException e) {
            System.out.println("   feature: " + featureName + " already exist, available features are:");
            System.out.println("  ");

        }


        FeatureStorageManager storageManager = new FeatureStorageManagerImpl(new StorageXMLImpl());
        storageManager.addFeature(new Feature(String.valueOf(props == null ? 0 : props.size() + 1), featureName, active));
        printFeatures();
    }

    @Override
    public void startFeature(String featureName) throws Exception {
        printStart("start");
        InitialContext ctx = new InitialContext();
        try {
            Properties props = (Properties) ctx.lookup(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
            Boolean featureValue = (Boolean) props.get(featureName);
            if (featureValue == null) {
                throw new NameNotFoundException();
            }
            props.put(featureName, true);
            unbind();
            bind(props);
        } catch (NameNotFoundException e) {
            System.out.println("   feature: " + featureName + " does not exist, available features are:");
            System.out.println("\n  ");
        }

        printFeatures();
        FeatureStorageManager storageManager = new FeatureStorageManagerImpl(new StorageXMLImpl());
        storageManager.updateFeature(new Feature("99", featureName, true));

    }

    @Override
    public void start() throws Exception {
        printStart("start");
        bindFeatures();
        printEnd();

    }

    private void printEnd() {
        System.out.println("*****************************   COMPLETE   *****************************");
    }

    private void printStart(String action) {
        System.out.println("*****************************FEATURE TOGGLE*****************************");
        System.out.println("   action: " + action);
    }

    @Override
    public void printFeatures() throws Exception {
        printStart("print available features");
        InitialContext ctx = new InitialContext();
        Properties props = (Properties) ctx.lookup(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());

        for (Map.Entry<Object, Object> prop : props.entrySet()) {

            System.out.println("    feature: " + prop.getKey());
            System.out.println("    -- active: " + prop.getValue());
            System.out.println("\n");

        }
        printEnd();
    }

    private void bindFeatures() throws IOException, SAXException, ParserConfigurationException, NamingException {
        FeatureStorageManager manager = new FeatureStorageManagerImpl(new StorageXMLImpl());
        List<Feature> features = manager.retrieveFeatures();
        Properties props = new Properties();
        System.out.println("   binding properties to: " + EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
        for (Feature feature : features) {
            System.out.println("    feature: " + feature.getName());
            System.out.println("    -- id: " + feature.getId());
            System.out.println("    -- active: " + feature.isActive());
            System.out.println("\n");
            props.put(feature.getName(), feature.isActive());
        }
        bind(props);
    }


    private void bind(Properties properties) throws NamingException {

        new InitialContext().bind(EnumJndiConstants.FEATURE_ROOT_PATH.getValue(), properties);
        NonSerializableFactory.bind(EnumJndiConstants.FEATURE_ROOT_PATH.getValue(), properties);
        System.out.println("   bound features to: " + EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
    }

    private void unbind() throws NamingException {
        InitialContext ctx = new InitialContext();
        ctx.unbind(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());

        NonSerializableFactory.unbind(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
        System.out.println("   ubound all features from: " + EnumJndiConstants.FEATURE_ROOT_PATH.getValue());

    }
}
