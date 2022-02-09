package com.oracle.simulator.site.model;

/**
 * Represents a turning LEFT/RIGHT
 *
 * @author Joby
 *
 */
public enum Turn {
    LEFT, RIGHT;

    public static Turn get(String name) {
        Turn side = null;
        if (name != null && !name.trim().isEmpty()) {
            try {
                side = Turn.valueOf(name.toUpperCase());
            } catch (IllegalArgumentException ex) {
            }
        }
        return side;
    }
}
