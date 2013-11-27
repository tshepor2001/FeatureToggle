package com.test.feature.toggle;

import junit.framework.Assert;
import org.junit.Test;


public class FeatureStorageFactoryTest {
    @Test
    public void should_return_xml_storage() throws Exception {
        Storage storage = FeatureStorageFactory.createStorage(EnumStorageHandler.XML_FILE);
        Assert.assertTrue(storage instanceof XMLStorage);
    }

    @Test(expected = IllegalAccessException.class)
    public void should_return_database_storage() throws Exception {
        Storage storage = FeatureStorageFactory.createStorage(EnumStorageHandler.DATABASE);

    }
}
