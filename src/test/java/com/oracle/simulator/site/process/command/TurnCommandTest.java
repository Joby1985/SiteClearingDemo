package com.oracle.simulator.site.process.command;

import org.junit.Assert;
import org.junit.Test;

import com.oracle.simulator.site.exceptions.SiteSimulatorException;
import com.oracle.simulator.site.model.Bulldozer;
import com.oracle.simulator.site.model.Direction;
import com.oracle.simulator.site.model.Position;

/**
 * Test the Turn command
 *
 * @author Joby
 *
 */
public class TurnCommandTest {

    @Test
    public void testTurnRightWhenInEastDirection() throws SiteSimulatorException {
        Bulldozer bulldozer = new Bulldozer(new Position(0, 0, Direction.EAST));
        Command command = new TurnCommand(bulldozer, "Right");
        command.execute();
        Assert.assertEquals(Direction.SOUTH, bulldozer.getPosition().getDirection());
    }

    @Test
    public void testTurnLeftWhenInEastDirection() throws SiteSimulatorException {
        Bulldozer bulldozer = new Bulldozer(new Position(0, 0, Direction.EAST));
        Command command = new TurnCommand(bulldozer, "Left");
        command.execute();
        Assert.assertEquals(Direction.NORTH, bulldozer.getPosition().getDirection());
    }

    @Test
    public void testTurnRightWhenInSouthDirection() throws SiteSimulatorException {
        Bulldozer bulldozer = new Bulldozer(new Position(0, 0, Direction.SOUTH));
        Command command = new TurnCommand(bulldozer, "Right");
        command.execute();
        Assert.assertEquals(Direction.WEST, bulldozer.getPosition().getDirection());
    }

    @Test
    public void testTurnLeftWhenInSouthDirection() throws SiteSimulatorException {
        Bulldozer bulldozer = new Bulldozer(new Position(0, 0, Direction.SOUTH));
        Command command = new TurnCommand(bulldozer, "Left");
        command.execute();
        Assert.assertEquals(Direction.EAST, bulldozer.getPosition().getDirection());
    }

    @Test
    public void testTurnRightWhenInWestDirection() throws SiteSimulatorException {
        Bulldozer bulldozer = new Bulldozer(new Position(0, 0, Direction.WEST));
        Command command = new TurnCommand(bulldozer, "Right");
        command.execute();
        Assert.assertEquals(Direction.NORTH, bulldozer.getPosition().getDirection());
    }

    @Test
    public void testTurnLeftWhenInWestDirection() throws SiteSimulatorException {
        Bulldozer bulldozer = new Bulldozer(new Position(0, 0, Direction.WEST));
        Command command = new TurnCommand(bulldozer, "Left");
        command.execute();
        Assert.assertEquals(Direction.SOUTH, bulldozer.getPosition().getDirection());
    }

    @Test
    public void testTurnRightWhenInNorthDirection() throws SiteSimulatorException {
        Bulldozer bulldozer = new Bulldozer(new Position(0, 0, Direction.NORTH));
        Command command = new TurnCommand(bulldozer, "Right");
        command.execute();
        Assert.assertEquals(Direction.EAST, bulldozer.getPosition().getDirection());
    }

    @Test
    public void testTurnLeftWhenInNorthDirection() throws SiteSimulatorException {
        Bulldozer bulldozer = new Bulldozer(new Position(0, 0, Direction.NORTH));
        Command command = new TurnCommand(bulldozer, "Left");
        command.execute();
        Assert.assertEquals(Direction.WEST, bulldozer.getPosition().getDirection());
    }
}
