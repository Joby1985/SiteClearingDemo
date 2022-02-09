package com.oracle.simulator.site.exceptions;

public abstract class SiteSimulatorException extends Exception {
    public SiteSimulatorException(String message) {
        this(message, null);
    }

    public SiteSimulatorException(String message, Throwable t) {
        super(message, t);
    }
}
