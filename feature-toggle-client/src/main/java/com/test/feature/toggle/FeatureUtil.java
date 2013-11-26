package com.test.feature.toggle;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class FeatureUtil {

    public static boolean isFeatureActive(String featureName) throws FeatureNotAvailableException {
        boolean result = false;
        try {
            InitialContext ctx = new InitialContext();
            Properties props = (Properties) ctx.lookup(EnumJndiConstants.FEATURE_ROOT_PATH.getValue());
            if (props.get(featureName) == null) {
                throw new FeatureNotAvailableException("Feature " + featureName + " is not available");
            }
            result = (Boolean) props.get(featureName);
        } catch (NamingException e) {
            throw new FeatureNotAvailableException(e);
        }
        return result;

    }

}
