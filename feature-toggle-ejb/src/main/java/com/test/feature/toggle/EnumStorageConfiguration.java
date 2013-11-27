package com.test.feature.toggle;

public enum EnumStorageConfiguration {
    XML_STORAGE_LOCATION("feature.toggle.xml.storage.location"),
    DEFAULT_STORAGE_LOCATION("/usr/local/application/feature-toggle/features.xml");

    private String value;

    private EnumStorageConfiguration(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
