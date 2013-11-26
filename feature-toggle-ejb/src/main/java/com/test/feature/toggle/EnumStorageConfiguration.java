package com.test.feature.toggle;

public enum EnumStorageConfiguration {
    XML_STORAGE_LOCATION("feature.toggle.xml.storage.location");

    private String value;

    private EnumStorageConfiguration(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
