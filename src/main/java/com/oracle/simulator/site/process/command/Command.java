package com.oracle.simulator.site.process.command;

import java.util.Map;

import com.oracle.simulator.site.exceptions.SiteSimulatorException;
import com.oracle.simulator.site.model.overheads.CreditOverheads;

/**
 * Abstract command that can represent a command of the Simulator
 *
 * @author Joby
 *
 */
public interface Command {
    public void execute() throws SiteSimulatorException;

    public Map<CreditOverheads, Integer> getOverheadQuantities();

    public String getDescription();
}
