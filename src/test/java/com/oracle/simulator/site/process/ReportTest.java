package com.oracle.simulator.site.process;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.oracle.simulator.site.Main;
import com.oracle.simulator.site.exceptions.SiteSimulatorException;
import com.oracle.simulator.site.model.Bulldozer;
import com.oracle.simulator.site.model.Direction;
import com.oracle.simulator.site.model.Position;
import com.oracle.simulator.site.model.SiteMap;

public class ReportTest {

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
    public void testReport() throws SiteSimulatorException {
        Bulldozer bulldozer = new Bulldozer(new Position(-1, 0, Direction.EAST));
        Simulation simulation = new Simulation(bulldozer, siteMap, simulationData);
        simulation.simulate();
        List<String> reportList = Report.generateReport(simulation);
        Assert.assertEquals("advance 4, turn right, advance 4, turn left, advance 2, advance 4, turn left",
                reportList.get(0));
        String replacedWhiteSpaces = reportList.get(1).replaceAll("\\s+", ",");
        Assert.assertEquals(
                "Item,Quantity,Cost,communication,overhead,7,7,fuel,usage,19,19,uncleared,squares,36,108,destruction,of,protected,tree,0,0,paint,damage,to,bulldozer,1,2,-------------------,Total,136",
                replacedWhiteSpaces);

    }

}
