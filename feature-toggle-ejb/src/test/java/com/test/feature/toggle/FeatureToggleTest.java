package com.test.feature.toggle;

import org.jboss.naming.NonSerializableFactory;
import org.junit.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class FeatureToggleTest {
    @BeforeClass
    public static void setUp() throws Exception {

        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
        System.setProperty(EnumStorageConfiguration.XML_STORAGE_LOCATION.getValue(), "/usr/local/application/feature-toggle/test/features.xml");

    }

    @Before
    public void initialise() {
        InitialContext ctx = null;
        try {
            ctx = new InitialContext();
            ctx.unbind(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
            NonSerializableFactory.unbind(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());

            ctx.bind(EnumJndiConstants.FEATURE_ROOT_PATH.getValue(), new Properties());
        } catch (Exception e) {

        } finally {
            try {
                ctx = new InitialContext();
                ctx.bind(EnumJndiConstants.FEATURE_ROOT_PATH.getValue(), new Properties());
            } catch (NamingException e) {

            }
        }
    }


    @Test
    public void should_reload_features_from_storage() throws Exception {
        FeatureToggle toggle = new FeatureToggle();
        toggle.reloadFeaturesFromStorage();
        InitialContext ctx = new InitialContext();
        Properties props = (Properties) ctx.lookup(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
        int size = props.size();
        Assert.assertTrue("properties loaded", size > 0);
    }

    @Test
    public void should_disable_feature() throws Exception {
        setupTestFeature(true);
        new FeatureToggle().disableFeature("test");

        Properties props = (Properties) new InitialContext().lookup(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
        Boolean result = (Boolean) props.get("test");
        Assert.assertFalse(result);
    }

    private void setupTestFeature(boolean active) throws NamingException {
        Properties initProps = (Properties) new InitialContext().lookup(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
        initProps.put("test", active);
        NonSerializableFactory.bind(EnumJndiConstants.FEATURE_ROOT_PATH.getValue(), initProps);
    }

    @Test
    public void should_add_new_feature() throws Exception {

        FeatureToggle toggle = new FeatureToggle();
        toggle.addFeature("test", false);
        InitialContext ctx = new InitialContext();
        Properties props = (Properties) ctx.lookup(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
        Boolean result = (Boolean) props.get("test");
        Assert.assertNotNull(result);

    }

    @Test
    public void should_enable_feature() throws Exception {
        setupTestFeature(false);
        FeatureToggle toggle = new FeatureToggle();
        toggle.enableFeature("test");
        Properties props = (Properties) new InitialContext().lookup(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
        Boolean result = (Boolean) props.get("test");
        Assert.assertTrue(result);
    }

    @Test
    public void should_load_all_features() throws Exception {
        FeatureToggle toggle = new FeatureToggle();
        toggle.start();
        InitialContext ctx = new InitialContext();
        Properties props = (Properties) ctx.lookup(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
        int size = props.size();
        Assert.assertTrue("properties loaded", size > 0);
    }

    @Test
    public void testPrintAllFeatures() throws Exception {
        FeatureToggle toggle = new FeatureToggle();
        toggle.printAllFeatures();
    }

    @Test
    public void should_remove_all_features() throws Exception {
        try {
            FeatureToggle toggle = new FeatureToggle();
            toggle.stopAllFeatures();
            InitialContext ctx = new InitialContext();
            Properties props = (Properties) ctx.lookup(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
        } catch (NamingException e) {
            String expectedMsg = "Name config.application.features. is not bound in this Context";
            Assert.assertEquals(expectedMsg, e.getMessage());
        }

    }

    @Test
    public void should_run_select() throws Exception {
        FeatureToggle toggle = new FeatureToggle();
        toggle.runSelectStatement();
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        String filePath = System.getProperty(EnumStorageConfiguration.XML_STORAGE_LOCATION.getValue());
        delete(new File(filePath));

    }

    private static void delete(File file) throws IOException {

        if (file.isDirectory()) {
            if (file.list().length == 0) {
                file.delete();
            }

        } else {
            file.delete();
            delete(file.getParentFile());
        }
    }

}
