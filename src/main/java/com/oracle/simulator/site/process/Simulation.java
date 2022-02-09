package com.oracle.simulator.site.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.simulator.site.exceptions.InputException;
import com.oracle.simulator.site.exceptions.SiteSimulatorException;
import com.oracle.simulator.site.model.Bulldozer;
import com.oracle.simulator.site.model.Coordinates;
import com.oracle.simulator.site.model.ReportItem;
import com.oracle.simulator.site.model.SiteMap;
import com.oracle.simulator.site.model.overheads.CostOverheadsInfo;
import com.oracle.simulator.site.model.overheads.CreditOverheads;
import com.oracle.simulator.site.process.command.AdvanceCommand;
import com.oracle.simulator.site.process.command.Command;
import com.oracle.simulator.site.process.command.TurnCommand;

/**
 * Class that handles a single simulation on the given Site for the Bulldozer.
 *
 * @author Joby
 *
 */
public class Simulation {
    private final Bulldozer bulldozer;
    private final SiteMap sitemap;
    private final Map<CreditOverheads, ReportItem> costingReport = new HashMap<>();
    private String executedCommands;
    private File inputFile;

    public Simulation(Bulldozer bulldozer, SiteMap sitemap, File inputFile) {
        this.bulldozer = bulldozer;
        this.sitemap = sitemap;
        this.inputFile = inputFile;
    }

    private static Logger LOGGER = LoggerFactory.getLogger(Simulation.class);

    public void simulate() throws SiteSimulatorException {
        System.out.println("\n\nWelcome to the Aconex site clearing simulator. This is a map of the site:\n\n"
                + sitemap.toString());
        System.out.println(
                "\nThe bulldozer is currently located at the Northern edge of the site, immediately to the West of the site, and facing East.");
        System.out.println("\nEnter the commands for simulation, press q/quit to end:");
        List<Command> commands = new ArrayList<>();
        parseCommands(commands);

        StringJoiner commandsExecuted = new StringJoiner(", ");
        Map<CreditOverheads, Integer> overheadQuantities = new HashMap<>();
        for (Command command : commands) {
            command.execute();
            commandsExecuted.add(command.getDescription());

            Map<CreditOverheads, Integer> commandOverheads = command.getOverheadQuantities();
            if (commandOverheads != null) {
                commandOverheads.forEach((key, value) -> {
                    overheadQuantities.merge(key, value, Integer::sum);
                });
            }
        }
        executedCommands = commandsExecuted.toString();

        calculateGeneralOverheadsQuantity(commands, overheadQuantities);
        calculateCosts(overheadQuantities);
    }

    public Map<CreditOverheads, ReportItem> getCostingReport() {
        return costingReport;
    }

    public String getExecutedCommands() {
        return executedCommands;
    }

    /**
     * calculate the quantity of communication overheads and unclear land.
     *
     * @param commands
     * @param overheadQuantities
     */
    private void calculateGeneralOverheadsQuantity(List<Command> commands,
            Map<CreditOverheads, Integer> overheadQuantities) {
        overheadQuantities.put(CreditOverheads.CREDIT_COMMN, commands.size());
        List<Coordinates> unclearPositions = sitemap.calculateUnclearPositions();
        overheadQuantities.put(CreditOverheads.CREDIT_UNCLEAR_SQUARE, unclearPositions.size());
    }

    /**
     * Calculate net costs.
     *
     * @param overheadQuantities
     */
    private void calculateCosts(Map<CreditOverheads, Integer> overheadQuantities) {
        overheadQuantities.forEach((key, quantity) -> {
            Integer cost = quantity * CostOverheadsInfo.getCostOverhead(key);
            costingReport.put(key, new ReportItem(quantity, cost));
        });
    }

    /**
     * Parse input commands of the simulation
     *
     * @param commands
     * @throws InputException
     */
    private void parseCommands(List<Command> commands) throws InputException {
        InputStream fileStream = null;
        Scanner scanner = null;

        try {
            if (inputFile != null) {
                fileStream = new FileInputStream(inputFile);
                scanner = new Scanner(fileStream);
            } else {
                scanner = new Scanner(System.in);
            }
            String enteredLine = null;
            while (enteredLine == null
                    || !(enteredLine.equalsIgnoreCase("Q") || enteredLine.equalsIgnoreCase("QUIT"))) {
                System.out.println("(l)eft, (r)ight, (a)dvance <n>, (q)uit:");
                enteredLine = scanner.nextLine().trim();
                // Ignore any empty lines.
                if (!enteredLine.isEmpty()) {
                    enteredLine = enteredLine.toUpperCase();
                    String[] tokens = enteredLine.split("\\s+");
                    switch (tokens[0]) {
                    case "A":
                    case "ADVANCE":

                        Integer numSteps = null;
                        if (tokens.length > 1) {
                            try {
                                numSteps = Integer.parseInt(tokens[1]);
                                if (numSteps < 0) {
                                    throw new InputException(
                                            "Input error: Number of steps shuld be positive for Advance command.");
                                }
                            } catch (NumberFormatException nfe) {
                                throw new InputException(
                                        "Input error: Advance command is given non-numeric number of steps.");
                            }
                        } else {
                            throw new InputException("Input error: Advance command without number of steps to move.");
                        }
                        commands.add(new AdvanceCommand(bulldozer, sitemap, numSteps));
                        break;
                    case "R":
                    case "RIGHT":
                        commands.add(new TurnCommand(bulldozer, "RIGHT"));
                        break;
                    case "L":
                    case "LEFT":
                        commands.add(new TurnCommand(bulldozer, "LEFT"));
                        break;
                    default:
                        LOGGER.warn("Unknown command.. Ignoring...");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new InputException(
                    "Could not find the path of the file " + (inputFile != null ? inputFile.getName() : ""));
        } finally {
            // We should close only File based stream, and not System.in (So this cannot be
            // done using autoclose with a common code)
            if (inputFile != null && scanner != null) {
                scanner.close();
                try {
                    fileStream.close();
                } catch (IOException e) {
                    LOGGER.warn("Error occurred while trying to close file:" + inputFile.getName());
                }
            }
        }
    }
}
