package com.test.feature.toggle;

import junit.framework.Assert;
import org.jboss.naming.NonSerializableFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class FeatureUtilTest {

    @BeforeClass
    public static void setup() {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
    }

    @Before
    public void initialise() {
        InitialContext ctx;
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
    public void should_return_active_feature() throws Exception {
        setupTestFeature(true);
        Assert.assertTrue(FeatureUtil.isFeatureActive("test"));
    }

    @Test
    public void should_return_inactive_feature() throws Exception {
        setupTestFeature(false);
        Assert.assertTrue(!FeatureUtil.isFeatureActive("test"));
    }

    @Test(expected = FeatureToggleException.class)
    public void should_throw_exception_when_feature_is_not_available() throws Exception {
        boolean result = FeatureUtil.isFeatureActive("XXX");

    }

    private void setupTestFeature(boolean active) throws NamingException {
        Properties initProps = (Properties) new InitialContext().lookup(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
        initProps.put("test", active);
        NonSerializableFactory.bind(EnumJndiConstants.FEATURE_ROOT_PATH.getValue(), initProps);
    }
}
