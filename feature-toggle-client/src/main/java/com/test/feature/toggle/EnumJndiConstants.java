package com.test.feature.toggle;

public enum EnumJndiConstants {
    FEATURE_ROOT_PATH("config.application.features.");

    private final String value;

    EnumJndiConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
