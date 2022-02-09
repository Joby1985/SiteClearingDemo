package com.oracle.simulator.site.process.command;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.simulator.site.exceptions.BoundaryException;
import com.oracle.simulator.site.exceptions.SiteSimulatorException;
import com.oracle.simulator.site.model.Bulldozer;
import com.oracle.simulator.site.model.Position;
import com.oracle.simulator.site.model.SiteCharacteristic;
import com.oracle.simulator.site.model.SiteMap;
import com.oracle.simulator.site.model.overheads.CostOverheadsInfo;
import com.oracle.simulator.site.model.overheads.CreditOverheads;
import com.oracle.simulator.site.model.overheads.FuelOverheads;

import lombok.RequiredArgsConstructor;

/**
 * Command to move the bulldozer by a given number of steps in the site. (Also
 * validate that the place is within the site.) This takes the direction set and
 * appropriately sets the next location.
 *
 * @author Joby
 *
 */
@RequiredArgsConstructor
public class AdvanceCommand implements Command {
    private static Logger LOGGER = LoggerFactory.getLogger(AdvanceCommand.class);

    private final Bulldozer bulldozer;
    private final SiteMap sitemap;
    private final int stepsToMove;
    private Map<CreditOverheads, Integer> overheadQuantities;

    @Override
    public void execute() throws SiteSimulatorException {
        Position pos = bulldozer.getPosition();
        if (pos.getDirection().getXDirection() != 0 || pos.getDirection().getYDirection() != 0) {
            overheadQuantities = new HashMap<CreditOverheads, Integer>();
            moveBulldozer(pos);
        }
    }

    private void moveBulldozer(Position currentPos) throws BoundaryException {
        Position pos = bulldozer.getPosition();
        Position nextPos = pos.clone();
        // Non-zero value for X-direction indicates that it is just an X-axis movement,
        // else, Y-axis movement.

        // This is how we move. For a NORTH direction move, we will have Y-direction =
        // +1, and X-direction = 0, in each step of move.
        boolean isXAxisMovement = (pos.getDirection().getXDirection() != 0);
        int fuel_usage = 0, count_paint_damage = 0, count_protected_tree_destruction = 0;
        for (int i = 0; i < stepsToMove; i++) {
            if (isXAxisMovement) {
                nextPos.incrementXPosition(pos.getDirection().getXDirection());
            } else {
                nextPos.incrementYPosition(pos.getDirection().getYDirection());
            }
            if (sitemap.isBoundedMove(nextPos)) {
                bulldozer.setPosition(nextPos);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Bulldozer moved to position :" + nextPos);
                }
                // overheadQuantities
                SiteCharacteristic characteristic = sitemap.getCharacteristicAt(nextPos);
                if (characteristic == null) {
                    fuel_usage += CostOverheadsInfo.getFuelOverhead(FuelOverheads.VISIT_ALREADY_CLEARED);
                } else if (characteristic == SiteCharacteristic.PLAIN_LAND) {
                    fuel_usage += CostOverheadsInfo.getFuelOverhead(FuelOverheads.CLEAR_PLAIN_LAND);
                } else if (characteristic == SiteCharacteristic.ROCKY_LAND) {
                    fuel_usage += CostOverheadsInfo.getFuelOverhead(FuelOverheads.CLEAR_ROCKY_LAND);
                } else if (characteristic == SiteCharacteristic.PROTECTED_TREE
                        || characteristic == SiteCharacteristic.REMOVABLE_TREE) {
                    fuel_usage += CostOverheadsInfo.getFuelOverhead(FuelOverheads.CLEAR_TREE);

                    if (characteristic == SiteCharacteristic.PROTECTED_TREE) {
                        count_protected_tree_destruction++;
                    }

                    // If the tree is not on the last step of current command, then there can be
                    // paint damage
                    if (i != (stepsToMove - 1)) {
                        count_paint_damage++;
                    }
                }
                sitemap.setCleared(nextPos);
            } else {
                LOGGER.error("The Bulldozer would move out of the site.");
                throw new BoundaryException("Going out of bound of the site.. Simulation will stop.");
            }
        }
        overheadQuantities.put(CreditOverheads.CREDIT_FUEL, fuel_usage);
        overheadQuantities.put(CreditOverheads.CREDIT_PAINT_DAMAGE, count_paint_damage);
        overheadQuantities.put(CreditOverheads.CREDIT_PROTECTED_TREE_REMOVAL, count_protected_tree_destruction);
    }

    @Override
    public String getDescription() {
        return "advance " + stepsToMove;
    }

    @Override
    public Map<CreditOverheads, Integer> getOverheadQuantities() {
        return overheadQuantities;
    }

}
