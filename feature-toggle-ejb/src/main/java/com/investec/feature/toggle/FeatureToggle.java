package com.investec.feature.toggle;

import com.investec.feature.toggle.FeatureToggleMBean;
import org.jboss.annotation.ejb.Management;
import org.jboss.annotation.ejb.Service;

import java.util.List;


@Service(name="FeatureToggle")
@Management(FeatureToggleMBean.class)
public class FeatureToggle  implements FeatureToggleMBean{

    @Override
    public void refresh() throws Exception {
        System.out.println("*****************************FEATURE TOGGLE*****************************");
    }

    @Override
    public void stopFeature(String feature) throws Exception {
        System.out.println("*****************************FEATURE TOGGLE*****************************");
    }

    @Override
    public void addFeature(String feature, boolean active) throws Exception {
        System.out.println("*****************************FEATURE TOGGLE*****************************");
    }

    @Override
    public void startFeature(String feature) throws Exception {
        System.out.println("*****************************FEATURE TOGGLE*****************************");
    }

    @Override
    public void start() throws Exception {
        System.out.println("*****************************FEATURE TOGGLE*****************************");
        System.out.println("*****************************    START     *****************************");
        FeatureStorageManager manager = new FeatureStorageManagerImpl(new StorageXMLImpl());
        List<Feature> features = manager.retrieveFeatures();
        bindFeaturesToJndi(features);
        System.out.println("*****************************   COMPLETE   *****************************");

    }

    private void bindFeaturesToJndi(List<Feature> features) {
        for(Feature feature : features){
           bind(feature);
        }
    }

    private void bind(Feature feature) {
        System.out.println("   -- bound feature: " + feature.getName() + " to: " + EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
        System.out.println("            name: " + feature.getName());
        System.out.println("            id: " + feature.getId());
        System.out.println("            active: " + feature.isActive());
    }
}
