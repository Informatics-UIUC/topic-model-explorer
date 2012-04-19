package org.seasr.services.topicmodel.gwt.shared.exceptions;

public class TokenNotFoundException extends Exception {

    private static final long serialVersionUID = -7253402769036384400L;

    public TokenNotFoundException(String token) {
        super("Token '" + token + "' was not found in the database");
    }
}
