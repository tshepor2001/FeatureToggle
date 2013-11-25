package com.investec.feature.toggle;

import java.util.List;

public interface FeatureStorageManager {
    public List<Feature> retrieveFeatures();
    public void updateFeature(Feature feature);
    public void addFeature(Feature feature);
}
