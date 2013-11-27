package com.test.feature.toggle;

public enum EnumStorageHandler {
    XML_FILE(XMLStorage.class), DATABASE(Class.class);

    private Class clazz;

    EnumStorageHandler(Class clazz) {
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }
}
