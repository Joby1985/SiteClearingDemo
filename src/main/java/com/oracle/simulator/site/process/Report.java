package com.oracle.simulator.site.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.oracle.simulator.site.exceptions.SiteSimulatorException;
import com.oracle.simulator.site.model.ReportItem;
import com.oracle.simulator.site.model.overheads.CreditOverheads;

public class Report {

    public static List<String> generateReport(Simulation simulation) throws SiteSimulatorException {
        List<String> reports = new ArrayList<>();
        String exuctedCommands = simulation.getExecutedCommands();
        reports.add(exuctedCommands);
        Map<CreditOverheads, ReportItem> report = simulation.getCostingReport();
        StringBuilder strB = new StringBuilder();
        strB.append(getPadded("Item", 105, false, ' ').toString());
        strB.append(getPadded("Quantity", 10, true, ' ').toString());
        strB.append(getPadded("Cost", 10, true, ' ').toString());

        // report.get(key)
        // for(String s : Arrays.asList("a", "b"))
        Integer totalCost = 0;
        for (CreditOverheads creditType : Arrays.asList(CreditOverheads.CREDIT_COMMN, CreditOverheads.CREDIT_FUEL,
                CreditOverheads.CREDIT_UNCLEAR_SQUARE, CreditOverheads.CREDIT_PROTECTED_TREE_REMOVAL,
                CreditOverheads.CREDIT_PAINT_DAMAGE)) {
            strB.append("\n");

            ReportItem item = report.get(creditType);
            totalCost += item.getCost();
            appendItemReport(creditType, item, report, strB);
        }
        strB.append("\n-------------------\n");
        appendItemReport(CreditOverheads.TOTAL, new ReportItem(null, totalCost), report, strB);
        reports.add(strB.toString());
        return reports;
    }

    /**
     * Append a report item (row of information to the costing part of the report)
     *
     * @param overheadType
     * @param report
     * @param strB
     */
    private static void appendItemReport(CreditOverheads overheadType, ReportItem item,
            Map<CreditOverheads, ReportItem> report, StringBuilder strB) {
        String label = null;
        switch (overheadType) {
        case CREDIT_COMMN:
            label = "communication overhead";
            break;
        case CREDIT_FUEL:
            label = "fuel usage";
            break;
        case CREDIT_UNCLEAR_SQUARE:
            label = "uncleared squares";
            break;
        case CREDIT_PROTECTED_TREE_REMOVAL:
            label = "destruction of protected tree";
            break;
        case CREDIT_PAINT_DAMAGE:
            label = "paint damage to bulldozer";
            break;
        default:
            label = "Total";
        }

        strB.append(getPadded(label, 105, false, ' ').toString());
        strB.append(getPadded((item.getQuantity() == null ? "" : Integer.toString(item.getQuantity())), 10, true, ' ')
                .toString());
        strB.append(
                getPadded((item.getCost() == null ? "" : Integer.toString(item.getCost())), 10, true, ' ').toString());
    }

    private static String getPadded(String input, int totalLength, boolean leftPadding, Character padding) {
        int lengthToPad = 0;

        StringBuilder builder = new StringBuilder();
        String toPad = Character.toString(padding);
        if (input == null || input.isEmpty()) {
            lengthToPad = totalLength;
        } else {
            lengthToPad = totalLength - input.length();
        }
        if (!leftPadding) {
            builder.append(input);
        }
        for (int i = 0; i < lengthToPad; i++) {
            builder.append(toPad);
        }
        if (leftPadding) {
            builder.append(input);
        }
        return builder.toString();
    }
}
