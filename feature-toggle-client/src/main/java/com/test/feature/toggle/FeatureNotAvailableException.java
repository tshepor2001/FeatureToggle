package com.test.feature.toggle;

import javax.naming.NamingException;

public class FeatureNotAvailableException extends Exception {

    public FeatureNotAvailableException(String message) {
        super(message);
    }

    public FeatureNotAvailableException(NamingException e) {
        super(e);
    }
}
