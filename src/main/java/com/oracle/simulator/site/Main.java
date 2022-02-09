package com.oracle.simulator.site;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.simulator.site.exceptions.SiteSimulatorException;
import com.oracle.simulator.site.model.Bulldozer;
import com.oracle.simulator.site.model.Direction;
import com.oracle.simulator.site.model.Position;
import com.oracle.simulator.site.model.SiteMap;
import com.oracle.simulator.site.process.Report;
import com.oracle.simulator.site.process.Simulation;
import com.oracle.simulator.site.process.SiteMapLoader;

public class Main {
    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SiteMap siteMap = loadSiteMap(args != null && args.length > 0 ? args[0] : null);

        String simulationFileName = (args != null && args.length > 1 ? args[1] : null);
        if (siteMap != null) {
            doSimulationAndReport(siteMap, simulationFileName);
        } else {
            System.out.println("Error: No sitemap could be found");
            LOGGER.error("Error: No sitemap could be found");
        }

    }

    public static void doSimulationAndReport(SiteMap siteMap, String simulationFileName) {
        File simulationFile = null;
        if (simulationFileName != null && !simulationFileName.trim().isEmpty()) {
            simulationFile = new File(simulationFileName);
        }
        // The bulldozer starts from North-West block corner outside of the site (-1,0)
        Simulation simulation = new Simulation(new Bulldozer(new Position(-1, 0, Direction.EAST)), siteMap,
                simulationFile);
        try {
            simulation.simulate();
            List<String> simulationResults = Report.generateReport(simulation);
            System.out.println("\nThe simulation has ended at your request. These are the commands you issued:\n"
                    + simulationResults.get(0));
            System.out.println("\nThe costs for this land clearing operation were:\n\n" + simulationResults.get(1));
        } catch (SiteSimulatorException e) {
            System.out.println("\nAborting this simulation : " + e.getMessage());
            LOGGER.warn("Aborting simulation : " + e.getMessage());
        }
    }

    public static SiteMap loadSiteMap(String inputSiteMapFileName) {
        File file = null;
        if (inputSiteMapFileName != null && !inputSiteMapFileName.trim().isEmpty()) {
            file = new File(inputSiteMapFileName);
        }
        SiteMapLoader loader = new SiteMapLoader(file);
        SiteMap siteMap = null;
        try {
            siteMap = loader.loadSiteMap();
        } catch (SiteSimulatorException e) {
            LOGGER.error("Aborting program : " + e.getMessage());
            return null;
        }
        return siteMap;
    }
}
