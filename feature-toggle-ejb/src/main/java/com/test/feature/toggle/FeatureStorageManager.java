package com.test.feature.toggle;

import javax.xml.transform.TransformerException;
import java.util.List;

public interface FeatureStorageManager {
    public List<Feature> retrieveFeatures();

    public void updateFeature(Feature feature) throws TransformerException;

    public void addFeature(Feature feature) throws TransformerException;
}
