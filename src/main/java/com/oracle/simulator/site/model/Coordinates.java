package com.oracle.simulator.site.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Co-ordinate representing a position in the SiteMap.
 *
 * @author Joby
 *
 */
@Data
@AllArgsConstructor
public class Coordinates {
    private int xPos = 0;
    private int yPos = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        // Relaxed check so that Position objects also can be used as key
        if (o == null || !(o instanceof Coordinates)) {
            return false;
        }
        Coordinates key = (Coordinates) o;
        return (xPos == key.xPos) && (yPos == key.yPos);
    }

    @Override
    public int hashCode() {
        int result = xPos;
        result = 31 * result + yPos;
        return result;
    }
}
