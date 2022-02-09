package com.oracle.simulator.site.process;

import java.io.File;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.oracle.simulator.site.Main;
import com.oracle.simulator.site.exceptions.SiteSimulatorException;
import com.oracle.simulator.site.model.Bulldozer;
import com.oracle.simulator.site.model.Direction;
import com.oracle.simulator.site.model.Position;
import com.oracle.simulator.site.model.ReportItem;
import com.oracle.simulator.site.model.SiteMap;
import com.oracle.simulator.site.model.overheads.CreditOverheads;

public class SimulationTest {

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

    @Test
    public void testSimulation() throws SiteSimulatorException {
        Bulldozer bulldozer = new Bulldozer(new Position(-1, 0, Direction.EAST));
        Simulation simulation = new Simulation(bulldozer, siteMap, simulationData);
        simulation.simulate();
        Assert.assertEquals("advance 4, turn right, advance 4, turn left, advance 2, advance 4, turn left",
                simulation.getExecutedCommands());

        Map<CreditOverheads, ReportItem> costingReport = simulation.getCostingReport();
        Assert.assertEquals(5, costingReport.size());
        Assert.assertEquals("ReportItem(quantity=7, cost=7)",
                costingReport.get(CreditOverheads.CREDIT_COMMN).toString());
        Assert.assertEquals("ReportItem(quantity=19, cost=19)",
                costingReport.get(CreditOverheads.CREDIT_FUEL).toString());
        Assert.assertEquals("ReportItem(quantity=1, cost=2)",
                costingReport.get(CreditOverheads.CREDIT_PAINT_DAMAGE).toString());
        Assert.assertEquals("ReportItem(quantity=0, cost=0)",
                costingReport.get(CreditOverheads.CREDIT_PROTECTED_TREE_REMOVAL).toString());
        Assert.assertEquals("ReportItem(quantity=36, cost=108)",
                costingReport.get(CreditOverheads.CREDIT_UNCLEAR_SQUARE).toString());
    }
}
