package com.test.feature.toggle;

/**
 * Created with IntelliJ IDEA.
 * User: Tshepo.Ramatsui
 * Date: 11/25/13
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FeatureToggleMBean {
    public void refresh() throws Exception;
    public void stopFeature(String feature) throws Exception;
    public void addFeature(String feature, boolean active) throws Exception;
    public void startFeature(String feature) throws Exception;
    public void start() throws Exception;

}
