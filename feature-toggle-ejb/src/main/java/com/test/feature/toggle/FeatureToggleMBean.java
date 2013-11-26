package com.test.feature.toggle;

public interface FeatureToggleMBean {
    public void refresh() throws Exception;

    public void stopFeature(String feature) throws Exception;

    public void addFeature(String feature, boolean active) throws Exception;

    public void startFeature(String feature) throws Exception;

    public void start() throws Exception;

    public void printFeatures() throws Exception;

    public void stopAllFeatures() throws Exception;

}
