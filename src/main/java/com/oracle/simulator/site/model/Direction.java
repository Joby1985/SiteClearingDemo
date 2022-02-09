package com.oracle.simulator.site.model;

import lombok.Getter;

/**
 * Enum representing Direction. This sets 2 unique signs xDirection (+/-/0) and
 * yDirection (+/-/0) based on human readable directions - NORTH, EAST, SOUTH,
 * WEST
 *
 * @author Joby
 *
 */
@Getter
public enum Direction {
    NORTH, EAST, SOUTH, WEST;

    private int xDirection;
    private int yDirection;

    /*
     * Translate direction into X/Y axis signs(+/-/0).
     */
    Direction() {
        if (name().equals("NORTH")) {
            yDirection = 1;
        } else if (name().equals("SOUTH")) {
            yDirection = -1;
        } else if (name().equals("EAST")) {
            xDirection = 1;
        } else if (name().equals("WEST")) {
            xDirection = -1;
        }
    }
}
