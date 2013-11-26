package com.test.feature.toggle;

import org.jboss.annotation.ejb.Management;
import org.jboss.annotation.ejb.Service;
import org.jboss.naming.NonSerializableFactory;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import java.util.List;
import java.util.Map;
import java.util.Properties;


@Service(name = "FeatureToggle")
@Management(FeatureToggleMBean.class)
public class FeatureToggle implements FeatureToggleMBean {

    @Override
    public void reloadFeaturesFromStorage() throws Exception {
        printStart("reload all features from storage");
        unbind();
        bindFeatures();
        printFeatures();
    }

    @Override
    public void disableFeature(String featureName) throws Exception {
        printStart("disable feature: " + featureName);

        InitialContext ctx = new InitialContext();
        try {
            Properties props = (Properties) ctx.lookup(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
            Boolean featureValue = (Boolean) props.get(featureName);
            if (featureValue == null) {
                throw new FeatureToggleException("feature does not exist");
            }
            props.put(featureName, false);
            unbind();
            bind(props);
            FeatureStorageFactory.createStorage(EnumStorageHandler.XML_FILE).updateFeature(new Feature(null, featureName, false));

        } catch (NameNotFoundException e) {
            System.out.println("   Service is currently not active");
            System.out.println("  ");

        } catch (FeatureToggleException e) {
            System.out.println("   feature: " + featureName + " does not exist, available features are:");
            System.out.println("  ");
        }

        printFeatures();
        printEnd();


    }

    @Override
    public void addFeature(String featureName, boolean active) throws Exception {
        printStart("add feature: " + featureName);
        InitialContext ctx = new InitialContext();
        Properties props;
        try {
            props = (Properties) ctx.lookup(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
            Boolean featureValue = (Boolean) props.get(featureName);
            if (featureValue != null) {
                throw new FeatureToggleException("Feature already exists");
            }
            props.put(featureName, active);
            unbind();
            bind(props);
            FeatureStorageFactory.createStorage(EnumStorageHandler.XML_FILE).addFeature(new Feature(String.valueOf(props == null ? 0 : props.size()), featureName, active));
        } catch (FeatureToggleException e) {
            System.out.println("   feature: " + featureName + " already exist, available features are:");
            System.out.println("  ");

        } catch (NameNotFoundException e) {
            System.out.println("   Service is currently not active");
            System.out.println("  ");

        }
        printFeatures();
        printEnd();
    }

    @Override
    public void enableFeature(String featureName) throws Exception {
        printStart("enable feature: " + featureName);
        InitialContext ctx = new InitialContext();
        try {
            Properties props = (Properties) ctx.lookup(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
            Boolean featureValue = (Boolean) props.get(featureName);
            if (featureValue == null) {
                throw new FeatureToggleException("feature does not exist");
            }
            props.put(featureName, true);
            unbind();
            bind(props);
            FeatureStorageFactory.createStorage(EnumStorageHandler.XML_FILE).updateFeature(new Feature("99", featureName, true));
        } catch (FeatureToggleException e) {
            System.out.println("   feature: " + featureName + " does not exist, available features are:");
            System.out.println("  ");

        } catch (NameNotFoundException e) {
            System.out.println("   service is currently not active");
            System.out.println("\n  ");
        }

        printFeatures();
        printEnd();
    }

    @Override
    public void start() throws Exception {
        printStart("initialise all features");
        unbind();
        bindFeatures();
        printFeatures();
        printEnd();

    }

    private void printEnd() {
        System.out.println("*****************************   COMPLETE   *****************************");
    }

    private void printStart(String action) {
        System.out.println("*****************************FEATURE TOGGLE*****************************");
        System.out.println("   action: " + action);
    }

    private void printFeatures() throws Exception {

        try {
            InitialContext ctx = new InitialContext();
            Properties props = (Properties) ctx.lookup(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());

            for (Map.Entry<Object, Object> prop : props.entrySet()) {

                System.out.println("    feature: " + prop.getKey());
                System.out.println("      - active: " + prop.getValue());
                System.out.println("\n");

            }
        } catch (NameNotFoundException e) {
            System.out.println("    all features are unbound.");
        }

    }

    @Override
    public void printAllFeatures() throws Exception {
        printStart("print available features");
        printFeatures();
        printEnd();
    }


    @Override
    public void stopAllFeatures() throws Exception {
        printStart("stop all features");
        unbind();
        printEnd();
    }

    private void bindFeatures() throws Exception {
        List<Feature> features = FeatureStorageFactory.createStorage(EnumStorageHandler.XML_FILE).retrieveFeatures();
        Properties props = new Properties();
        System.out.println("   binding features to: " + EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
        for (Feature feature : features) {
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
        try {
            ctx.lookup(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
            ctx.unbind(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
            NonSerializableFactory.unbind(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
            System.out.println("   removed all features from: " + EnumJndiConstants.FEATURE_ROOT_PATH.getValue());


        } catch (NameNotFoundException e) {
            System.out.println("   there are no features bound to: " + EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
        }


    }
}
