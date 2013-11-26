package com.test.feature.toggle;

import javax.xml.transform.TransformerException;
import java.util.List;

public interface Storage {
    List<Feature> retrieveFeatures();

    void updateFeature(Feature feature) throws TransformerException;

    void addFeature(Feature feature) throws TransformerException;
}
