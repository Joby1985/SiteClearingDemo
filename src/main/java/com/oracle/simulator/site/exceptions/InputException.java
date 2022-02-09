package com.oracle.simulator.site.exceptions;

public class InputException extends SiteSimulatorException {

    public InputException(String message) {
        super(message);
    }

    public InputException(String message, Throwable t) {
        super(message, t);
    }
}
