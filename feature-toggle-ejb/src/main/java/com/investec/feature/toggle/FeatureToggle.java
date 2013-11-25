package com.investec.feature.toggle;

import com.investec.feature.toggle.FeatureToggleMBean;
import org.jboss.annotation.ejb.Management;
import org.jboss.annotation.ejb.Service;
import org.xml.sax.SAXException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;


@Service(name="FeatureToggle")
@Management(FeatureToggleMBean.class)
public class FeatureToggle  implements FeatureToggleMBean{

    @Override
    public void refresh() throws Exception {
        System.out.println("*****************************FEATURE TOGGLE*****************************");
        System.out.println("*****************************    REFRESH   *****************************");
        FeatureStorageManager manager = new FeatureStorageManagerImpl(new StorageXMLImpl());
        List<Feature> features = manager.retrieveFeatures();
        for (Feature feature : features){
            unbind(feature);
            bind(feature);
        }
        System.out.println("*****************************   COMPLETE   *****************************");
    }

    @Override
    public void stopFeature(String featureName) throws Exception {
        System.out.println("*****************************FEATURE TOGGLE*****************************");
        Feature feature = new Feature(null, featureName, false);
        FeatureStorageManager manager = new FeatureStorageManagerImpl(new StorageXMLImpl());
        manager.updateFeature(feature);
        unbind(feature);
        bind(feature);

    }

    @Override
    public void addFeature(String featureName, boolean active) throws Exception {
        System.out.println("*****************************FEATURE TOGGLE*****************************");
        Feature feature = new Feature(null, featureName, active);
        FeatureStorageManager manager = new FeatureStorageManagerImpl(new StorageXMLImpl());
        manager.addFeature(feature);
        unbind(feature);
        bind(feature);
    }

    @Override
    public void startFeature(String featureName) throws Exception {
        System.out.println("*****************************FEATURE TOGGLE*****************************");
        Feature feature = new Feature(null, featureName, true);
        FeatureStorageManager manager = new FeatureStorageManagerImpl(new StorageXMLImpl());
        manager.updateFeature(feature);
        unbind(feature);
        bind(feature);
    }

    @Override
    public void start() throws Exception {
        System.out.println("*****************************FEATURE TOGGLE*****************************");
        System.out.println("*****************************    START     *****************************");
        FeatureStorageManager manager = new FeatureStorageManagerImpl(new StorageXMLImpl());
        List<Feature> features = manager.retrieveFeatures();
        for (Feature feature : features){
            bind(feature);
        }
        System.out.println("*****************************   COMPLETE   *****************************");

    }



    private void bind(Feature feature) throws NamingException {
        InitialContext ctx = new InitialContext();
        ctx.createSubcontext("config-");
        ctx.bind(feature.getName(), feature.isActive());
        System.out.println("   -- bound feature: " + feature.getName() + " to: " + EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
        System.out.println("            name: " + feature.getName());
        System.out.println("            id: " + feature.getId());
        System.out.println("            active: " + feature.isActive());
        Object obj = ctx.lookup(feature.getName());
        System.out.println("***" + obj);
    }

    private void unbind(Feature feature) {
        System.out.println("   -- ubound feature: " + feature.getName() + " from: " + EnumJndiConstants.FEATURE_ROOT_PATH.getValue());

    }
}
