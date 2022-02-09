package com.oracle.simulator.site.process.command;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.simulator.site.exceptions.InputException;
import com.oracle.simulator.site.exceptions.SiteSimulatorException;
import com.oracle.simulator.site.model.Bulldozer;
import com.oracle.simulator.site.model.Direction;
import com.oracle.simulator.site.model.Turn;
import com.oracle.simulator.site.model.overheads.CreditOverheads;

import lombok.AllArgsConstructor;

/**
 * Command to turn the robot LEFT or RIGHT when set on a direction
 * (NORTH/EAST/SOUTH/WEST) on the table.
 *
 * @author Joby
 *
 */
@AllArgsConstructor
public class TurnCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(TurnCommand.class);
    private static Map<Direction, Map<Turn, Direction>> directionTransform = getDirectionTransformMap();

    private Bulldozer bulldozer;
    private String turning;

    @Override
    public void execute() throws SiteSimulatorException {
        Turn side = Turn.get(turning);
        if (side != null) {
            Map<Turn, Direction> directionMapping = directionTransform.get(bulldozer.getPosition().getDirection());
            Direction targeDirection = directionMapping.get(side);
            bulldozer.getPosition().setDirection(targeDirection);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Bulldozer changed direction :" + targeDirection);
            }
        } else {
            throw new InputException("Invalid turning side specified " + turning);
        }
    }

    /*
     * Create a static map of knowledge base about the resultant direction when the
     * robot is turned LEFT/RIGHT when in a specific direction.
     *
     */
    private static Map<Direction, Map<Turn, Direction>> getDirectionTransformMap() {
        Map<Direction, Map<Turn, Direction>> directionTransform = new HashMap<Direction, Map<Turn, Direction>>();

        Map<Turn, Direction> mapping = new HashMap<Turn, Direction>();
        // When facing NORTH, LEFT turn results in WEST direction, RIGHT turn results in
        // EAST direction.
        mapping.put(Turn.LEFT, Direction.WEST);
        mapping.put(Turn.RIGHT, Direction.EAST);
        directionTransform.put(Direction.NORTH, mapping);

        // When facing EAST, LEFT turn results in NORTH direction, RIGHT turn results in
        // SOUTH direction.
        mapping = new HashMap<Turn, Direction>();
        mapping.put(Turn.LEFT, Direction.NORTH);
        mapping.put(Turn.RIGHT, Direction.SOUTH);
        directionTransform.put(Direction.EAST, mapping);

        // When facing SOUTH, LEFT turn results in EAST direction, RIGHT turn results in
        // WEST direction.
        mapping = new HashMap<Turn, Direction>();
        mapping.put(Turn.LEFT, Direction.EAST);
        mapping.put(Turn.RIGHT, Direction.WEST);
        directionTransform.put(Direction.SOUTH, mapping);

        // When facing WEST, LEFT turn results in SOUTH direction, RIGHT turn results in
        // NORTH direction.
        mapping = new HashMap<Turn, Direction>();
        mapping.put(Turn.LEFT, Direction.SOUTH);
        mapping.put(Turn.RIGHT, Direction.NORTH);
        directionTransform.put(Direction.WEST, mapping);

        return directionTransform;
    }

    @Override
    public String getDescription() {
        return "turn " + turning.toLowerCase();
    }

    @Override
    public Map<CreditOverheads, Integer> getOverheadQuantities() {
        // TODO Auto-generated method stub
        return null;
    }
}
