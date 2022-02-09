package com.oracle.simulator.site.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SiteMap model
 *
 * @author Joby
 *
 */
public class SiteMap {

    private Map<Coordinates, SiteCharacteristic> mapping = new HashMap<Coordinates, SiteCharacteristic>();

    public Map<Coordinates, SiteCharacteristic> getMapping() {
        return mapping;
    }

    private List<Coordinates> unclearPositions = new ArrayList<>();

    private int highestXaxis;
    private int lowestYaxis;

    public void addEntry(Integer xCoord, Integer yCoord, Character characteristic) {
        mapping.put(new Coordinates(xCoord, yCoord), SiteCharacteristic.mapToEnum(characteristic));
    }

    public void setBounds(int maxX, int minY) {
        highestXaxis = maxX;
        lowestYaxis = minY;
    }

    /**
     * Is the position within the site boundaries?
     *
     * @param robotPosition
     * @return
     */
    public boolean isBoundedMove(Position position) {
        return position.getXPos() >= 0 && position.getXPos() <= highestXaxis && position.getYPos() <= 0
                && position.getYPos() >= lowestYaxis;
    }

    public SiteCharacteristic getCharacteristicAt(int xPosition, int yPosition) {
        return mapping.get(new Coordinates(xPosition, yPosition));
    }

    public SiteCharacteristic getCharacteristicAt(Position position) {
        return mapping.get(position);
    }

    public void setCleared(Position position) {
        mapping.put(position, null);
    }

    public List<Coordinates> getUnclearedPositions() {
        if (unclearPositions == null) {
            calculateUnclearPositions();
        }
        return unclearPositions;
    }

    public List<Coordinates> calculateUnclearPositions() {
        unclearPositions = new ArrayList<>();
        mapping.forEach((key, value) -> {
            if (value != null) {
                unclearPositions.add(key);
            }
        });
        return unclearPositions;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        Position position = new Position(0, 0, Direction.EAST);
        for (int j = 0; j >= lowestYaxis; j--) {
            position.setYPos(j);
            for (int i = 0; i < highestXaxis; i++) {
                position.setXPos(i);
                builder.append(mapping.get(position));
                builder.append(" ");
            }
            position.setXPos(highestXaxis);
            builder.append(mapping.get(position));
            builder.append("\n");
        }
        return builder.toString();
    }
}
