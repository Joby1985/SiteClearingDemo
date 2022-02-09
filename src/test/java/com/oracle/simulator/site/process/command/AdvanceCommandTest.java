package com.oracle.simulator.site.process.command;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.oracle.simulator.site.Main;
import com.oracle.simulator.site.exceptions.BoundaryException;
import com.oracle.simulator.site.exceptions.SiteSimulatorException;
import com.oracle.simulator.site.model.Bulldozer;
import com.oracle.simulator.site.model.Direction;
import com.oracle.simulator.site.model.Position;
import com.oracle.simulator.site.model.SiteMap;

public class AdvanceCommandTest {
    private SiteMap siteMap;

    @Before
    public void setup() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("data.txt").getFile());
        String absolutePath = file.getAbsolutePath();

        siteMap = Main.loadSiteMap(absolutePath);
    }

    @Test
    public void testAdvancement() throws SiteSimulatorException {
        Bulldozer bulldozer = new Bulldozer(new Position(-1, 0, Direction.EAST));
        AdvanceCommand command = new AdvanceCommand(bulldozer, siteMap, 6);

        command.execute();
        Position currentPosition = bulldozer.getPosition();
        Assert.assertEquals(5, currentPosition.getXPos());
        Assert.assertEquals(0, currentPosition.getYPos());
    }

    @Test
    public void testAdvancementToBoundary() throws SiteSimulatorException {
        Bulldozer bulldozer = new Bulldozer(new Position(-1, 0, Direction.EAST));
        AdvanceCommand command = new AdvanceCommand(bulldozer, siteMap, 10);

        command.execute();
        Position currentPosition = bulldozer.getPosition();
        Assert.assertEquals(9, currentPosition.getXPos());
        Assert.assertEquals(0, currentPosition.getYPos());
    }

    @Test(expected = BoundaryException.class)
    public void testAdvancementWithOutOfBounds() throws SiteSimulatorException {
        Bulldozer bulldozer = new Bulldozer(new Position(-1, 0, Direction.EAST));
        AdvanceCommand command = new AdvanceCommand(bulldozer, siteMap, 12);
        command.execute();
    }
}
