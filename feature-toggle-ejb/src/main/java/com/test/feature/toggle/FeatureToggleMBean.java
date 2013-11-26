package com.test.feature.toggle;

public interface FeatureToggleMBean {
    public void reloadFeaturesFromStorage() throws Exception;

    public void disableFeature(String feature) throws Exception;

    public void addFeature(String feature, boolean active) throws Exception;

    public void enableFeature(String feature) throws Exception;

    public void start() throws Exception;

    public void printAllFeatures() throws Exception;

    public void stopAllFeatures() throws Exception;

}
