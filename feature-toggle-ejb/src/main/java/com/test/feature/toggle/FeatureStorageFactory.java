package com.test.feature.toggle;

public class FeatureStorageFactory {

    public static Storage createStorage(EnumStorageHandler storageHandler) throws IllegalAccessException, InstantiationException {

        return (Storage) storageHandler.getClazz().newInstance();
    }
}
