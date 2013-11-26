package com.test.feature.toggle;

import javax.naming.NamingException;

public class FeatureToggleException extends Exception {

    public FeatureToggleException(String message) {
        super(message);
    }

    public FeatureToggleException(NamingException e) {
        super(e);
    }
}
