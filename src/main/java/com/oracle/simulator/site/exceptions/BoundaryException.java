package com.oracle.simulator.site.exceptions;

public class BoundaryException extends SiteSimulatorException {

    public BoundaryException(String message) {
        super(message);
    }

    public BoundaryException(String message, Throwable t) {
        super(message, t);
    }
}
