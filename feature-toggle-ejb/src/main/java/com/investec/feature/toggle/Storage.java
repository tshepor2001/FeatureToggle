package com.investec.feature.toggle;

import java.util.List;

public interface Storage {
    List<Feature> retrieveFeatures();

    void updateFeature(Feature feature);

    void addFeature(Feature feature);
}
