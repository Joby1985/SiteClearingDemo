package com.oracle.simulator.site.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Model Representing Position of Bulldozer. (Coordinates + Direction)
 *
 * @author Joby
 *
 */
@Getter
@Setter
public class Position extends Coordinates {
    private Direction direction;

    public Position(int xPos, int yPos, Direction direction) {
        super(xPos, yPos);
        this.direction = direction;
    }

    @Override
    public Position clone() {
        return new Position(getXPos(), getYPos(), getDirection());
    }

    @Override
    public String toString() {
        return super.getXPos() + "," + super.getYPos() + "," + direction.name();
    }

    public void incrementXPosition(int increment) {
        super.setXPos(getXPos() + increment);
    }

    public void incrementYPosition(int increment) {
        super.setYPos(getYPos() + increment);
    }
}
