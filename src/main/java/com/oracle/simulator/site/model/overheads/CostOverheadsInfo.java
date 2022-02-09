package com.oracle.simulator.site.model.overheads;

import java.util.HashMap;
import java.util.Map;

public class CostOverheadsInfo {
    private static Map<FuelOverheads, Integer> fuelOverheads;
    private static Map<CreditOverheads, Integer> costOverheads;

    static {
        // These may be made configurable by properties file / config table data.
        fuelOverheads = new HashMap<FuelOverheads, Integer>() {
            {
                put(FuelOverheads.CLEAR_PLAIN_LAND, 1);
                put(FuelOverheads.VISIT_ALREADY_CLEARED, 1);
                put(FuelOverheads.CLEAR_ROCKY_LAND, 2);
                put(FuelOverheads.CLEAR_TREE, 2);
            }
        };

        costOverheads = new HashMap<CreditOverheads, Integer>() {
            {
                put(CreditOverheads.CREDIT_COMMN, 1);
                put(CreditOverheads.CREDIT_FUEL, 1);
                put(CreditOverheads.CREDIT_UNCLEAR_SQUARE, 3);
                put(CreditOverheads.CREDIT_PROTECTED_TREE_REMOVAL, 10);
                put(CreditOverheads.CREDIT_PAINT_DAMAGE, 2);
            }
        };
    }

    public static Integer getFuelOverhead(FuelOverheads fuelOverhead) {
        return fuelOverheads.get(fuelOverhead);
    }

    public static Integer getCostOverhead(CreditOverheads creditOverhead) {
        return costOverheads.get(creditOverhead);
    }

}
