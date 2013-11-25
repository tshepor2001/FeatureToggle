package com.test.feature.toggle;

import java.util.List;

public class FeatureStorageManagerImpl implements FeatureStorageManager {

    private final Storage storage;

    public FeatureStorageManagerImpl(Storage storage){
        this.storage = storage;

    }
    @Override
    public List<Feature> retrieveFeatures() {
        return storage.retrieveFeatures();
    }

    @Override
    public void updateFeature(Feature feature) {
        storage.updateFeature(feature);
    }

    @Override
    public void addFeature(Feature feature) {
        storage.addFeature(feature);
    }
}
