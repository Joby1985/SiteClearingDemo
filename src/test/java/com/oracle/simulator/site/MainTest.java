package com.oracle.simulator.site;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.oracle.simulator.site.exceptions.SiteSimulatorException;
import com.oracle.simulator.site.model.Bulldozer;
import com.oracle.simulator.site.model.Direction;
import com.oracle.simulator.site.model.Position;
import com.oracle.simulator.site.model.SiteCharacteristic;
import com.oracle.simulator.site.model.SiteMap;
import com.oracle.simulator.site.process.Report;
import com.oracle.simulator.site.process.Simulation;

public class MainTest {
    private SiteMap siteMap;
    private File simulationData;

    @Before
    public void initialize() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("data.txt").getFile());
        String absolutePath = file.getAbsolutePath();

        siteMap = Main.loadSiteMap(absolutePath);

        simulationData = new File(classLoader.getResource("simulationCommands.txt").getFile());
        System.out.println(simulationData);
    }

    // @Test
    public void testLoadedSiteMap() {
        Assert.assertNotNull(siteMap);
        Assert.assertEquals(SiteCharacteristic.ROCKY_LAND, siteMap.getCharacteristicAt(0, -2));
        Assert.assertEquals(SiteCharacteristic.REMOVABLE_TREE, siteMap.getCharacteristicAt(2, 0));
        Assert.assertEquals(SiteCharacteristic.PROTECTED_TREE, siteMap.getCharacteristicAt(7, -1));
        Assert.assertTrue(siteMap.isBoundedMove(new Position(0, 0, Direction.EAST)));
        Assert.assertTrue(siteMap.isBoundedMove(new Position(0, -4, Direction.EAST)));
        Assert.assertTrue(siteMap.isBoundedMove(new Position(9, 0, Direction.EAST)));
        Assert.assertTrue(siteMap.isBoundedMove(new Position(9, -4, Direction.EAST)));

        Assert.assertFalse(siteMap.isBoundedMove(new Position(0, -5, Direction.EAST)));
        Assert.assertFalse(siteMap.isBoundedMove(new Position(-1, -4, Direction.EAST)));

        Assert.assertEquals("o o t o o o o o o o\n" + "o o o o o o o T o o\n" + "r r r o o o o T o o\n"
                + "r r r r o o o o o o\n" + "r r r r r t o o o o\n", siteMap.toString());
    }

    @Test
    public void testSimulation() {
        Simulation simulation = new Simulation(new Bulldozer(new Position(-1, 0, Direction.EAST)), siteMap,
                simulationData);
        try {
            simulation.simulate();
            List<String> simulationResults = Report.generateReport(simulation);
            System.out.println("\nThe simulation has ended at your request. These are the commands you issued:\n"
                    + simulationResults.get(0));
            System.out.println("\nThe costs for this land clearing operation were:\n\n" + simulationResults.get(1));
        } catch (SiteSimulatorException e) {
            System.out.println("Aborting this simulation : " + e.getMessage());
        }
    }
}
